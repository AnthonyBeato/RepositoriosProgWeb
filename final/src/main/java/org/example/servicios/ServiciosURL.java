package org.example.servicios;

import org.example.BaseDatos.GestionBD;
import org.example.encapsulacion.URL;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Queue;

public class ServiciosURL extends GestionBD<URL> {

    private static ServiciosURL instancia;

    public ServiciosURL() {
        super(URL.class);
        EntityManager em = getEntityManager();
    }

    public static ServiciosURL getInstancia() {
        if(instancia == null){
            instancia = new ServiciosURL();
        }

        return instancia;
    }

    public static void setInstancia(ServiciosURL instancia) {
        ServiciosURL.instancia = instancia;
    }

    public List<URL> findAll(){
        EntityManager em = getEntityManager();
        Query query = em.createNativeQuery("select * from URL", URL.class);
        List<URL> list = query.getResultList();

        return list;
    }

    public URL getProductByID(String id){

        for (URL url : findAll()){
            if (url.getIdURL().equals(id)){
                System.out.println("Llego el producto por ID: "+ url.getIdURL());
                return url;
            }
        }
        return null;
    }

    public URL getURLByID(String id){

        for (URL url : findAll()){
            if (url.getIdURL().equals(id)){
                System.out.println("Llego el producto por ID: "+ url.getIdURL());
                return url;
            }
        }
        return null;
    }
}
