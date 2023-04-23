package org.example.grpc;


import acortadorrn.AcortadorRn;
import acortadorrn.ServicioListadoGrpc;
import io.grpc.stub.StreamObserver;

public class ListadoService extends ServicioListadoGrpc.ServicioListadoImplBase {

    @Override
    public void listarURLs(AcortadorRn.ListarURLsRequest request, StreamObserver<AcortadorRn.ListarURLsResponse> responseObserver) {
        // implementa aquí la lógica para listar las URLs y sus estadísticas para un usuario
    }
}
