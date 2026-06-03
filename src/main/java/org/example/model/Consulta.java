package org.example.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.example.enums.statusConsulta;
import org.example.enums.tipoSessao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Consulta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "profissional_id")
    private Profissional profissional;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataHora;

    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    private tipoSessao tipo;

    @Enumerated(EnumType.STRING)
    private statusConsulta status;

    private String observacoes;

    public Consulta() {
    }

    // Métodos de Regra de Negócio
    public void confirmar() {
        this.status = statusConsulta.REALIZADA;
    }

    public void cancelar() {
        this.status = statusConsulta.CANCELADA;
    }

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public Profissional getProfissional() { return profissional; }
    public void setProfissional(Profissional profissional) { this.profissional = profissional; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public tipoSessao getTipo() { return tipo; }
    public void setTipo(tipoSessao tipo) { this.tipo = tipo; }

    public statusConsulta getStatus() { return status; }
    public void setStatus(statusConsulta status) { this.status = status; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}