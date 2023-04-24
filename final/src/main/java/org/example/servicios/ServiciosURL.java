package org.example.servicios;

import org.example.BaseDatos.GestionBD;
import org.example.encapsulacion.URL;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class ServiciosURL extends GestionBD<URL> implements UrlService {

    private static ServiciosURL instancia;

    public ServiciosURL() {
        super(URL.class);
        EntityManager em = getEntityManager();
    }

    public static ServiciosURL getInstancia() {
        if (instancia == null) {
            instancia = new ServiciosURL();
        }

        return instancia;
    }

    public static void setInstancia(ServiciosURL instancia) {
        ServiciosURL.instancia = instancia;
    }

    @Override
    public List<URL> findAll() {
        EntityManager em = getEntityManager();
        Query query = em.createNativeQuery("select * from URL", URL.class);
        List<URL> list = query.getResultList();

        return list;
    }

    @Override
    public URL getProductByID(String id) {

        for (URL url : findAll()) {
            if (url.getIdURL().equals(id)) {
                System.out.println("Llego el producto por ID: " + url.getIdURL());
                return url;
            }
        }
        return null;
    }

    @Override
    public URL getURLByID(String id) {

        for (URL url : findAll()) {
            if (url.getIdURL().equals(id)) {
                System.out.println("Llego el producto por ID: " + url.getIdURL());
                return url;
            }
        }
        return null;
    }
}
