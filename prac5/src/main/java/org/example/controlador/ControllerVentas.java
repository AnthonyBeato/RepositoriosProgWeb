package org.example.controlador;

import Utilidad.ControllerBase;
import io.javalin.Javalin;
import org.example.encapsulacion.CarroCompras;
import org.example.encapsulacion.Usuario;
import org.example.encapsulacion.VentaProductos;
import org.example.servicios.ServiciosVentasProductos;
import  org.example.BaseDatos.BDCockroach;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    }
}
