package org.example.controller;

import org.example.model.SessaoUsuario;
import org.example.repository.SessaoUsuarioRepository;
import org.example.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seguranca")
@CrossOrigin(origins = "*")
public class SegurancaController {

    private final SessaoUsuarioRepository sessaoRepository;

    // Construtor explícito unificado
    public SegurancaController(SessaoUsuarioRepository sessaoRepository) {
        this.sessaoRepository = sessaoRepository;
    }

    // 1. Listar Sessões Ativas do Usuário
    @GetMapping("/sessoes/{perfil}/{usuarioId}")
    public List<SessaoUsuario> listarSessoes(@PathVariable String perfil, @PathVariable Integer usuarioId) {
        List<SessaoUsuario> sessoes = sessaoRepository.findByUsuarioIdAndPerfilAndAtivaTrueOrderByDataLoginDesc(usuarioId, perfil.toUpperCase());
        if (sessoes.isEmpty()) {
            throw new ResourceNotFoundException("Nenhuma sessão ativa encontrada para este usuário.");
        }
        return sessoes;
    }

    // 2. Encerrar Outras Sessões (Logout em outros dispositivos)
    @PostMapping("/sessoes/encerrar-outras")
    public ResponseEntity<String> encerrarOutrasSessoes(@RequestParam Integer usuarioId, @RequestParam String perfil) {
        List<SessaoUsuario> sessoes = sessaoRepository.findByUsuarioIdAndPerfilAndAtivaTrueOrderByDataLoginDesc(usuarioId, perfil.toUpperCase());

        // Desativa de forma sequencial todas as sessões antigas, preservando apenas o login mais recente
        if (sessoes.size() > 1) {
            for (int i = 1; i < sessoes.size(); i++) {
                SessaoUsuario s = sessoes.get(i);
                s.setAtiva(false);
                sessaoRepository.save(s);
            }
        }
        return ResponseEntity.ok("Sessões em outros dispositivos encerradas com sucesso.");
    }

    // 3. Simulação de Ativação de 2FA
    @PostMapping("/2fa/toggle")
    public ResponseEntity<String> toggle2FA(@RequestParam Integer usuarioId, @RequestParam String perfil) {
        // Encaixe perfeito para futuras integrações de autenticação de dois fatores baseadas no DESIGN.md
        return ResponseEntity.ok("Configuração de autenticação em dois fatores (2FA) atualizada.");
    }
}