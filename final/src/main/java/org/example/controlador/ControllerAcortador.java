package org.example.controlador;

import Utilidad.ControllerBase;
import io.javalin.Javalin;
import org.example.servicios.ServiciosAcortador;
import org.example.servicios.ServiciosUsuario;

import java.util.HashMap;
import java.util.Map;

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

//                   modelo.put("titulo", "Acortador de links");
//                   modelo.put("session", ctx.sessionAttributeMap());
                   ctx.render("/templates/vista/index.html", modelo);

               });
           });
        });
    }
}
