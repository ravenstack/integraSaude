package org.example.controller;

import org.example.model.Documento;
import org.example.model.Paciente;
import org.example.repository.DocumentoRepository;
import org.example.repository.PacienteRepository;
import org.example.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/documentos")
@CrossOrigin(origins = "*")
public class DocumentoController {

    private final DocumentoRepository repository;
    private final PacienteRepository pacienteRepository;

    public DocumentoController(DocumentoRepository repository, PacienteRepository pacienteRepository) {
        this.repository = repository;
        this.pacienteRepository = pacienteRepository;
    }

    @GetMapping("/paciente/{pacienteId}")
    public List<Documento> listarPorPaciente(@PathVariable Integer pacienteId) {
        if (!pacienteRepository.existsById(pacienteId)) {
            throw new ResourceNotFoundException("Paciente não encontrado com o ID: " + pacienteId);
        }
        return repository.findByPacienteIdOrderByDataEnvioDesc(pacienteId);
    }

    @PostMapping("/{pacienteId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Documento salvar(@PathVariable Integer pacienteId, @RequestBody Documento documento) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado com o ID: " + pacienteId));

        documento.setPaciente(paciente);
        documento.setDataEnvio(LocalDateTime.now());

        return repository.save(documento);
    }

    @PostMapping("/upload/{pacienteId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Documento uploadDocumento(@PathVariable Integer pacienteId,
                                     @RequestParam("arquivo") MultipartFile arquivo,
                                     @RequestParam("tipo") String tipo,
                                     @RequestParam(value = "descricao", required = false) String descricao) throws IOException {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado com o ID: " + pacienteId));

        Documento documento = new Documento();
        documento.setPaciente(paciente);
        documento.setNome(arquivo.getOriginalFilename());
        documento.setTamanho(formatarTamanho(arquivo.getSize()));
        documento.setUrl("/documentos/" + pacienteId + "/" + arquivo.getOriginalFilename());
        documento.setDataEnvio(LocalDateTime.now());

        return repository.save(documento);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Documento não encontrado com o ID: " + id);
        }
        repository.deleteById(id);
    }

    @PutMapping("/{id}")
    public Documento atualizar(@PathVariable Integer id, @RequestBody Documento documentoAtualizado) {
        return repository.findById(id).map(documento -> {
            if (documentoAtualizado.getNome() != null) {
                documento.setNome(documentoAtualizado.getNome());
            }
            if (documentoAtualizado.getUrl() != null) {
                documento.setUrl(documentoAtualizado.getUrl());
            }
            return repository.save(documento);
        }).orElseThrow(() -> new ResourceNotFoundException("Documento não encontrado com o ID: " + id));
    }

    private String formatarTamanho(long bytes) {
        if (bytes <= 0) return "0 B";
        final String[] units = new String[]{"B", "KB", "MB", "GB"};
        int digitGroups = (int) (Math.log10(bytes) / Math.log10(1024));
        return String.format("%.1f %s", bytes / Math.pow(1024, digitGroups), units[digitGroups]);
    }
}
