package org.example.encapsulacion;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "acortador")
public class Acortador {
    @Id
    private String idAcortador;
    @Column(name = "url_acortado")
    private String URLAcortado;

    @ManyToOne
    @JoinColumn(name = "url_original")
    private URL URLOriginal;

    @Column(name = "created_date_time")
    private LocalDateTime created_date_time;
    @Column(name = "visits_counter")
    private int visits_counter;
    @Column(name = "usuario_agente")
    private String usuario_agente;
    @Column(name = "ip_address")
    private String ip_address;

    public Acortador(String URLAcortado, URL URLOriginal, LocalDateTime created_date_time, int visits_counter, String usuario_agente, String ip_address) {
        this.idAcortador = UUID.randomUUID().toString();
        this.URLAcortado = URLAcortado;
        this.URLOriginal = URLOriginal;
        this.created_date_time = created_date_time;
        this.visits_counter = visits_counter;
        this.usuario_agente = usuario_agente;
        this.ip_address = ip_address;
    }

    public Acortador() {

    }

    public String getIdAcortador() {
        return idAcortador;
    }

    public void setIdAcortador(String idAcortador) {
        this.idAcortador = idAcortador;
    }

    public String getURLAcortado() {
        return URLAcortado;
    }

    public void setURLAcortado(String URLAcortado) {
        this.URLAcortado = URLAcortado;
    }

    public URL getURLOriginal() {
        return URLOriginal;
    }

    public void setURLOriginal(URL URLOriginal) {
        this.URLOriginal = URLOriginal;
    }

    public LocalDateTime getCreated_date_time() {
        return created_date_time;
    }

    public void setCreated_date_time(LocalDateTime created_date_time) {
        this.created_date_time = created_date_time;
    }

    public int getVisits_counter() {
        return visits_counter;
    }

    public void setVisits_counter(int visits_counter) {
        this.visits_counter = visits_counter;
    }

    public String getUsuario_agente() {
        return usuario_agente;
    }

    public void setUsuario_agente(String usuario_agente) {
        this.usuario_agente = usuario_agente;
    }

    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }
}

