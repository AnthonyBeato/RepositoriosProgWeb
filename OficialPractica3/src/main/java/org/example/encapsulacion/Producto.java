package org.example.encapsulacion;


//import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.*;

@Entity
@Table(name = "producto")
public class  Producto {
    @Id
    private String idProducto;
    @ManyToMany(mappedBy = "productos", cascade = CascadeType.ALL)
    private List<VentaProductos> ventas = new ArrayList<>();
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "precio")
    private double precio = 0;
    @Column(name = "cantidad")
    private int cantidad = 0;
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(mappedBy = "producto", fetch = FetchType.LAZY)
    private List<Comentario> comentarios = new ArrayList<>();
    @OneToMany(mappedBy = "producto", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Foto> fotos = new ArrayList<>();

    public Producto(String idProducto, String nombre, double precio, int cantidad, String descripcion, List<Foto> fotos, List<Comentario> comentarios) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.idProducto = idProducto;
        this.descripcion = descripcion;
        this.fotos = fotos;
        this.comentarios = comentarios;
    }

    public Producto() {

    }

    public void update(Producto tempProducto){
        idProducto = tempProducto.getIdProducto();
        nombre = tempProducto.getNombre();
        precio = tempProducto.getPrecio();
        cantidad = tempProducto.getCantidad();
        fotos = tempProducto.getFotos();
    }

//    public void updateCantidad(int nuevaCantidad, int max){
//        if(nuevaCantidad > max){
//            cantidad = max;
//            return;
//        }
//        cantidad += nuevaCantidad;
//    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public List<VentaProductos> getVentas() {
        return ventas;
    }

    public void setVentas(List<VentaProductos> ventas) {
        this.ventas = ventas;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

//    public String getMimeType() {
//        return mimeType;
//    }
//
//    public void setMimeType(String mimeType) {
//        this.mimeType = mimeType;
//    }
//
//    public String getFotoBase64() {
//        return fotoBase64;
//    }
//
//    public void setFotoBase64(String fotoBase64) {
//        this.fotoBase64 = fotoBase64;
//    }

    public List<Foto> getFotos() {
        return fotos;
    }

    public void setFotos(List<Foto> fotos) {
        this.fotos = fotos;
    }

    public List<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return idProducto == producto.idProducto;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + idProducto +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", cantidad= "+ cantidad +
                '}';
    }

    public double getTotal() {
        return precio * cantidad;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProducto);
    }
}
