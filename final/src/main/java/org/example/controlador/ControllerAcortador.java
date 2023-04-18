package org.example.controlador;

import Utilidad.ControllerBase;
import io.javalin.Javalin;
import jakarta.servlet.http.HttpSession;
import org.eclipse.jetty.http.HttpStatus;
import org.example.encapsulacion.Acortador;
import org.example.encapsulacion.URL;
import org.example.encapsulacion.Usuario;
import org.example.servicios.ServiciosAcortador;
import org.example.servicios.ServiciosURL;
import org.example.servicios.ServiciosUsuario;

import javax.xml.crypto.Data;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.*;

import static io.javalin.apibuilder.ApiBuilder.*;

public class ControllerAcortador extends ControllerBase {

    ServiciosAcortador serviciosAcortador = ServiciosAcortador.getInstancia();


    public ControllerAcortador(Javalin app) {
        super(app);
    }

    @Override
    public void aplicarDireccionamiento() {
        app.routes(() -> {
            path("/", () -> {
                get("/", ctx -> {
                    Map<String, Object> modelo = new HashMap<>();

                    modelo.put("titulo", "Acortador de links");
                    modelo.put("session", ctx.sessionAttributeMap());
                    boolean seAcorto = false;
                    modelo.put("seAcorto", seAcorto);

                    Usuario usuario = ctx.sessionAttribute("usuario");
                    if(usuario != null){
                        //Mandar listado de url desde fuera de la sesion, para ponerlo en la sesión.
                        HttpSession session = ctx.req().getSession(true);
                        String sessionId = session.getId();
                        System.out.println("           En controllerAcortador id de la sesion es" + sessionId);


                        List<Acortador> lista = serviciosAcortador.cambiarURLSAUsuario(usuario, sessionId);
                        if(lista == null){
                            lista = new ArrayList<>();
                        }
                        for (Acortador acortador: lista) {
                            if(serviciosAcortador.find(acortador.getIdAcortador()) != null){
                                ServiciosURL.getInstancia().editar(acortador.getURLOriginal());
                                serviciosAcortador.editar(acortador);
                            }else{
                                ServiciosURL.getInstancia().crear(acortador.getURLOriginal());
                                serviciosAcortador.crear(acortador);
                            }

                            System.out.println("El link: " + acortador.getURLOriginal() + "se le paso al usuario: " + acortador.getUsuario().getUsuario());
                        }
                        //serviciosAcortador.listaAcortadoresParaNoRegistrados.clear();
                        if(serviciosAcortador.getListaAcortadoresPorSesion().get(sessionId) != null){
                            serviciosAcortador.getListaAcortadoresPorSesion().get(sessionId).clear();
                        }
                    }
                    modelo.put("usuario", usuario);


                    ctx.render("/templates/vista/index.html", modelo);
                });

                post("/acortar", ctx -> {
                    String URLOriginal = ctx.formParam("url_original");
                    Usuario usuario = ctx.sessionAttribute("usuario");
                    URL url = new URL(URLOriginal, usuario);

                    String URLAcortada = "";
                    Acortador temp = serviciosAcortador.findByOriginalUrlAndUser(URLOriginal, usuario);
                    if (temp != null) {
                        URLAcortada = temp.getURLAcortado();
                    } else {
                        URLAcortada = ServiciosAcortador.getInstancia().generateURLCorta(URLOriginal);
                    }

                    String userAgent = ctx.userAgent();
                    String ipAddress = ctx.ip();
                    LocalDateTime dateTime = LocalDateTime.now();

                    Acortador acortador = new Acortador(URLAcortada, url, dateTime, 0, userAgent, ipAddress, usuario);

                    Map<String, List<Acortador>> listaAcortadoresPorSesion = serviciosAcortador.getListaAcortadoresPorSesion();
                    if(usuario == null){
                        HttpSession session = ctx.req().getSession(true);
                        String sessionId = session.getId();

                        List<Acortador> lista = listaAcortadoresPorSesion.get(sessionId);
                        if(lista == null){
                            lista = new ArrayList<>();
                            listaAcortadoresPorSesion.put(sessionId, lista);
                        }

                        boolean encontrado = false;
                        for (Acortador aux: lista) {
                            if(Objects.equals(aux.getURLOriginal().getURLOriginal(), acortador.getURLOriginal().getURLOriginal())){
                                encontrado = true;
                                break;
                            }
                        }
                        if (!encontrado) {
                            lista.add(acortador);
                            ServiciosURL.getInstancia().crear(url);
                            serviciosAcortador.crear(acortador);
                        }
                    }else{
                        boolean encontrado = false;
                        for (Acortador aux: serviciosAcortador.findAllByUser(usuario)) {
                            if(Objects.equals(aux.getURLOriginal().getURLOriginal(), acortador.getURLOriginal().getURLOriginal())){
                                encontrado = true;
                                break;
                            }
                        }
                        if (!encontrado) {
                            ServiciosURL.getInstancia().crear(url);
                            ServiciosAcortador.getInstancia().crear(acortador);
                        }
                    }

                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("titulo", "Acortador de links");
                    modelo.put("url_acortada", URLAcortada);
                    modelo.put("url_original", URLOriginal);
                    modelo.put("session", ctx.sessionAttributeMap());

                    boolean seAcorto = true;
                    modelo.put("seAcorto", seAcorto);

                    ctx.render("/templates/vista/index.html", modelo);
                });

                get("/{url_acortada}", ctx -> {
                    Usuario usuario = ctx.sessionAttribute("usuario");
                    Acortador acortador = new Acortador();
                    if(usuario == null){
                        //acortador = ServiciosAcortador.getInstancia().findByShortUrlNoRegistrados(ctx.pathParam("url_acortada"));
                        acortador = serviciosAcortador.findByShortUrl(ctx.pathParam("url_acortada"));

                    }else{
                        acortador = ServiciosAcortador.getInstancia().findByShortUrl(ctx.pathParam("url_acortada"));
                    }
                    String userAgent = ctx.userAgent();
                    String ipAddress = ctx.ip();
                    LocalDateTime dateTime = LocalDateTime.now();

                    if(acortador == null){
                        ctx.status(HttpStatus.NOT_FOUND_404).result("Pagina no encontrada");
                    }else{
                        ServiciosAcortador.getInstancia().incrementarContadorVisitas(acortador);
                        acortador.agregarAgenteUsuario(userAgent);
                        acortador.agregarDireccionIP(ipAddress);
                        acortador.agregarFechaAcceso(dateTime);
                        //if(usuario != null){
                            serviciosAcortador.editar(acortador);
                        //}
                        System.out.println("   agente del usuario: "  + userAgent);
                        System.out.println("     ip: " + ipAddress);
                        System.out.println("    fecha: " + dateTime);

                        System.out.println(acortador.getURLOriginal().getURLOriginal());
                        ctx.redirect(acortador.getURLOriginal().getURLOriginal());
                    }
                });

            });
        });
    }
}