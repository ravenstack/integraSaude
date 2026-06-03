package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.ProfissionalRequestDTO;
import org.example.dto.ProfissionalResponseDTO;
import org.example.service.ProfissionalService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cadastro-profissionais")
@CrossOrigin(origins = "*")
public class ProfissionalController {

    private final ProfissionalService service;

    // Injeção de dependência moderna via construtor unificado
    public ProfissionalController(ProfissionalService service) {
        this.service = service;
    }

    // Listar todos os profissionais/clínicos cadastrados no ecossistema
    @GetMapping
    public List<ProfissionalResponseDTO> listarTodos() {
        return service.listarTodos();
    }

    // NOVO: Buscar os dados de um profissional específico pelo ID (Essencial para carregar o Painel do Psicólogo)
    @GetMapping("/{id}")
    public ProfissionalResponseDTO buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    // Salvar o cadastro de um novo profissional (Gera resposta HTTP 201 Created)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProfissionalResponseDTO salvar(@Valid @RequestBody ProfissionalRequestDTO dto) {
        return service.salvar(dto);
    }

    // Atualizar dados do profissional existente (especialidade, valor da sessão, etc)
    @PutMapping("/{id}")
    public ProfissionalResponseDTO atualizar(@PathVariable Integer id, @Valid @RequestBody ProfissionalRequestDTO dto) {
        return service.atualizar(id, dto);
    }

    // Deletar o registro de um profissional do sistema (Gera resposta HTTP 204 No Content)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        service.deletar(id);
    }
}