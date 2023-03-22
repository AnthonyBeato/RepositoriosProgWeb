package org.example.encapsulacion;
import  javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "comentario")
public class Comentario {
    @Id
    private String idComentario;
    @Column(name = "comentario")
    private String comentario;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    public Comentario(){}

    public Comentario(String comentario, Producto producto, Usuario usuario) {
        this.comentario = comentario;
        this.producto = producto;
        this.idComentario = UUID.randomUUID().toString();
        this.usuario = usuario;
    }

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }


    public String getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(String idComentario) {
        this.idComentario = idComentario;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
