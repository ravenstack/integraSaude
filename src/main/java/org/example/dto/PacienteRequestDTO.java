package org.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;
import java.time.LocalDate;

public record PacienteRequestDTO(
        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @NotBlank(message = "O CPF é obrigatório")
        @CPF(message = "Formato de CPF inválido")
        String cpf,

        @Email(message = "Formato de e-mail inválido")
        @NotBlank(message = "O e-mail é obrigatório")
        String email,

        String telefone,

        LocalDate dataNascimento,

        @NotBlank(message = "A senha é obrigatória")
        String senha
) {}