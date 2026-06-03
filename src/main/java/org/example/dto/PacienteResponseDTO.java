package org.example.dto;

import java.time.LocalDate;

public record PacienteResponseDTO(
        Integer id,
        String nome,
        String cpf,
        String email,
        String telefone,
        LocalDate dataNascimento
) {}