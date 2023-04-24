package org.example.grpc;

import acortadorrn.AcortadorRn;
import acortadorrn.ServicioCreacionGrpc;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.google.protobuf.Timestamp;
import io.grpc.stub.StreamObserver;
import org.checkerframework.checker.units.qual.A;
import org.example.encapsulacion.Acortador;
import org.example.encapsulacion.URL;
import org.example.encapsulacion.Usuario;
import org.example.servicios.ServiciosAcortador;
import org.example.servicios.ServiciosURL;
import org.example.servicios.ServiciosUsuario;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.AbstractCollection;
import java.util.List;
import java.util.UUID;


public class CreacionService extends ServicioCreacionGrpc.ServicioCreacionImplBase {
    @Override
    public void crearURL(AcortadorRn.CrearURLRequest request, StreamObserver<AcortadorRn.CrearURLResponse> responseObserver) {
        // Obtener el JSON del cuerpo de la solicitud
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(request.getJson(), JsonObject.class);
        LocalDateTime dateTime = LocalDateTime.now();

        // Obtener los valores de los campos del JSON
        String id_usuario = jsonObject.get("id_usuario").getAsString();
        String url_original = jsonObject.get("url_original").getAsString();
        String fecha_creacion = dateTime.toString();
        String imagen_previa = jsonObject.get("imagen_previa").getAsString();

        // Generar la URL acortada
        String url_acortada = ServiciosAcortador.getInstancia().generateURLCorta(url_original);

        // Construir el objeto de estadísticas (en este ejemplo se deja vacío)
        AcortadorRn.Estadisticas estadisticasURL = AcortadorRn.Estadisticas.newBuilder().build();

        Usuario usuario = new Usuario("invitado", "invitado", "invitado", false);
        usuario.setIdUsuario("invitado");
        ServiciosUsuario.getInstancia().crear(usuario);





        // Creamos un objeto URL con los datos de la solicitud y la imagen en base64
        URL nuevaURL = new URL(url_original, usuario, imagen_previa);
        Acortador nuevoAcortador = new Acortador(url_acortada, nuevaURL, dateTime, 0, estadisticasURL.getUltimoAgente(), estadisticasURL.getUltimaDirIp(), usuario);

        // Guardamos la URL en la base de datos
        ServiciosURL.getInstancia().crear(nuevaURL);
        ServiciosAcortador.getInstancia().crear(nuevoAcortador);


        // Construir la respuesta con los datos de la nueva URL acortada
        AcortadorRn.CrearURLResponse response = AcortadorRn.CrearURLResponse.newBuilder()
                .setIdUsuario(id_usuario)
                .setUrlCompleta(url_original)
                .setUrlAcortada(url_acortada)
                .setFechaCreacion(dateTime.toString())
                .setEstadisticas(estadisticasURL)
                .build();


        // Enviar la respuesta al cliente
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
