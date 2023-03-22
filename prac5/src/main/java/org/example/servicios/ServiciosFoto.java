package org.example.servicios;

import org.example.encapsulacion.Foto;
import org.example.encapsulacion.Producto;
import org.example.BaseDatos.GestionBD;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class ServiciosFoto extends GestionBD<Foto> {

    private static ServiciosFoto instancia;


    //private List<Foto> listaFotos = new ArrayList<>();

    public ServiciosFoto() {
        super(Foto.class);
        EntityManager em = getEntityManager();

    }

    public static ServiciosFoto getInstancia(){
        if(instancia==null){
            instancia = new ServiciosFoto();
        }
        return instancia;
    }

//    public List<Foto> getFotosByProducto(Producto producto){
//        EntityManager em = getEntityManager();
//        Query query = em.createQuery("select e from Foto e where e.id = :photoId");
//
//
//        for (Foto foto : producto.getFotos()){
//            if (foto.getProducto().getIdProducto().equals(producto.getIdProducto())){
//                //System.out.println("Llego el producto por ID: "+ producto.getIdProducto());
//                listaFotos.add(foto);
//            }
//        }
//        return listaFotos;
//    }

    public Foto findOneById(String id){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select e from Foto e where e.id = :idFoto");
        query.setParameter("idFoto", id);
        List<Foto> list = query.getResultList();
        return list.get(0);
    }


    public List<Foto> findAll(){
        EntityManager em = getEntityManager();
        Query query = em.createNativeQuery("select * from Foto", Foto.class);
        List<Foto> list = query.getResultList();
        return list;
    }

    public List<Foto> getFotosByProducto(String id){
        EntityManager em = getEntityManager();
        Query query = em.createNativeQuery("select * from Foto where producto_id = idfoto", Foto.class);
        List<Foto> list = query.getResultList();
        return list;
    }





//    public void eliminarFoto(Foto foto) {
////        repositorioFotos.eliminar(foto.getIdFoto());
//    }

}
