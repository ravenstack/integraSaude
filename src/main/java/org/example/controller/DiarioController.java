package org.example.controller;

import org.example.model.Diario;
import org.example.model.Paciente;
import org.example.repository.DiarioRepository;
import org.example.repository.PacienteRepository;
import org.example.exception.ResourceNotFoundException;
import org.example.exception.RegraNegocioException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/diarios")
@CrossOrigin(origins = "*")
public class DiarioController {

    private final DiarioRepository repository;
    private final PacienteRepository pacienteRepository;

    // Injeção limpa via Construtor unificado
    public DiarioController(DiarioRepository repository, PacienteRepository pacienteRepository) {
        this.repository = repository;
        this.pacienteRepository = pacienteRepository;
    }

    // Listar registros de diário de um paciente específico (ordenados do mais recente para o mais antigo)
    @GetMapping("/paciente/{pacienteId}")
    public List<Diario> listarPorPaciente(@PathVariable Integer pacienteId) {
        if (!pacienteRepository.existsById(pacienteId)) {
            throw new ResourceNotFoundException("Paciente não encontrado com o ID: " + pacienteId);
        }
        return repository.findByPacienteIdOrderByDataCriacaoDesc(pacienteId);
    }

    // Salvar um novo registro de humor/anotação vinculando ao paciente correto
    @PostMapping("/{pacienteId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Diario salvar(@PathVariable Integer pacienteId, @RequestBody Diario diario) {
        // Busca o paciente sem a necessidade da conversão forçada Math.toIntExact
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Não foi possível salvar o diário. Paciente não encontrado com o ID: " + pacienteId));

        // Validação de Regra de Negócio: Garante a integridade do registro
        if (diario.getTitulo() == null || diario.getTitulo().isBlank()) {
            throw new RegraNegocioException("O campo título é obrigatório para registrar o diário de humor.");
        }

        // Configuração automatizada de metadados clínicos no lado do servidor
        diario.setPaciente(paciente);
        diario.setDataCriacao(LocalDateTime.now());

        return repository.save(diario);
    }
}