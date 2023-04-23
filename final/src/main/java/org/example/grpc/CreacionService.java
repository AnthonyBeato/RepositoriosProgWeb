package org.example.grpc;

import acortadorrn.AcortadorRn;
import acortadorrn.ServicioCreacionGrpc;
import io.grpc.stub.StreamObserver;

public class CreacionService extends ServicioCreacionGrpc.ServicioCreacionImplBase {
    @Override
    public void crearURL(AcortadorRn.CrearURLRequest request, StreamObserver<AcortadorRn.CrearURLResponse> responseObserver) {
        // implementa aquí la lógica para crear una nueva URL y obtener sus estadísticas y vista previa
    }
}
