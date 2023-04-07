package org.example.controlador;

import Utilidad.ControllerBase;
import io.javalin.Javalin;
import jakarta.servlet.http.HttpSession;
import org.example.encapsulacion.Acortador;
import org.example.encapsulacion.Usuario;
import org.example.servicios.ServiciosAcortador;
import org.example.servicios.ServiciosUsuario;
import org.jasypt.util.text.AES256TextEncryptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.*;

public class ControllerSeguridad extends ControllerBase {

    ServiciosUsuario serviciosUsuario = ServiciosUsuario.getInstancia();

    public ControllerSeguridad(Javalin app) {
        super(app);
    }

    @Override
    public void aplicarDireccionamiento() {
        app.routes(() -> {
            path("/registrar", () -> {
                get(ctx -> {
                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("titulo", "Registrar");

                    ctx.render("/templates/vista/registrar.html", modelo);
                });
                post(ctx -> {
                    String nombre = ctx.formParam("nombre");
                    String nomUsuario = ctx.formParam("usuario");
                    String pass = ctx.formParam("contrasena");
                    String confirmarPass = ctx.formParam("passwordConfirm");
                    Usuario usuario = new Usuario(nombre, nomUsuario, pass, false);
                    boolean registrado = ServiciosUsuario.getInstancia().validacionRegistro(usuario, confirmarPass);


                    System.out.println("Se registró "+ usuario.getUsuario());
                    if(registrado){
                        ctx.redirect("/login");
                        System.out.println("El usuario es: "+ usuario.getNombre());
                    }else{
                        ctx.redirect("/registrar");
                    }
                });

            });

            path("/login", () -> {
                get(ctx -> {
                    Usuario usuario = ctx.sessionAttribute("usuario");
                    String data = ctx.cookie("data");

                    if(data != null){
                        AES256TextEncryptor textEncryptor = new AES256TextEncryptor();
                        textEncryptor.setPassword("$2a$12$iDaIfhy8y7o12l4t9VEEEuPr6NG1iR/cplWVv1NATWVc2N/0L/722");
                        String username = textEncryptor.decrypt(data);
                        //Usuario tmp = serviciosUsuario.getInstancia().getUsuariotByUser(username);
                        Usuario tmp = serviciosUsuario.getInstancia().getUsuariotByUser(username);
                        if(tmp != null) {
                            usuario = tmp;
                            ctx.sessionAttribute("usuario", tmp);
                        }
                    }

                    if (usuario != null) {
                        ctx.redirect("/Seguridad/Productos");
                    }
                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("titulo", "Login");
                    ctx.render("/templates/vista/login.html", modelo);
                });
            });
        });

        app.routes(() -> {
            path("/Seguridad/", () ->{
                before(ctx -> {
                    Usuario usuario = ctx.sessionAttribute("usuario");
                    if(usuario == null){
                        ctx.redirect("/login");
                    }
                });
                post(ctx -> {
                    String nombreUsuario = ctx.formParam("usuario");
                    String password = ctx.formParam("contrasena");
                    Usuario usuario = serviciosUsuario.getInstancia().authenticateUsuario(nombreUsuario, password);
                    String checkboxValue = ctx.formParam("rememberMe");
                    if(checkboxValue != null){
                        AES256TextEncryptor textEncryptor = new AES256TextEncryptor();
                        textEncryptor.setPassword("$2a$12$iDaIfhy8y7o12l4t9VEEEuPr6NG1iR/cplWVv1NATWVc2N/0L/722");
                        String data = textEncryptor.encrypt(usuario.getUsuario());
                        ctx.cookie("data", data, 604800);
                        ctx.sessionAttribute("LoginSession", ctx.cookie("data", data, 604800));

                    }else {
                        System.out.println("No se selecciono ");
                    }

                    if(usuario != null){
                        ctx.sessionAttribute("usuario", usuario);
                        System.out.println("Se logueó el usuario: "+ usuario.getUsuario());
                        ctx.redirect("/");
                    }else {
                        ctx.redirect("/login");
                    }
                });
            });
            path("/logout", () -> {
                get(ctx -> {
                    // Obtiene la sesión actual
                    HttpSession session = ctx.req().getSession(false);
                    if (session != null) {
                        // Invalida la sesión
                        session.invalidate();
                    }
                    // Redirecciona al usuario a la página de inicio o de login
                    ctx.redirect("/");
                });
            });
        });

        app.routes(() -> {
            path("/Usuarios", () -> {
                get("/Listado", ctx -> {
                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("titulo", "Lista de Usuarios");
                    List<Usuario> lista = serviciosUsuario.findAll();
                    modelo.put("usuarios", lista);
                    modelo.put("session", ctx.sessionAttributeMap());
                    Usuario usuario = ctx.sessionAttribute("usuario");
                    modelo.put("usuario", usuario);



                    ctx.render("/templates/vista/listadoUsuarios.html", modelo);
                });

                post("/Eliminar/{idUsuario}", ctx -> {
                    String identificador = ctx.pathParam("idUsuario");

                    //Eliminar de la BD
                    Usuario usuario = serviciosUsuario.getUsuarioByID(identificador);
                    for (Acortador acortado : ServiciosAcortador.getInstancia().findAllByUser(usuario)) {
                        if(acortado != null){
                            ServiciosAcortador.getInstancia().eliminar(acortado.getIdAcortador());
                        }
                    }
                    serviciosUsuario.eliminar(identificador);
                    System.out.println("          Se elimino :" + identificador);

                    ctx.redirect("/Usuarios/Listado");
                });

                post("/HacerAdmin/{idUsuario}", ctx -> {

                });
            });
        });
    }
}
