package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import java.math.BigDecimal;

/**
 * DTO para criação de conta de profissionais de saúde (Psicólogos, Nutricionistas, etc.)
 */
public record ProfissionalRequestDTO(
        @NotBlank(message = "O tipo de profissional é obrigatório")
        String tipo, // Ex: psicologo, nutricionista, psiquiatra...

        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @NotBlank(message = "O registro profissional é obrigatório")
        String registroProfissional, // Antigo CRP, agora aceita CRM, CRN, etc.

        @NotBlank(message = "A especialidade é obrigatória")
        String especialidade,

        @NotNull(message = "O valor da sessão é obrigatório")
        BigDecimal valorSessao,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "E-mail inválido")
        String email,
        String senha
) {}