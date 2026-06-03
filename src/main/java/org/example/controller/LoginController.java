package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.example.dto.LoginDTO;
import org.example.dto.PacienteRequestDTO;
import org.example.dto.PacienteResponseDTO;
import org.example.dto.ProfissionalRequestDTO;
import org.example.dto.ProfissionalResponseDTO;
import org.example.exception.RegraNegocioException;
import org.example.model.SessaoUsuario;
import org.example.repository.SessaoUsuarioRepository;
import org.example.service.PacienteService;
import org.example.service.ProfissionalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/login")
@CrossOrigin(origins = "*")
public class LoginController {

    private final PacienteService pacienteService;
    private final ProfissionalService profissionalService;
    private final SessaoUsuarioRepository sessaoRepository;

    // Construtor explícito: Robusto, limpo e em total conformidade com boas práticas corporativas
    public LoginController(PacienteService pacienteService,
                           ProfissionalService profissionalService,
                           SessaoUsuarioRepository sessaoRepository) {
        this.pacienteService = pacienteService;
        this.profissionalService = profissionalService;
        this.sessaoRepository = sessaoRepository;
    }

    @PostMapping
    public ResponseEntity<?> autenticar(@RequestBody LoginDTO login, HttpServletRequest request) {
        Integer usuarioId;
        Object resposta;

        // Triagem inteligente de tipo de Perfil de Acesso
        if ("PACIENTE".equals(login.perfil())) {
            PacienteResponseDTO paciente = pacienteService.autenticar(login.email(), login.senha());
            usuarioId = paciente.id();
            resposta = paciente;
        } else if ("PROFISSIONAL".equals(login.perfil())) {
            ProfissionalResponseDTO profissional = profissionalService.autenticar(login.email(), login.senha());
            usuarioId = profissional.id();
            resposta = profissional;
        } else {
            throw new RegraNegocioException("Perfil de acesso inválido.");
        }

        // Captura de metadados da requisição para Auditoria de Dispositivos
        String userAgent = request.getHeader("User-Agent");
        String ip = request.getRemoteAddr();

        SessaoUsuario sessao = new SessaoUsuario();
        sessao.setUsuarioId(usuarioId);
        sessao.setPerfil(login.perfil());
        sessao.setIp("0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip);
        sessao.setDataLogin(LocalDateTime.now());
        sessao.setAtiva(true);

        // Processamento modular simplificado da String User-Agent
        if (userAgent != null) {
            sessao.setNavegador(userAgent.contains("Chrome") ? "Chrome" : userAgent.contains("Firefox") ? "Firefox" : userAgent.contains("Safari") ? "Safari" : "Outro");
            sessao.setSistemaOperacional(userAgent.contains("Windows") ? "Windows PC" : userAgent.contains("Mac OS") ? "Mac/iOS" : userAgent.contains("Android") ? "Android" : "Outro");
        } else {
            sessao.setNavegador("Desconhecido");
            sessao.setSistemaOperacional("Desconhecido");
        }

        // Registra a sessão ativa no banco de dados para segurança do usuário
        sessaoRepository.save(sessao);

        return ResponseEntity.ok(resposta);
    }

    @PostMapping("/cadastro")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> cadastrarPaciente(@Valid @RequestBody PacienteRequestDTO pacienteData, HttpServletRequest request) {
        PacienteResponseDTO paciente = pacienteService.salvar(pacienteData);

        // Captura de metadados da requisição para Auditoria de Dispositivos
        String userAgent = request.getHeader("User-Agent");
        String ip = request.getRemoteAddr();

        SessaoUsuario sessao = new SessaoUsuario();
        sessao.setUsuarioId(paciente.id());
        sessao.setPerfil("PACIENTE");
        sessao.setIp("0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip);
        sessao.setDataLogin(java.time.LocalDateTime.now());
        sessao.setAtiva(true);

        if (userAgent != null) {
            sessao.setNavegador(userAgent.contains("Chrome") ? "Chrome" : userAgent.contains("Firefox") ? "Firefox" : userAgent.contains("Safari") ? "Safari" : "Outro");
            sessao.setSistemaOperacional(userAgent.contains("Windows") ? "Windows PC" : userAgent.contains("Mac OS") ? "Mac/iOS" : userAgent.contains("Android") ? "Android" : "Outro");
        } else {
            sessao.setNavegador("Desconhecido");
            sessao.setSistemaOperacional("Desconhecido");
        }

        sessaoRepository.save(sessao);

        return ResponseEntity.status(HttpStatus.CREATED).body(paciente);
    }

    @PostMapping("/cadastro-profissional")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> cadastrarProfissional(@Valid @RequestBody ProfissionalRequestDTO profissionalData, HttpServletRequest request) {
        ProfissionalResponseDTO profissional = profissionalService.salvar(profissionalData);

        // Captura de metadados da requisição para Auditoria de Dispositivos
        String userAgent = request.getHeader("User-Agent");
        String ip = request.getRemoteAddr();

        SessaoUsuario sessao = new SessaoUsuario();
        sessao.setUsuarioId(profissional.id());
        sessao.setPerfil("PROFISSIONAL");
        sessao.setIp("0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip);
        sessao.setDataLogin(java.time.LocalDateTime.now());
        sessao.setAtiva(true);

        if (userAgent != null) {
            sessao.setNavegador(userAgent.contains("Chrome") ? "Chrome" : userAgent.contains("Firefox") ? "Firefox" : userAgent.contains("Safari") ? "Safari" : "Outro");
            sessao.setSistemaOperacional(userAgent.contains("Windows") ? "Windows PC" : userAgent.contains("Mac OS") ? "Mac/iOS" : userAgent.contains("Android") ? "Android" : "Outro");
        } else {
            sessao.setNavegador("Desconhecido");
            sessao.setSistemaOperacional("Desconhecido");
        }

        sessaoRepository.save(sessao);

        return ResponseEntity.status(HttpStatus.CREATED).body(profissional);
    }
}