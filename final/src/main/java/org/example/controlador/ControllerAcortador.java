package org.example.controlador;

import Utilidad.ControllerBase;
import io.javalin.Javalin;
import org.example.servicios.ServiciosAcortador;
import org.example.servicios.ServiciosUsuario;

public class ControllerAcortador extends ControllerBase {

    ServiciosAcortador serviciosAcortador = ServiciosAcortador.getInstancia();

    public ControllerAcortador(Javalin app) {
        super(app);
    }

    @Override
    public void aplicarDireccionamiento() {

    }
}
