package org.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    // ==========================================
    // 📁 FLUXO DE AUTENTICAÇÃO (Pasta: templates/login/)
    // ==========================================

    @GetMapping("/login")
    public String abrirLogin() {
        return "login/login"; // Caminho corrigido para a subpasta
    }

    @GetMapping("/cadastro-escolha")
    public String abrirCadastroEscolha() {
        return "login/cadastro-escolha";
    }

    @GetMapping("/cadastro-paciente")
    public String abrirCadastroPaciente() {
        return "login/cadastro-paciente";
    }

    @GetMapping("/cadastro-profissional")
    public String abrirCadastroProfissional() {
        return "login/cadastro-profissional";
    }

    @GetMapping("/recuperar-senha")
    public String abrirRecuperarSenha() {
        return "login/recuperar-senha";
    }

    // ==========================================
    // 📁 ÁREA DO PACIENTE (Pasta: templates/paciente/)
    // ==========================================

    @GetMapping("/dashboard-paciente")
    public String abrirDashboardPaciente() {
        return "paciente/dashboard-paciente";
    }

    @GetMapping("/agenda-paciente")
    public String abrirAgendaPaciente() {
        return "paciente/agenda-paciente";
    }

    @GetMapping("/diario-paciente")
    public String abrirDiarioPaciente() {
        return "paciente/diario-paciente";
    }

    @GetMapping("/documentacao-paciente")
    public String abrirDocumentacaoPaciente() {
        return "paciente/documentacao-paciente";
    }

    @GetMapping("/pagamentos-paciente")
    public String abrirPagamentosPaciente() {
        return "paciente/pagamentos-paciente";
    }

    @GetMapping("/perfil-paciente")
    public String abrirPerfilPaciente() {
        return "paciente/perfil-paciente";
    }

    @GetMapping("/preferencias-paciente")
    public String abrirPreferenciasPaciente() {
        return "paciente/preferencias-paciente";
    }

    @GetMapping("/seguranca-paciente")
    public String abrirSegurancaPaciente() {
        return "paciente/seguranca-paciente";
    }

    @GetMapping("/marcar-consulta")
    public String abrirMarcarConsulta() {
        return "paciente/marcar-consulta";
    }

    @GetMapping("/documentos-paciente")
    public String abrirDocumentosPaciente() {
        return "paciente/documentos-paciente";
    }

    @GetMapping("/suporte-paciente")
    public String abrirSuportePaciente() {
        return "paciente/suporte-paciente";
    }

    // ==========================================
    // 📁 ÁREA DO PROFISSIONAL/MÉDICO
    // ==========================================

    @GetMapping("/medico")
    public String abrirAreaMedico() {
        return "medico/medico-layout";
    }

    @GetMapping("/dashboard-profissional")
    public String abrirDashboardProfissional() {
        return "profissional/dashboard-profissional";
    }
}