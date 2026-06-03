package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Diario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String conteudo;

    private LocalDateTime dataCriacao;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;
}