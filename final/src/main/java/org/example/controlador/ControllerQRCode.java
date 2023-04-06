package org.example.controlador;

import io.javalin.Javalin;
import org.example.servicios.ServiciosQRCode;
import Utilidad.ControllerBase;

public class ControllerQRCode extends ControllerBase {

    public ControllerQRCode(Javalin app) {
        super(app);
    }

    @Override
    public void aplicarDireccionamiento() {
        app.get("/qr-code/{url_acortada}", ctx -> {
            String shortUrl = ctx.pathParam("url_acortada");
            new ServiciosQRCode().generateQRCode(ctx, shortUrl);
        });
    }
}
