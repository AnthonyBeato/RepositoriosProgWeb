package org.example.controlador;

import Utilidad.ControllerBase;
import io.javalin.Javalin;
import org.example.servicios.ServiciosAcortador;
import org.example.servicios.ServiciosURL;

public class ControllerURL extends ControllerBase {

    ServiciosURL serviciosURL = ServiciosURL.getInstancia();

    public ControllerURL(Javalin app) {
        super(app);
    }

    @Override
    public void aplicarDireccionamiento() {

    }
}
