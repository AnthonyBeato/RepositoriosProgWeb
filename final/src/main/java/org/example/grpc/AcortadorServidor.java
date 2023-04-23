package org.example.grpc;

import acortadorrn.AcortadorRn;
import acortadorrn.ServicioCreacionGrpc;
import acortadorrn.ServicioListadoGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;

public class AcortadorServidor {

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(9090)
                .addService(new ServicioListadoGrpc.ServicioListadoImplBase() {
                    @Override
                    public void listarURLs(AcortadorRn.ListarURLsRequest request, StreamObserver<AcortadorRn.ListarURLsResponse> responseObserver) {
                        super.listarURLs(request, responseObserver);
                    }
                })
                .addService(new ServicioCreacionGrpc.ServicioCreacionImplBase() {
                    @Override
                    public void crearURL(AcortadorRn.CrearURLRequest request, StreamObserver<AcortadorRn.CrearURLResponse> responseObserver) {
                        super.crearURL(request, responseObserver);
                    }
                })
                .build();

        server.start();
        System.out.println("Servidor iniciado en el puerto " + server.getPort());

        server.awaitTermination();
    }

}
