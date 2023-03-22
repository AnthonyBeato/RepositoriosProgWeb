package org.example.controlador;

import Utilidad.ControllerBase;
import org.example.encapsulacion.Comentario;
import org.example.encapsulacion.Producto;
import org.example.servicios.ServiciosComentario;
import org.example.servicios.ServiciosProducto;
import io.javalin.Javalin;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

public class ControladorComentario extends ControllerBase{

    ServiciosComentario serviciosComentario = ServiciosComentario.getInstancia();
    ServiciosProducto serviciosProducto = ServiciosProducto.getInstancia();

    public ControladorComentario(Javalin app){super(app);}
    @Override
    public void aplicarDireccionamiento() {
//        app.routes(() -> {
//            path("/comentario/", () -> {
//                post("/{id}/", ctx -> {
//                    String comentario = ctx.formParam("comentario");
//                    //long id = ctx.pathParamAsClass("id" , long.class).get();
//                    String id = ctx.pathParam("idProducto");
//                    Producto producto = serviciosProducto.find(id);
//                    serviciosComentario.crear(new Comentario(comentario, producto));
//                    //ctx.redirect("/product/"+ctx.pathParamAsClass("id", long.class).get());
//                    ctx.redirect("/producto/" + ctx.pathParam("idProducto"));
//                });
//            });
//            path("/Seguridad/comentario/", () -> {
//                post("/{comentario}/", ctx -> {
//                    int id = ctx.pathParamAsClass("comentario" , Integer.class).get();
//                    Comentario comentario = serviciosComentario.find(id);
//                    serviciosComentario.eliminar(id);
//                    System.out.println(id);
//                    ctx.redirect("/Seguridad/Productos/Modificar/"+comentario.getProducto().getIdProducto());
//                });
//            });
//        });
    }
}
