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
                .addService(new ListadoService())
                .addService(new CreacionService())
                .build();

        server.start();
        System.out.println("Servidor iniciado en el puerto " + server.getPort());

        ClienteGRPC.main(new String[] {null});
        server.awaitTermination();
    }

}
