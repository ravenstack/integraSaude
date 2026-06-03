package org.example.controller;

import org.example.model.Consulta;
import org.example.repository.ConsultaRepository;
import org.example.enums.statusConsulta;
import org.example.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultas")
@CrossOrigin(origins = "*")
public class ConsultaController {

    private final ConsultaRepository repository;

    public ConsultaController(ConsultaRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Consulta> listarTodas() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Consulta buscarPorId(@PathVariable Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada com o ID: " + id));
    }

    @GetMapping("/paciente/{pacienteId}")
    public List<Consulta> listarPorPaciente(@PathVariable Integer pacienteId) {
        return repository.findByPacienteId(pacienteId);
    }

    // CORRIGIDO: nome do parâmetro batendo com a URL (era "profesionalId", faltava um 's')
    @GetMapping("/profissional/{profissionalId}")
    public List<Consulta> listarPorProfissional(@PathVariable Integer profissionalId) {
        return repository.findByProfissionalId(profissionalId);
    }

    // CORRIGIDO: anotação corrompida (@劇esponseStatus → @ResponseStatus)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Consulta salvar(@RequestBody Consulta consulta) {
        consulta.setStatus(statusConsulta.AGENDADA);
        return repository.save(consulta);
    }

    @PutMapping("/{id}")
    public Consulta atualizar(@PathVariable Integer id, @RequestBody Consulta consultaAtualizada) {
        return repository.findById(id).map(consulta -> {
            consulta.setPaciente(consultaAtualizada.getPaciente());
            consulta.setDataHora(consultaAtualizada.getDataHora());
            consulta.setValor(consultaAtualizada.getValor());
            consulta.setTipo(consultaAtualizada.getTipo());
            consulta.setStatus(consultaAtualizada.getStatus());
            consulta.setObservacoes(consultaAtualizada.getObservacoes());
            return repository.save(consulta);
        }).orElseThrow(() -> new ResourceNotFoundException("Não foi possível atualizar. Consulta não encontrada com o ID: " + id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Não foi possível deletar. Consulta não encontrada com o ID: " + id);
        }
        repository.deleteById(id);
    }
}