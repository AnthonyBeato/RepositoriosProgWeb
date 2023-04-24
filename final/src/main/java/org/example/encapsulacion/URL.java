package org.example.encapsulacion;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name="url")
public class URL {
    @Id
    private String idURL;
    @Column(name = "url_original")
    private String URLOriginal;
    @ManyToOne
    @JoinColumn(name = "usuario")
    private Usuario usuario;

    @Lob
    @Column(name = "foto")
    private String fotoBase64;

    public URL(String URLOriginal, Usuario usuario) {
        this.idURL = UUID.randomUUID().toString();
        this.URLOriginal = URLOriginal;
        this.usuario = usuario;
    }

    public URL(String URLOriginal, Usuario usuario, String fotoBase64) {
        this.idURL = UUID.randomUUID().toString();
        this.URLOriginal = URLOriginal;
        this.usuario = usuario;
        this.fotoBase64 = fotoBase64;
    }

    public URL(Usuario usuario, String urlOriginal, String urlCorta) {

    }
    public URL(){

    }

    public String getIdURL() {
        return idURL;
    }

    public void setIdURL(String idURL) {
        this.idURL = idURL;
    }

    public String getURLOriginal() {
        return URLOriginal;
    }

    public void setURLOriginal(String URLOriginal) {
        this.URLOriginal = URLOriginal;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }


    public String getFotoBase64() {
        return fotoBase64;
    }

    public void setFotoBase64(String fotoBase64) {
        this.fotoBase64 = fotoBase64;
    }
}
