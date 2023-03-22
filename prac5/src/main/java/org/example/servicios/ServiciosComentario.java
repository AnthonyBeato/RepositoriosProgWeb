package org.example.servicios;

import org.example.BaseDatos.GestionBD;
import org.example.encapsulacion.Comentario;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.xml.stream.events.Comment;
import java.util.List;

public class ServiciosComentario extends GestionBD<Comentario> {

    private static ServiciosComentario instance;

    private ServiciosComentario(){
      super(Comentario.class);
      EntityManager em = getEntityManager();
    }

    public static ServiciosComentario getInstancia(){
        if(instance==null){
            instance = new ServiciosComentario();
        }
        return instance;
    }

    public List<Comentario> obtenerComentariosPorProducto(String idProducto) {
        EntityManager em = getEntityManager();
        Query query = em.createNativeQuery("SELECT * FROM Comentario c WHERE c.producto_id = :idProducto", Comentario.class);
        query.setParameter("idProducto", idProducto);
        List<Comentario> list = query.getResultList();
        em.close();

        return list;
    }

}
