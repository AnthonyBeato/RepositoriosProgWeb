package org.example.controlador;

import Utilidad.ControllerBase;
import io.javalin.Javalin;
import org.example.encapsulacion.CarroCompras;
import org.example.encapsulacion.Producto;
import org.example.encapsulacion.Usuario;
import org.example.encapsulacion.VentaProductos;
import org.example.servicios.ServiciosVentasProductos;
import  org.example.BaseDatos.BDCockroach;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.WebContext;


import java.util.*;

import static io.javalin.apibuilder.ApiBuilder.*;

public class ControllerVentas extends ControllerBase {

    ServiciosVentasProductos serviciosVentasProductos = ServiciosVentasProductos.getInstancia();

    public ControllerVentas (Javalin app){
        super (app);
    }
    @Override
    public void aplicarDireccionamiento() {
        app.routes(() ->{
            path("/Seguridad/realizarVenta", () ->{
                post(ctx -> {
                    Usuario usuario = ctx.sessionAttribute("usuario");
                    CarroCompras carroCompras = ctx.sessionAttribute("carroCompras");

                    if(ctx.sessionAttribute("usuario") != null){
                        java.util.Date utilDate = new java.util.Date();
                        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                        VentaProductos ventaProductos = new VentaProductos(sqlDate, usuario.getNombre().toString(), carroCompras.getListaProductos(), 0);
                        //serviciosVentasProductos.createVentaProducto(ventaProductos);
                        serviciosVentasProductos.crearVenta(ventaProductos);
                        BDCockroach.getInstance().insertarVenta(ventaProductos);
                        ctx.sessionAttribute("carroCompras", new CarroCompras());
                        carroCompras.setVendido(true); //Confirmar que el carrito fue vendido
                        System.out.println(ventaProductos.getNombreCliente());
//                        for (int i = 0; i < serviciosVentasProductos.obtenerVentaPorId(); i++) {
//                            //System.out.println(ventaProductos.fin().get(i).getNombre());
//                        }
                        ctx.redirect("/");
                    } else {
                        ctx.redirect("/error");
                    }
                });
            });

            get("/Ventas", ctx -> {
                //List<VentaProductos> ventas = serviciosVentasProductos.getListVentas();
                List<VentaProductos> ventas = serviciosVentasProductos.obtenerVentas();

                Map<String, Object> modelo = new HashMap<>();
                modelo.put("ventas", ventas);


                Usuario usuarioActual = ctx.sessionAttribute("usuario");

                if(usuarioActual != null && usuarioActual.getUsuario().equals("admin")){
                    ctx.render("/templates/vista/ventas.html", modelo);
                }else{
                    ctx.render("/templates/vista/error.html");
                }
            });
        });

        app.routes(() -> {
            path("/", () -> {
                get("/dashboard", ctx -> {
                    List<VentaProductos> ventas = serviciosVentasProductos.obtenerVentas();

                    // Crear lista de etiquetas y datos para el piechart
                    List<String> labels = new ArrayList<>();
                    List<Integer> data = new ArrayList<>();
                    for (VentaProductos venta : ventas) {
                        for (Producto producto : venta.getProductos()) {
                            if (!labels.contains(producto.getNombre())) {
                                labels.add(producto.getNombre());
                                data.add(1);
                            } else {
                                int index = labels.indexOf(producto.getNombre());
                                data.set(index, data.get(index) + 1);
                            }
                        }
                    }

                    // Agregar los datos al objeto Context
//                    Context thymeleafContext = new Context();
//                    thymeleafContext.setVariable("labels", labels);
//                    thymeleafContext.setVariable("data", data);
                    Map<String, Object> modelo = new HashMap<>();

                    modelo.put("labels", labels);
                    modelo.put("data", data);
                    ctx.render("/templates/vista/dashboard.html", modelo);
                });
            });
        });

    }
}
