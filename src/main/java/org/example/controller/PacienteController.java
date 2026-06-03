package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.PacienteRequestDTO;
import org.example.dto.PacienteResponseDTO;
import org.example.service.PacienteService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
@CrossOrigin(origins = "*")
public class PacienteController {

    private final PacienteService service;

    // Injeção via construtor: Mantém o padrão seguro, testável e limpo
    public PacienteController(PacienteService service) {
        this.service = service;
    }

    // Listar todos os pacientes registrados
    @GetMapping
    public List<PacienteResponseDTO> listarTodos() {
        return service.listarTodos();
    }

    // NOVO: Buscar os dados de um paciente específico pelo ID (Essencial para telas de Perfil/Configurações)
    @GetMapping("/{id}")
    public PacienteResponseDTO buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    // Salvar o cadastro de um novo paciente (Gera resposta HTTP 201 Created)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PacienteResponseDTO salvar(@Valid @RequestBody PacienteRequestDTO dto) {
        return service.salvar(dto);
    }

    // Atualizar os dados cadastrais de um paciente existente
    @PutMapping("/{id}")
    public PacienteResponseDTO atualizar(@PathVariable Integer id, @Valid @RequestBody PacienteRequestDTO dto) {
        return service.atualizar(id, dto);
    }

    // Exportar o relatório clínico de pacientes cadastrados em formato CSV de forma dinâmica
    @GetMapping(value = "/exportar-csv", produces = "text/csv")
    public ResponseEntity<String> exportarCsv() {
        List<PacienteResponseDTO> pacientes = service.listarTodos();

        // Constrói a estrutura de dados textual do arquivo CSV
        StringBuilder csv = new StringBuilder();
        csv.append("ID,Nome,CPF,Email,Telefone,Data de Nascimento\n");

        for (PacienteResponseDTO p : pacientes) {
            csv.append(p.id()).append(",")
                    .append(p.nome()).append(",")
                    .append(p.cpf()).append(",")
                    .append(p.email()).append(",")
                    .append(p.telefone() != null ? p.telefone() : "").append(",")
                    .append(p.dataNascimento() != null ? p.dataNascimento() : "").append("\n");
        }

        // Retorna forçando o navegador cliente a disparar o fluxo de download nativo do arquivo
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"relatorio_pacientes.csv\"")
                .body(csv.toString());
    }

    // Deletar o registro de um paciente (Gera resposta HTTP 244 No Content)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        service.deletar(id);
    }
}