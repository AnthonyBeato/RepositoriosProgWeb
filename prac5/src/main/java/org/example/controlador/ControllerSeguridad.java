package org.example.controlador;

import Utilidad.ControllerBase;
import io.javalin.Javalin;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;
import org.example.encapsulacion.CarroCompras;
import org.example.encapsulacion.Usuario;
import org.example.servicios.ServiciosUsuario;
import org.h2.engine.Session;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.jasypt.util.text.AES256TextEncryptor;

import jakarta.servlet.http.HttpServletResponse;


import java.util.*;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.before;
import static io.javalin.apibuilder.ApiBuilder.path;

public class ControllerSeguridad extends ControllerBase {

    ServiciosUsuario serviciosUsuario = ServiciosUsuario.getInstancia();

    public ControllerSeguridad (Javalin app){
        super(app);
    }

    @Override
    public void aplicarDireccionamiento() {
        app.routes(() -> {
            path("/", () ->{
                before(ctx -> {
                    CarroCompras carroCompras = ctx.sessionAttribute("carroCompras");
                    if(carroCompras == null){
                        ctx.sessionAttribute("carroCompras", new CarroCompras());
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
                    CarroCompras carroCompras = ctx.sessionAttribute("carroCompras");
                    modelo.put("cantidadProdCarrito", carroCompras.getCantidadCarroCompra());
                    ctx.render("/templates/vista/login.html");
                });
            });

            path("/registrar", () -> {
                get(ctx -> {
                    Map<String, Object> modelo = new HashMap<>();
                    CarroCompras carroCompras = ctx.sessionAttribute("carroCompras");

                    modelo.put("cantidadProdCarrito", carroCompras.getCantidadCarroCompra());
                    ctx.render("/templates/vista/registrar.html");
                });
                post(ctx -> {
                    String nombre = ctx.formParam("nombre");
                    String nomUsuario = ctx.formParam("usuario");
                    String pass = ctx.formParam("contrasena");
                    String confirmarPass = ctx.formParam("passwordConfirm");
                    Usuario usuario = new Usuario(nombre, nomUsuario, pass);
                    //boolean registrado = ServiciosUsuario.getInstancia().registrarUsuario(usuario);
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
    }
}
