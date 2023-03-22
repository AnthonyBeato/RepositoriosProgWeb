package org.example.controlador;

import Utilidad.ControllerBase;
import io.javalin.Javalin;
import org.example.encapsulacion.CarroCompras;
import org.example.encapsulacion.Producto;
import org.example.servicios.ServiciosProducto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.*;

public class ControllerCarritoCompras extends ControllerBase {

    ServiciosProducto serviciosProducto = ServiciosProducto.getInstancia();

    public ControllerCarritoCompras (Javalin app) {
        super(app);
    }

    @Override
    public void aplicarDireccionamiento() {
        app.routes(() ->{
            path("/carritoCompras",  () ->{
                //Abrir carrito de compras
                get( ctx -> {
                   CarroCompras carroCompras = ctx.sessionAttribute("carroCompras");
                   Map<String, Object> modelo = new HashMap<>();
                   List<Producto> listaProductos = carroCompras.getListaProductos();

                    //System.out.println("Tamanio: "+listaProductos.size());

                    modelo.put("productos", listaProductos);
                    modelo.put("cantidadProdCarrito", carroCompras.getCantidadCarroCompra());
                    modelo.put("titulo", "Carro de compras");
                    modelo.put("total_carrito", carroCompras.getMontoCarroCompra());

                    //Guardar el nombre de Usuario en header
                    modelo.put("session", ctx.sessionAttributeMap());
                    ctx.render("/templates/vista/carritoCompra.html", modelo);
                });

                //Listado de productos por comprar
//                get(ctx -> {
//                    CarroCompras carroCompras = ctx.sessionAttribute("carroCompras");
//                    Map<String, Object> modelo = new HashMap<>();
//                    List<Producto> listaProductos = carroCompras.getListaProductos();
//
//                    modelo.put("titulo", "Lista de productos");
//                    modelo.put("productos", listaProductos);
//                    modelo.put("cantidadProdCarrito", carroCompras.getCantidad());
//
//                    ctx.render("/templates/vista/index.html", modelo);
//                });

                //Crear carrito, y añadir productos al carrito
                post("/{idProducto}", ctx -> {
                    String identificador = ctx.pathParam("idProducto");
                    String nombre = ctx.formParam("nombre");
                    int cantidad = ctx.formParamAsClass("cantidad", Integer.class).get();
                    //System.out.println("Dentro de crear, IDProd: "+ identificador + " cantidad: " + cantidad);
                    //Producto tempProducto = serviciosProducto.getProductByID(identificador);
                    Producto tempProducto = serviciosProducto.find(identificador);

                    //Validar si producto existe en inventario
                    if (tempProducto != null){
                        CarroCompras carroCompras = ctx.sessionAttribute("carroCompras");

                        //Asignación de variable booleana que indicará si existe o no el producto
                        boolean existe = false;

                        //Bucle para buscar si el producto existe dentro de la lista del carrito
                        for (Producto producto: carroCompras.getListaProductos()) {
                            if(identificador.equals(producto.getIdProducto())) {
                                int cantidadNueva = producto.getCantidad() + cantidad;
                                producto.setCantidad(cantidadNueva);
                                existe = true;
                                break;
                            }
                        }

                        //En el caso de que el producto no exista, se asigna la cantidad dada por el input
                        //además, se agrega a la lista de carro de compra.
                        if(existe == false){
                            tempProducto.setCantidad(cantidad);
                            carroCompras.addProducto(tempProducto);
                        }

                        ctx.sessionAttribute("carroCompras", carroCompras);

                    }



                    ctx.redirect("/");
                });

                //Eliminar producto del carrito
                post("/Eliminar/{idProducto}", ctx -> {
                    String identificador = ctx.pathParam("idProducto");
                    CarroCompras carroCompras = ctx.sessionAttribute("carroCompras");
                    carroCompras.deleteProducto(serviciosProducto.getProductByID(identificador));
                    //System.out.println("El id borrado es:"+ serviciosProducto.getProductByID(identificador) );

                    ctx.sessionAttribute("carroCompras", carroCompras);
                    ctx.redirect("/carritoCompras");
                });

                //Limpiar todos los productos del carrito
                post("/LimpiarCarroCompras", ctx -> {
                    System.out.println("Llego a limpiarCarroCompras");
                    CarroCompras carroCompras = ctx.sessionAttribute("carroCompras");
                    carroCompras.limpiarCarrito();
                    //List<Producto> listaProductos = carroCompras.getListaProductos();
                    //carroCompras.limpiarCarrito(listaProductos);
                    //carroCompras.setListaProductos(new ArrayList<>());

                    ctx.sessionAttribute("carroCompras", carroCompras);

                    //Map<String, Object> modelo = new HashMap<>();

                    //System.out.println("Tamanio: "+listaProductos.size());


                    System.out.println("Llego a final del carrito");
                    //ctx.render("/templates/vista/carritoCompra.html", modelo);
                    ctx.redirect("/");
                });
            });
        });
    }
}
