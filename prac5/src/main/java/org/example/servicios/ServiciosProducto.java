package org.example.servicios;

import org.example.encapsulacion.Producto;
import org.example.BaseDatos.GestionBD;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ServiciosProducto extends GestionBD<Producto> {
    private static ServiciosProducto instancia;
    private int ProductPerPage=10;
    //private List<Producto> listaProductos = new ArrayList<>();

    private ServiciosProducto(){
        super(Producto.class);
        EntityManager em = getEntityManager();
        //Listado de productos preinsertados
        //listaProductos.add(new Producto( UUID.randomUUID().toString(),"Ram 8GB Asus",1500, 1, "Tarjeta de 8GB de ram DDR5"));
        //listaProductos.add(new Producto(UUID.randomUUID().toString(),"Laptop Asus Zephyrus M15",5000, 1, "Laptop Asus Zephyrus M15"));
        //listaProductos.add(new Producto(UUID.randomUUID().toString(), "iPhone 15",7000, 1, "iPhone 15 Ultra con 1tb de almacenamiento, pantalla XDR con promotion y 5 cámaras"));

    }

    //Creación de producto
    public Producto createProducto(Producto producto){
        Producto tempProducto = getProductByID(producto.getIdProducto());
        if (tempProducto != null){
            return null;
        }
        System.out.println("Producto registrado :)");
        //listaProductos.add(producto);
        return producto;
    }

    //Modificar producto
    public Producto updateProducto(Producto producto){
        String identificador = producto.getIdProducto();
        Producto tempProducto = getProductByID(identificador);

        if(tempProducto == null){
            System.out.println("Error, el producto que elegiste no es existente");
            return null;
        }
        System.out.println("Se modifico el producto");
        tempProducto.update(producto);
        return producto;

    }

    //Eliminar producto
    public boolean deleteProducto(String identificador){
        Producto tempProducto = getProductByID(identificador);
        if(tempProducto == null){
            System.out.println("Producto no existente");
            return false;
        }
        System.out.println("Producto eliminado :)");
        //listaProductos.remove(tempProducto);
        return true;
    }

    //Actualizar cantidad de productos
//    public void updateCantidadProducto(String identificador, int cantidad){
//        Producto tempProducto = getProductByID(identificador);
//        if(tempProducto == null){
//            System.out.println("Producto no existente :/");
//        }else{
//            System.out.println("Cantidad cambiada con exito!");
//            tempProducto.updateCantidad(cantidad, tempProducto.getCantidad());
//        }
//    }

    public static ServiciosProducto getInstancia() {
        if(instancia == null){
            instancia = new ServiciosProducto();
        }

        return instancia;
    }

    public static void setInstancia(ServiciosProducto instancia) {
        ServiciosProducto.instancia = instancia;
    }

//    public List<Producto> getListaProductos() {
//        return listaProductos;
//    }
    public List<Producto> findAll(){
        EntityManager em = getEntityManager();
        Query query = em.createNativeQuery("select * from Producto ", Producto.class);
        List<Producto> list = query.getResultList();
        return list;
    }

//    public void setListaProductos(List<Producto> listaProductos) {
//        this.listaProductos = listaProductos;
//    }

    public Producto getProductByID(String id){

        for (Producto producto : findAll()){
            if (producto.getIdProducto().equals(id)){
                System.out.println("Llego el producto por ID: "+ producto.getIdProducto());
                return producto;
            }
        }
        return null;
    }
//    public Product findOneById(Long id){
//        EntityManager em = getEntityManager();
//        Query query = em.createQuery("select e from Product e where e.id = :productId");
//        query.setParameter("productId", id);
//        List<Product> list = query.getResultList();
//        return list.get(0);
//    }

//    public Producto getProductByID(String id) {
//        return listaProductos.stream()
//                .filter(producto -> producto.getIdProducto().equals(id))
//                .findFirst()
//                .orElse(null);
//    }

    public Producto getProductByNombre(String nombre){
        for (Producto producto : findAll()){
            if (producto.getNombre().equals(nombre)){
                return producto;
            }
        }
        return null;
    }

    public List<Producto> getPage(int page){
        EntityManager em = getEntityManager();
        Query query = em.createNativeQuery("select * from Producto OFFSET "+page*ProductPerPage+" ROWS FETCH NEXT "+ProductPerPage+" ROWS ONLY", Producto.class);
        List<Producto> list = query.getResultList();
        return list;
    }

    public List<Integer> getPageCount() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < Math.round(getInstancia().findAll().size() / ProductPerPage)+1; i++) {
            list.add(i+1);
        }
        return list;
    }

    public List<Producto> getPageSearch(int page, String name){
        List<Producto> list = getInstancia().findAll();
        return list.stream().filter(e -> e.getNombre().toLowerCase().contains(name)).toList();
    }
}
