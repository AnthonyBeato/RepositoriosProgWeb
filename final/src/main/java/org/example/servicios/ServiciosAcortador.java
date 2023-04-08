package org.example.servicios;

import org.example.BaseDatos.GestionBD;
import org.example.encapsulacion.Acortador;
import org.example.encapsulacion.URL;
import org.example.encapsulacion.Usuario;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class ServiciosAcortador extends GestionBD<Acortador> {

    private static ServiciosAcortador instancia;

    public List<Acortador> listaAcortadoresParaNoRegistrados = new ArrayList<>();

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

    public List<Acortador> findAllByUser(Usuario usuario){
        EntityManager em = getEntityManager();
        Query query = em.createNativeQuery("select a.* from Acortador a WHERE a.usuario = :usuario", Acortador.class);
        query.setParameter("usuario", usuario);
        List<Acortador> list = query.getResultList();

        return list;
    }

    public Acortador findByOriginalUrl(String URLOriginal){
        for (Acortador acortado : findAll()){
            if (acortado.getURLOriginal().equals(URLOriginal)){
                return acortado;
            }
        }
        return null;
    }


    public Acortador findByOriginalUrlAndUser(String originalUrl, Usuario usuario) {
        EntityManager em = getEntityManager();
        Query query = em.createNativeQuery("SELECT a.* FROM Acortador a WHERE a.url_original = :originalUrl AND a.usuario = :usuario", Acortador.class);
        query.setParameter("originalUrl", originalUrl);
        query.setParameter("usuario", usuario);
        List<Acortador> list = query.getResultList();

        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }



    public Acortador findByShortUrl(String URLAcortado){

        for (Acortador acortado : findAll()){
            if (acortado.getURLAcortado().equals(URLAcortado)){
                return acortado;
            }
        }
        return null;
    }

    public String generateURLCorta(String URLOriginal){
        String base62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder shortUrl = new StringBuilder();

        // Convert long URL to unique hash code
        int hashCode = URLOriginal.hashCode();

        // Convert hash code to base62
        while (hashCode > 0) {
            shortUrl.append(base62.charAt(hashCode % 62));
            hashCode /= 62;
        }

        // Get absolute value of hashcode to avoid negative values
        hashCode = Math.abs(hashCode);

        // Convert hash code to base62
        while (hashCode > 0) {
            shortUrl.append(base62.charAt(hashCode % 62));
            hashCode /= 62;
        }

        // Pad the short URL with leading zeros
        while (shortUrl.length() < 8) {
            shortUrl.insert(0, '0');
        }

        // Add a random suffix to the short URL
        String suffix = UUID.randomUUID().toString().substring(0, 4);
        shortUrl.append(suffix);

//        // Check if short URL already exists
//        Acortador existingUrl = ServiciosAcortador.getInstancia().findByShortUrl(shortUrl.toString());
//        if (existingUrl != null) {
//            // Short URL already exists, generate a new one
//            return generateURLCorta(URLOriginal);
//        }

        // Short URL is unique, return it
        return shortUrl.toString();
    }


    public void incrementarContadorVisitas(Acortador acortador) {
        int visitasActuales = acortador.getVisits_counter();
        acortador.setVisits_counter(visitasActuales + 1);
    }

    public void actualizarAcortador(Acortador acortador) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(acortador);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }


    public List<Acortador> cambiarURLSAUsuario(Usuario usuario){
        List<Acortador> lista = listaAcortadoresParaNoRegistrados;

        for (Acortador acortador : lista) {
            //System.out.println("  Usuario anterior: "+ acortador.getUsuario().getUsuario());
            acortador.setUsuario(usuario);
            System.out.println("  Se pasó el url: " + acortador.getURLOriginal().getURLOriginal() + "al usuario: " + acortador.getUsuario().getUsuario());
        }

        return lista;
    }
}
