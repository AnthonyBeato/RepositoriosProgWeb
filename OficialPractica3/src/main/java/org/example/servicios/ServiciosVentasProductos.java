package org.example.servicios;

import org.example.BaseDatos.GestionBD;
import org.example.encapsulacion.VentaProductos;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class ServiciosVentasProductos extends GestionBD<VentaProductos> {
    private static ServiciosVentasProductos instancia;
    private static ServiciosProducto serviciosProducto = ServiciosProducto.getInstancia();
    //private List<VentaProductos> listVentas = new ArrayList<>();

    public ServiciosVentasProductos() {
        super(VentaProductos.class);
        EntityManager em = getEntityManager();
    }

    //Creacion de venta
//    public VentaProductos createVentaProducto(VentaProductos ventaProductos){
//        //VentaProductos tempVenta = getVentaProductotByID(ventaProductos.getIdVentas());
//        if(tempVenta != null){
//            System.out.println("Venta registrada");
//            return null;
//        }
//        System.out.println("Venta registrada ");
//        //listVentas.add(ventaProductos);
////        for (int i = 0; i < ventaProductos.getListaProductos().size(); i++){
////            serviciosProducto.updateCantidadProducto(ventaProductos.getListaProductos().get(i).getIdProducto(), ventaProductos.getListaProductos().get(i).getCantidad() * -1);
////        }
//
//        return ventaProductos;
//    }

    public static ServiciosVentasProductos getInstancia() {
        if(instancia == null){
            instancia = new ServiciosVentasProductos();
        }

        return instancia;
    }

    public static void setInstancia(ServiciosVentasProductos instancia) {
        ServiciosVentasProductos.instancia = instancia;
    }

//    public List<VentaProductos> getListVentas() {
//        return listVentas;
//    }
//
//    public void setListVentas(List<VentaProductos> listVentas) {
//        this.listVentas = listVentas;
//    }

//    public VentaProductos getVentaProductotByID(String id){
//        for (VentaProductos venta : listVentas){
//            if (venta.getIdVentas().equals(id)){
//                return venta;
//            }
//        }
//        return null;
//
//    }

    public List<VentaProductos> obtenerVentas() {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT v FROM VentaProductos v", VentaProductos.class);
        List<VentaProductos> list = query.getResultList();
        return list;
    }

    public VentaProductos obtenerVentaPorId(String idVentas) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT v FROM VentaProductos v JOIN FETCH v.listaProductos WHERE v.id = :idVentas", VentaProductos.class);
        query.setParameter("idVentas", idVentas);
        VentaProductos ventaProductos = (VentaProductos) query.getSingleResult();
        return ventaProductos;
    }
}
