package org.example.dto;

import java.math.BigDecimal;

/**
 * DTO para retorno de dados de profissionais (Psicólogos, Nutricionistas, Médicos, etc.)
 */
public record ProfissionalResponseDTO(
        Integer id,
        String tipo, // Identifica a categoria (ex: nutricionista, psicologo)
        String nome,
        String registroProfissional, // Nome genérico para CRP, CRM, CRN, etc.
        String especialidade,
        BigDecimal valorSessao,
        String email
) {}