package org.example.servicios;

import org.example.BaseDatos.GestionBD;
import org.example.encapsulacion.Usuario;
import org.example.encapsulacion.URL;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class ServiciosUsuario extends GestionBD<Usuario> {

    private static ServiciosUsuario instancia;

    public static void setInstancia(ServiciosUsuario instancia) {
        ServiciosUsuario.instancia = instancia;
    }

    public ServiciosUsuario() {
        super(Usuario.class);
        EntityManager em = getEntityManager();
    }
    //private List<Usuario> listaUsuarios = new ArrayList<>();
//    private ServiciosUsuario(){
//        listaUsuarios.add(new Usuario("admin", "admin", "admin"));
//        listaUsuarios.add(new Usuario("Anthony", "anthony", "1234"));
//    }

    //Validar usuario
    public Usuario authenticateUsuario(String usuario, String password){
        Usuario tempUsuario = getUsuariotByUser(usuario);
        if(tempUsuario != null){
            if(tempUsuario.getContrasena().equals(password)){
                return tempUsuario;
            }
        }
        return null;
    }

    public static ServiciosUsuario getInstancia() {
        if(instancia == null){
            instancia = new ServiciosUsuario();
        }

        return instancia;
    }

    public Usuario getUsuariotByUser (String usuario){

        EntityManager em = getEntityManager();
        Query query = em.createQuery("select e from Usuario e where e.usuario like :usuario");
        query.setParameter("usuario", "%"+usuario+"%");
        List<Usuario> list = query.getResultList();

        if (list.isEmpty()) {
            return null;
        }

        return list.get(0);
    }

    public Usuario getUsuarioByID(String id){

        for (Usuario usuario : findAll()){
            if (usuario.getIdUsuario().equals(id)){
                return usuario;
            }
        }
        return null;
    }

    public List<Usuario> findAll(){
        EntityManager em = getEntityManager();
        Query query = em.createNativeQuery("select * from Usuario ", Usuario.class);
        List<Usuario> list = query.getResultList();
        return list;
    }

    public boolean validacionRegistro(Usuario usuario, String confirmacionContrasena){
        EntityManager em = getEntityManager();
        Query query = em.createNativeQuery("select * from Usuario ", Usuario.class);
        List<Usuario> list = query.getResultList();

        if(list.stream().anyMatch(u -> u.getUsuario().equals(usuario.getUsuario()))){
            return false;
        }

        if(!usuario.getContrasena().equals(confirmacionContrasena)){
            return false;
        }

        System.out.println("Se registró el usuario: "+ usuario.getUsuario());
        ServiciosUsuario.getInstancia().crear(usuario);
        return true;
    }
    public List<URL> obtenerUrlsPorUsuario(String usuarioId) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select u from URL u where u.usuario.idUsuario = :usuarioId", URL.class);
        query.setParameter("usuarioId", usuarioId);
        return query.getResultList();
    }

    public URL crearUrlParaUsuario(Usuario usuario, String urlOriginal, String urlCorta) {
        URL nuevaUrl = new URL(usuario, urlOriginal, urlCorta);
        ServiciosURL.getInstancia().crear(nuevaUrl);
        return nuevaUrl;
    }


}
