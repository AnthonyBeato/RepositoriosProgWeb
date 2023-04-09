package org.example.encapsulacion;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    @Column(name = "agente_cliente")
    private String agente_cliente;
    @Column(name = "ip_address")
    private String ip_address;
    @ManyToOne
    @JoinColumn(name = "usuario")
    private Usuario usuario;

    @ElementCollection
    private List<LocalDateTime> fechasAcceso = new ArrayList<>();

    @ElementCollection
    private List<String> direccionesIP = new ArrayList<>();

    @ElementCollection
    private List<String> agentesUsuario = new ArrayList<>();

    public Acortador(String URLAcortado, URL URLOriginal, LocalDateTime created_date_time, int visits_counter, String agente_cliente, String ip_address, Usuario usuario) {
        this.idAcortador = UUID.randomUUID().toString();
        this.URLAcortado = URLAcortado;
        this.URLOriginal = URLOriginal;
        this.created_date_time = created_date_time;
        this.visits_counter = visits_counter;
        this.agente_cliente = agente_cliente;
        this.ip_address = ip_address;
        this.usuario = usuario;
    }

    public void agregarFechaAcceso(LocalDateTime fecha) {
        this.fechasAcceso.add(fecha);
    }

    public void agregarDireccionIP(String direccionIP) {
        this.direccionesIP.add(direccionIP);
    }

    public void agregarAgenteUsuario(String agenteUsuario) {
        this.agentesUsuario.add(agenteUsuario);
    }

    public List<LocalDateTime> getFechasAcceso() {
        return fechasAcceso;
    }

    public List<String> getDireccionesIP() {
        return direccionesIP;
    }

    public List<String> getAgentesUsuario() {
        return agentesUsuario;
    }

    public Acortador() {

    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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

    public String getAgente_cliente() {
        return agente_cliente;
    }

    public void setAgente_cliente(String usuario_agente) {
        this.agente_cliente = usuario_agente;
    }

    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }
}

