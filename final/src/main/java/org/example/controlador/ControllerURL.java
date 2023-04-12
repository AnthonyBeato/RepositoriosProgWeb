package org.example.controlador;

import Utilidad.ControllerBase;
import io.javalin.Javalin;
import org.example.encapsulacion.Acortador;
import org.example.encapsulacion.URL;
import org.example.encapsulacion.Usuario;
import org.example.servicios.ServiciosAcortador;
import org.example.servicios.ServiciosURL;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.*;

public class ControllerURL extends ControllerBase {

    ServiciosAcortador serviciosAcortador = ServiciosAcortador.getInstancia();

    public ControllerURL(Javalin app) {
        super(app);
    }

    @Override
    public void aplicarDireccionamiento() {
        app.routes(() -> {
            path("/URL", () -> {
                get("/misUrls", ctx -> {
                    Map<String, Object> modelo = new HashMap<>();
                    Usuario usuario = ctx.sessionAttribute("usuario");

                    int actualPage = 1;
                    String page = ctx.req().getParameter("page");
                    if(page != null){
                        try{
                            actualPage = Integer.parseInt(page);
                        }catch (NumberFormatException e){
                            ctx.redirect("/");
                        }
                    }

                    int cantURLSCortas = 0;
                    int cantURLXPage = 10;
                    int totalPages = 0;
                    int indiceIni = (actualPage - 1) * cantURLXPage;
                    int indiceFinal = 0;
                    List<Acortador> misURLS = new ArrayList<>();

                    if (usuario == null){
                        cantURLSCortas = serviciosAcortador.listaAcortadoresParaNoRegistrados.size();
                        totalPages = (int) Math.ceil((double) cantURLSCortas/cantURLXPage);
                        indiceFinal = Math.min(indiceIni + cantURLXPage, cantURLSCortas);

                        misURLS = serviciosAcortador.listaAcortadoresParaNoRegistrados
                                .subList(indiceIni, indiceFinal)
                                .stream()
                                .filter(a -> a.getVisits_counter() >= 0)
                                .toList();
                    }else{
                        cantURLSCortas = serviciosAcortador.findAllByUser(usuario).size();
                        totalPages = (int) Math.ceil((double) cantURLSCortas/cantURLXPage);
                        indiceFinal = Math.min(indiceIni + cantURLXPage, cantURLSCortas);

                        misURLS = serviciosAcortador.findAllByUser(usuario)
                                .subList(indiceIni, indiceFinal)
                                .stream()
                                .filter(a -> a.getVisits_counter() >= 0)
                                .toList();
                    }



                    modelo.put("titulo", "Lista de URLS");
                    modelo.put("misUrls", misURLS);
                    modelo.put("actualPage", actualPage);
                    modelo.put("totalPages", totalPages);
                    modelo.put("cantURLXPage", cantURLXPage);
                    modelo.put("cantURLSCortas", cantURLSCortas);
                    modelo.put("usuario",usuario);

                    modelo.put("session", ctx.sessionAttributeMap());
                    ctx.render("/templates/vista/listadoUrls.html", modelo);
                });
            });

            path("/Seguridad/URL", () ->{
                get("/detalle/{idAcortador}", ctx -> {
                    Map<String, Object> modelo = new HashMap<>();
                    if((serviciosAcortador.find(ctx.pathParam("idAcortador")) != null)){
                        Acortador urlAcortada = serviciosAcortador.find(ctx.pathParam("idAcortador"));
                        modelo.put("idAcortador", urlAcortada.getIdAcortador());
                        modelo.put("url_original", urlAcortada.getURLOriginal());
                        modelo.put("url_acortado", urlAcortada.getURLAcortado());
                    }


                    // Crear lista de etiquetas y datos para el grafico de barras
                    List<Acortador> acortadores = serviciosAcortador.findAll();
                    List<String> labels = new ArrayList<>();
                    List<Integer> data = new ArrayList<>();
                    List<String> ips = new ArrayList<>();
                    List<String> agentes = new ArrayList<>();

                    for (Acortador acortador : acortadores) {
                        List<LocalDateTime> fechasAcceso = acortador.getFechasAcceso();
                        List<String> direccionIP = acortador.getDireccionesIP();
                        List<String> agenteUsuario = acortador.getAgentesUsuario();

                        for (int i = 0; i < fechasAcceso.size(); i++) {
                            LocalDateTime fecha = fechasAcceso.get(i);
                            String label = fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                            if (!labels.contains(label)) {
                                labels.add(label);
                                data.add(1);
                                ips.add(direccionIP.get(i));
                                agentes.add(agenteUsuario.get(i));
                            } else {
                                int index = labels.indexOf(label);
                                data.set(index, data.get(index) + 1);
                            }
                        }
                    }

                    modelo.put("acortadores", acortadores);
                    modelo.put("labels", labels);
                    modelo.put("data", data);
                    modelo.put("ips", ips);
                    modelo.put("agentes", agentes);

                    //Guardar el nombre de Usuario en header
                    modelo.put("session", ctx.sessionAttributeMap());
                    ctx.render("/templates/vista/detalleURL.html", modelo);
                });
            });

            path("/Seguridad/URL/Administrar", () -> {
                get("/", ctx -> {
                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("titulo", "Administrador de URLs");
                    List<Acortador> lista = serviciosAcortador.findAll();
                    modelo.put("acortadas", lista);
                    modelo.put("session", ctx.sessionAttributeMap());
                    Usuario usuario = ctx.sessionAttribute("usuario");
                    modelo.put("usuario", usuario);



                    ctx.render("/templates/vista/gestionUrls.html", modelo);
                });

                post("/Eliminar/{idAcortador}", ctx -> {
                    String identificador = ctx.pathParam("idAcortador");

                    //Eliminar de la BD
                    Acortador acortado = serviciosAcortador.find(identificador);

                    Acortador temp = acortado;
                    serviciosAcortador.eliminar(identificador);
                    ServiciosURL.getInstancia().eliminar(temp.getURLOriginal().getIdURL());
                    System.out.println("          Se elimino :" + identificador);

                    ctx.redirect("/URL/misUrls");
                });
            });
        });

    }
}
