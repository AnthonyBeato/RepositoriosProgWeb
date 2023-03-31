package org.example.controlador;

import Utilidad.ControllerBase;
import io.javalin.Javalin;
import org.example.servicios.ServiciosUsuario;

public class ControllerSeguridad extends ControllerBase {

    ServiciosUsuario serviciosUsuario = ServiciosUsuario.getInstancia();

    public ControllerSeguridad(Javalin app) {
        super(app);
    }

    @Override
    public void aplicarDireccionamiento() {

    }
}
