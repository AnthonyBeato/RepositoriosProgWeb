package org.example.grpc;


import acortadorrn.AcortadorRn;
import acortadorrn.ServicioListadoGrpc;
import com.google.gson.Gson;
import com.google.protobuf.Timestamp;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.example.encapsulacion.Acortador;
import org.example.encapsulacion.Usuario;
import org.example.servicios.ServiciosAcortador;
import org.example.servicios.ServiciosUsuario;

import com.google.protobuf.ByteString;


import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class ListadoService extends ServicioListadoGrpc.ServicioListadoImplBase {

    @Override
    public void listarURLs(AcortadorRn.ListarURLsRequest request, StreamObserver<AcortadorRn.ListarURLsResponse> responseObserver) {
        System.out.println(" Llego a listarURLs");
        try {

            String idUsuario = request.getIdUsuario();
            Usuario usuario = ServiciosUsuario.getInstancia().find(idUsuario);
            System.out.println(" En listarULs el usuario es: " + idUsuario);

            List<Acortador> listaDeAcortadores = ServiciosAcortador.getInstancia().findAllByUser(usuario);

            LocalDateTime dateTime = LocalDateTime.now();

            List<AcortadorRn.URL> listaDeUrls = new ArrayList<>();


            if(!listaDeAcortadores.isEmpty()){
                listaDeUrls = listaDeAcortadores.stream()
                        .map(acortador -> {
                            String ultimaDirIp = acortador.getDireccionesIP().isEmpty() ? "" : acortador.getDireccionesIP().get(acortador.getDireccionesIP().size() - 1);
                            String ultimoAgente = acortador.getAgentesUsuario().isEmpty() ? "" : acortador.getAgentesUsuario().get(acortador.getAgentesUsuario().size() - 1);
                            AcortadorRn.URL.Builder builder = AcortadorRn.URL.newBuilder()
                                    .setIdUsuario(acortador.getUsuario().getIdUsuario())
                                    .setUrlCompleta(acortador.getURLOriginal().getURLOriginal())
                                    .setUrlAcortada(acortador.getURLAcortado())
                                    .setFechaCreacion(acortador.getCreated_date_time().toString())
                                    .setEstadisticas(AcortadorRn.Estadisticas.newBuilder()
                                            .setNumVisitas(acortador.getVisits_counter())
                                            .setUltimaVisita(dateTime.toString())
                                            .setUltimaDirIp(ultimaDirIp)
                                            .setUltimoAgente(ultimoAgente)
                                            .build());
                            return builder.build();
                        })
                        .toList();
            }


            // Creamos una instancia de Gson
            Gson gson = new Gson();

            // Convertimos la lista de objetos Java a una cadena JSON utilizando Gson
            String json = gson.toJson(listaDeUrls);



            // Creamos la respuesta para enviar al cliente
            AcortadorRn.ListarURLsResponse response = AcortadorRn.ListarURLsResponse.newBuilder()
                    .setJson(json)
                    .build();

            // Enviamos la respuesta al cliente
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (StatusRuntimeException e) {
            System.out.println("Error al llamar al servidor: " + e.getStatus());
        }
    }
}
