package org.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class SessaoUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer usuarioId;
    private String perfil; // "PACIENTE" ou "PSICOLOGO"

    private String navegador;
    private String sistemaOperacional;
    private String ip;

    private LocalDateTime dataLogin;
    private boolean ativa;

    // Construtor, Getters e Setters
    public SessaoUsuario() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }
    public String getPerfil() { return perfil; }
    public void setPerfil(String perfil) { this.perfil = perfil; }
    public String getNavegador() { return navegador; }
    public void setNavegador(String navegador) { this.navegador = navegador; }
    public String getSistemaOperacional() { return sistemaOperacional; }
    public void setSistemaOperacional(String sistemaOperacional) { this.sistemaOperacional = sistemaOperacional; }
    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }
    public LocalDateTime getDataLogin() { return dataLogin; }
    public void setDataLogin(LocalDateTime dataLogin) { this.dataLogin = dataLogin; }
    public boolean isAtiva() { return ativa; }
    public void setAtiva(boolean ativa) { this.ativa = ativa; }
}