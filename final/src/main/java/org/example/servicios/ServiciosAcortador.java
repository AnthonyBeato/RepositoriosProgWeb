package org.example.servicios;

import org.example.BaseDatos.GestionBD;
import org.example.encapsulacion.Acortador;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class ServiciosAcortador extends GestionBD<Acortador> {

    private static ServiciosAcortador instancia;

    public ServiciosAcortador() {
        super(Acortador.class);
        EntityManager em = getEntityManager();
    }

    public static ServiciosAcortador getInstancia() {
        if(instancia == null){
            instancia = new ServiciosAcortador();
        }

        return instancia;
    }

    public static void setInstancia(ServiciosAcortador instancia) {
        ServiciosAcortador.instancia = instancia;
    }

    public List<Acortador> findAll(){
        EntityManager em = getEntityManager();
        Query query = em.createNativeQuery("select * from Acortador ", Acortador.class);
        List<Acortador> list = query.getResultList();
        return list;
    }
}
