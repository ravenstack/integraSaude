package org.example.controller;

import org.example.model.Agenda;
import org.example.repository.AgendaRepository;
import org.example.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agendas")
@CrossOrigin(origins = "*")
public class AgendaController {

    private final AgendaRepository repository;

    // Injeção via construtor: Mais seguro, limpo e recomendado pelo Spring Team
    public AgendaController(AgendaRepository repository) {
        this.repository = repository;
    }

    // Listar todas as agendas cadastradas no sistema
    @GetMapping
    public List<Agenda> listarTodas() {
        return repository.findAll();
    }

    @GetMapping("/profissional/{profissionalId}")
    public List<Agenda> listarPorProfissional(@PathVariable Integer profissionalId) {
        return repository.findByProfissionalId(profissionalId);
    }

    // Buscar uma configuração de agenda específica pelo ID
    @GetMapping("/{id}")
    public Agenda buscarPorId(@PathVariable Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Horário de agenda não encontrado com o ID: " + id));
    }

    // Salvar um novo bloco de horários de atendimento
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Agenda salvar(@RequestBody Agenda agenda) {
        return repository.save(agenda);
    }

    // Atualizar um bloco de horários existente
    @PutMapping("/{id}")
    public Agenda atualizar(@PathVariable Integer id, @RequestBody Agenda agendaAtualizada) {
        return repository.findById(id).map(agenda -> {
            agenda.setDiaSemana(agendaAtualizada.getDiaSemana());
            agenda.setHoraInicio(agendaAtualizada.getHoraInicio());
            agenda.setHoraFim(agendaAtualizada.getHoraFim());
            agenda.setProfissional(agendaAtualizada.getProfissional());
            return repository.save(agenda);
        }).orElseThrow(() -> new ResourceNotFoundException("Registro de agenda não encontrado com o ID: " + id));
    }

    // Deletar um bloco de horários
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Impossível deletar. Registro não encontrado com o ID: " + id);
        }
        repository.deleteById(id);
    }
}