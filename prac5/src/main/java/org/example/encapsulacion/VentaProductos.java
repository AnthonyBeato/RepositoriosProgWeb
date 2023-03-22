package org.example.encapsulacion;

import java.util.*;

import javax.persistence.*;


@Entity
@Table(name = "ventas")
public class VentaProductos {
    @Id
    private String idVentas;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "venta_producto",
            joinColumns = @JoinColumn(name = "venta_id"),
            inverseJoinColumns = @JoinColumn(name = "producto_id")
    )
    private List<Producto> productos = new ArrayList<>();

    @Column(name = "fecha")
    private Date fechaCompra;

    @Column(name = "nombreCliente")
    private String nombreCliente;



    private double total = 0;

    public VentaProductos(Date fechaCompra, String nombreCliente, List<Producto> productos, double total) {
        this.fechaCompra = fechaCompra;
        this.nombreCliente = nombreCliente;
        this.productos = productos;
        for (Producto producto:
productos             ) {
            System.out.println("Producto: "+ producto.getNombre());
        }
        this.idVentas = UUID.randomUUID().toString();
        for (int i =0; i< productos.size(); i++){
            total += productos.get(i).getTotal();
        }
        this.total = total;
    }

    public VentaProductos() {

    }

    public String getIdVentas() {
        return idVentas;
    }

    public void setIdVentas(String idVentas) {
        this.idVentas = idVentas;
    }

    public Date getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> listaProductos) {
        this.productos = listaProductos;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VentaProductos that = (VentaProductos) o;
        return idVentas == that.idVentas;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idVentas);
    }
}
