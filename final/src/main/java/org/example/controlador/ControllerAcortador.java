package org.example.controlador;

import Utilidad.ControllerBase;
import com.google.zxing.WriterException;
import io.javalin.Javalin;
import org.eclipse.jetty.http.HttpStatus;
import org.example.encapsulacion.Acortador;
import org.example.encapsulacion.URL;
import org.example.servicios.ServiciosAcortador;
import org.example.servicios.ServiciosURL;
import org.example.servicios.ServiciosUsuario;

import io.javalin.http.Context;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.*;

public class ControllerAcortador extends ControllerBase {

    ServiciosAcortador serviciosAcortador = ServiciosAcortador.getInstancia();

    public ControllerAcortador(Javalin app) {
        super(app);
    }

    public void mostrarQR(Context ctx) throws IOException, WriterException {
        String urlAcortada = ctx.pathParam("urlAcortada");
        byte[] codigoQR = generarCodigoQR(urlAcortada);

        ctx.contentType("image/png").result(codigoQR);
    }
    @Override
    public void aplicarDireccionamiento() {
        app.routes(() -> {
           path("/", () -> {
               get("/", ctx -> {
                   Map<String, Object> modelo = new HashMap<>();

                   modelo.put("titulo", "Acortador de links");
                   modelo.put("session", ctx.sessionAttributeMap());
                   ctx.render("/templates/vista/index.html", modelo);
               });

               post("/acortar", ctx -> {
                   String URLOriginal = ctx.formParam("url_original");
                   URL url = new URL(URLOriginal, ctx.sessionAttribute("usuario"));

                   String URLAcortada = ServiciosAcortador.getInstancia().generateURLCorta(URLOriginal);

                   String userAgent = ctx.userAgent();
                   String ipAddress = ctx.ip();
                   LocalDateTime dateTime = LocalDateTime.now();

                   Acortador acortador = new Acortador(URLAcortada, url, dateTime, 0, userAgent, ipAddress);
                   ServiciosURL.getInstancia().crear(url);
                   ServiciosAcortador.getInstancia().crear(acortador);


                   Map<String, Object> modelo = new HashMap<>();
                   modelo.put("titulo", "Acortador de links");
                   modelo.put("url_acortada", URLAcortada);
                   modelo.put("url_original", URLOriginal);
                   modelo.put("session", ctx.sessionAttributeMap());


                   ctx.render("/templates/vista/index.html", modelo);
               });

               get("/{url_acortada}", ctx -> {
                    Acortador acortador = ServiciosAcortador.getInstancia().findByShortUrl(ctx.pathParam("url_acortada"));

                    if(acortador == null){
                        ctx.status(HttpStatus.NOT_FOUND_404).result("Pagina no encontrada");
                    }else{
                        ServiciosAcortador.getInstancia().incrementarContadorVisitas(acortador);
                        ServiciosAcortador.getInstancia().actualizarAcortador(acortador);
                        System.out.println(acortador.getURLOriginal().getURLOriginal());
                        ctx.redirect(acortador.getURLOriginal().getURLOriginal());
                    }
               });
           });
        });
    }
}
