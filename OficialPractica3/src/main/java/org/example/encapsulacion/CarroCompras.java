package org.example.encapsulacion;

//import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;

//@Entity
//@Table(name = "carroCompras")
public class CarroCompras {
//    @Id
    private String idCarroCompra;
//    @OneToMany(mappedBy = "carroCompra",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Producto> listaProductos = new ArrayList<>();
//    @Column(nullable = false)
    private Boolean vendido = false;

    public CarroCompras() {}

    public CarroCompras(String id, List<Producto> listaProductos, Boolean vendido) {
        this.listaProductos = listaProductos;
        this.idCarroCompra = id;
        this.vendido = vendido;
    }

    public Boolean getVendido() {
        return vendido;
    }

    public void setVendido(Boolean estado) {
        this.vendido = estado;
    }

    public String getIdCarroCompra() {
        return idCarroCompra;
    }

    public void setIdCarroCompra(String idCarroCompra) {
        this.idCarroCompra = idCarroCompra;
    }

    public List<Producto> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(List<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }

    public void addProducto(Producto producto){
        listaProductos.add(producto);
    }

    public void deleteProducto(Producto producto){
        listaProductos.remove(producto);
    }

    public void limpiarCarrito(){
        listaProductos.clear();
    }

    public double getMontoCarroCompra(){
        double total = 0;
        double precio = 0;
        int cantidad = 0;

        for (int i = 0; i < listaProductos.size(); i++){
            precio = listaProductos.get(i).getPrecio();
            cantidad = listaProductos.get(i).getCantidad();
            total += precio * cantidad;
        }

        return total;
    }

    public double getTotalCarroCompra(){
        double total = 0;
        for(int i = 0; i< listaProductos.size(); i++){
            total += listaProductos.get(i).getPrecio();
        }
        return total;
    }

    public double getTotalIndividualProducto(Producto producto){
        double totalIndividual = 0;



        return totalIndividual;
    }

    public int getCantidadCarroCompra(){
        int cantidad = 0;
        for (int i =0; i< listaProductos.size(); i++){
            cantidad += listaProductos.get(i).getCantidad();
        }
        return cantidad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarroCompras that = (CarroCompras) o;
        return idCarroCompra == that.idCarroCompra;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCarroCompra);
    }
}
