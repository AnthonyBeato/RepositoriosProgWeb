package org.example.controlador;

import Utilidad.ControllerBase;
import org.example.encapsulacion.Acortador;
import org.example.encapsulacion.URL;
import org.example.encapsulacion.Usuario;
import org.example.servicios.ServiciosAcortador;
import org.example.servicios.ServiciosUsuario;
import io.javalin.Javalin;
import org.imgscalr.Scalr;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static io.javalin.apibuilder.ApiBuilder.*;

public class ControllerRest extends ControllerBase {

    public ControllerRest(Javalin app) {super(app);}
    @Override
    public void aplicarDireccionamiento() {
            app.routes(() -> {
                path("/api", () -> {
                    get("/urls/{username}", ctx -> {
                        String username = ctx.pathParam("username");
                        Usuario usuario = ServiciosUsuario.getInstancia().getUsuariotByUser(username);

                        if (usuario == null) {
                            ctx.status(404).result("Usuario no encontrado");
                            return;
                        }

                        List<Acortador> urls = ServiciosAcortador.getInstancia().findAllByUser(usuario);
                        ctx.json(urls);
                    });

                    post("/urls", ctx -> {
                        // 1. Extraer los parámetros del formulario o JSON (nombre de usuario, URL original, etc.)
                        String username = ctx.formParam("username");
                        String URLOriginal = ctx.formParam("url");

                        // 2. Crear un nuevo objeto Acortador y guardar en la base de datos
                        Usuario usuario = ServiciosUsuario.getInstancia().getUsuariotByUser(username);
                        if (usuario == null) {
                            ctx.status(404).result("Usuario no encontrado");
                            return;
                        }

                        Acortador acortador = new Acortador(URLOriginal, usuario);
                        ServiciosAcortador.getInstancia().crear(acortador);

                        // 3. Obtener una vista previa de la imagen y codificarla en base64
                        String imagenBase64 = "";
                        try {
                           imagenBase64 = obtenerImagenBase64(URLOriginal);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        // 4. Devolver la estructura básica como respuesta JSON
                        Map<String, Object> result = new HashMap<>();
                        result.put("url_completa", acortador.getURLOriginal());
                        result.put("url_acortada", acortador.getURLAcortado());
                        result.put("fecha_creacion", acortador.getCreated_date_time());
                        result.put("visitas", acortador.getVisits_counter());
                        result.put("ult_acceso", acortador.getFechasAcceso().isEmpty() ? null : acortador.getFechasAcceso().get(acortador.getFechasAcceso().size() - 1));
                        result.put("imagen_base64", imagenBase64);

                        ctx.json(result);
                    });

                });
            });
        }
    private String obtenerImagenBase64(String urlOriginal) throws IOException {
        String imagenBase64 = "";
        try {
            Document doc = Jsoup.connect(urlOriginal).get();
            Element imgElement = doc.select("img").first();
            if (imgElement != null) {
                String imgSrc = imgElement.absUrl("src");
                if (!imgSrc.isEmpty()) {
                    BufferedImage image;
                    java.net.URL url = new java.net.URL(imgSrc);
                    try (InputStream is = url.openStream()) {
                        image = ImageIO.read(is);
                    }
                    BufferedImage thumbnail = Scalr.resize(image, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_WIDTH, 100, Scalr.OP_ANTIALIAS);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(thumbnail, "jpg", baos);
                    byte[] imageBytes = baos.toByteArray();
                    imagenBase64 = Base64.getEncoder().encodeToString(imageBytes);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imagenBase64;
    }

}