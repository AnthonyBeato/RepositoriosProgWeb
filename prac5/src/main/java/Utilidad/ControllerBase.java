package Utilidad;

import io.javalin.Javalin;

public abstract class ControllerBase {

    protected Javalin app;

    public ControllerBase(Javalin app){
        this.app = app;
    }

    abstract public void aplicarDireccionamiento();
}
