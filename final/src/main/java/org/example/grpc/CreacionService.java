package org.example.grpc;

import acortadorrn.AcortadorRn;
import acortadorrn.ServicioCreacionGrpc;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.grpc.stub.StreamObserver;
import org.example.encapsulacion.URL;

import java.time.Instant;


public class CreacionService extends ServicioCreacionGrpc.ServicioCreacionImplBase {
    @Override
    public void crearURL(AcortadorRn.CrearURLRequest request, StreamObserver<AcortadorRn.CrearURLResponse> responseObserver) {
//        // Obtener el JSON del cuerpo de la solicitud
//        JsonObject jsonObject = JsonParser.parseString(request.getJson()).getAsJsonObject();
//
//        // Obtener los valores de los campos del JSON
//        String id_usuario = jsonObject.get("id_usuario").getAsString();
//        String url_original = jsonObject.get("url_original").getAsString();
//
//        // Generar la URL acortada
//        String id_url = generarIdURL();
//        String url_acortada = URL_BASE + id_url;
//
//        // Generar la fecha de creación
//        Instant instant = Instant.now();
//        Timestamp fecha_creacion = Timestamp.newBuilder()
//                .setSeconds(instant.getEpochSecond())
//                .setNanos(instant.getNano())
//                .build();
//
//        // Construir el objeto de estadísticas (en este ejemplo se deja vacío)
//        EstadisticasURL estadisticasURL = EstadisticasURL.newBuilder().build();
//
//        // Construir la respuesta con los datos de la nueva URL acortada
//        AcortadorRn.CrearURLResponse response = CrearURLResponse.newBuilder()
//                .setUrlOriginal(url_original)
//                .setUrlAcortada(url_acortada)
//                .setFechaCreacion(fecha_creacion)
//                .setEstadisticas(estadisticasURL)
//                .build();
//
//        // Enviar la respuesta al cliente
//        responseObserver.onNext(response);
//        responseObserver.onCompleted();
    }
}
