package org.example.grpc;


import acortadorrn.AcortadorRn;
import acortadorrn.ServicioListadoGrpc;
import com.google.gson.Gson;
import io.grpc.stub.StreamObserver;
import org.example.encapsulacion.Acortador;
import org.example.encapsulacion.Usuario;
import org.example.servicios.ServiciosAcortador;
import org.example.servicios.ServiciosUsuario;

import com.google.protobuf.ByteString;


import java.util.List;

public class ListadoService extends ServicioListadoGrpc.ServicioListadoImplBase {

    @Override
    public void listarURLs(AcortadorRn.ListarURLsRequest request, StreamObserver<AcortadorRn.ListarURLsResponse> responseObserver) {
        String idUsuario = request.getIdUsuario();
        Usuario usuario = ServiciosUsuario.getInstancia().find(idUsuario);

        List<Acortador> listaDeAcortadores = ServiciosAcortador.getInstancia().findAllByUser(usuario);

        // Creamos una instancia de Gson
        Gson gson = new Gson();

        // Convertimos la lista de objetos Java a una cadena JSON utilizando Gson
        String json = gson.toJson(listaDeAcortadores);

        // Creamos la respuesta para enviar al cliente
        AcortadorRn.ListarURLsResponse response = AcortadorRn.ListarURLsResponse.newBuilder()
                .setJson(json)
                .build();

        // Enviamos la respuesta al cliente
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
