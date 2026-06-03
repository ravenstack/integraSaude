package org.example.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.example.enums.diaSemana;

import java.time.LocalTime;

@Entity
public class Agenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private diaSemana diaSemana;

    @ManyToOne
    @JoinColumn(name = "profissional_id")
    private Profissional profissional; // Alterado de Psicologo para Profissional

    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaInicio;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaFim;

    public Agenda() {
    }

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public diaSemana getDiaSemana() { return diaSemana; }
    public void setDiaSemana(diaSemana diaSemana) { this.diaSemana = diaSemana; }

    public Profissional getProfissional() { return profissional; }
    public void setProfissional(Profissional profissional) { this.profissional = profissional; }

    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }

    public LocalTime getHoraFim() { return horaFim; }
    public void setHoraFim(LocalTime horaFim) { this.horaFim = horaFim; }
}