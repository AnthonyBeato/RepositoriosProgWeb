package org.example.grpc;

import acortadorrn.AcortadorRn;
import acortadorrn.ServicioCreacionGrpc;
import acortadorrn.ServicioListadoGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;

public class AcortadorServidor {

    private Server servidor;

    public void iniciarServidor(int puerto) throws IOException {
        servidor = ServerBuilder.forPort(puerto)
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
        servidor.start();
        System.out.println("Servidor iniciado en el puerto " + puerto);
    }

    public void detenerServidor() {
        if (servidor != null) {
            servidor.shutdown();
            System.out.println("Servidor detenido");
        }
    }

    public void esperarTerminacion() throws InterruptedException {
        if (servidor != null) {
            servidor.awaitTermination();
        }
    }

}
