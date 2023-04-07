package org.example.controlador;

import Utilidad.ControllerBase;
import io.javalin.Javalin;
import org.example.encapsulacion.Acortador;
import org.example.encapsulacion.Usuario;
import org.example.servicios.ServiciosAcortador;
import org.example.servicios.ServiciosURL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

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
                    int cantURLSCortas = serviciosAcortador.findAllByUser(usuario).size();
                    int cantURLXPage = 10;
                    int totalPages = (int) Math.ceil((double) cantURLSCortas/cantURLXPage);

                    int indiceIni = (actualPage - 1) * cantURLXPage;
                    int indiceFinal = Math.min(indiceIni + cantURLXPage, cantURLSCortas);

                    modelo.put("titulo", "Lista de URLS");

                    List<Acortador> misURLS = serviciosAcortador.findAllByUser(usuario)
                            .subList(indiceIni, indiceFinal)
                            .stream()
                            .filter(a -> a.getVisits_counter() >= 0)
                            .toList();

                    modelo.put("misUrls", misURLS);
                    modelo.put("actualPage", actualPage);
                    modelo.put("totalPages", totalPages);
                    modelo.put("cantURLXPage", cantURLXPage);
                    modelo.put("cantURLSCortas", cantURLSCortas);

                    modelo.put("session", ctx.sessionAttributeMap());
                    ctx.render("/templates/vista/listadoUrls.html", modelo);
                });
            });

            path("/Seguridad/URL", () ->{
                get("/{idURL}", ctx -> {

                });
            });
        });

    }
}
