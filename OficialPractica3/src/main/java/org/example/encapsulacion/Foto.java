package org.example.encapsulacion;

//import jakarta.persistence.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
public class Foto implements Serializable {
    @Id
    private String idFoto;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "type")
    private String mimeType;
    @Lob
    @Column(name = "foto")
    private String fotoBase64;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    public Foto() {
    }

    public Foto(String nombre, String mimeType, String fotoBase64, Producto producto){
        this.nombre = nombre;
        this.mimeType = mimeType;
        this.fotoBase64 = fotoBase64;
        this.producto = producto;
        this.idFoto = UUID.randomUUID().toString();
    }

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }


    public String getIdFoto() {
        return idFoto;
    }

    public void setIdFoto(String idFoto) {
        this.idFoto = idFoto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getFotoBase64() {
        return fotoBase64;
    }

    public void setFotoBase64(String fotoBase64) {
        this.fotoBase64 = fotoBase64;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}