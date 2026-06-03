package org.example.service;

import org.example.model.Paciente;
import org.example.repository.PacienteRepository;
import org.example.repository.ConsultaRepository;
import org.example.dto.PacienteRequestDTO;
import org.example.dto.PacienteResponseDTO;
import org.example.exception.ResourceNotFoundException;
import org.example.exception.RegraNegocioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository repository;

    @Autowired
    private ConsultaRepository consultaRepository;

    public List<PacienteResponseDTO> listarTodos() {
        return repository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public PacienteResponseDTO buscarPorId(Integer id) {
        return repository.findById(id)
                .map(this::converterParaDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado com o ID: " + id));
    }

    public PacienteResponseDTO salvar(PacienteRequestDTO dto) {
        Paciente paciente = new Paciente();
        paciente.setNome(dto.nome());
        paciente.setCpf(dto.cpf());
        paciente.setEmail(dto.email());
        paciente.setTelefone(dto.telefone());
        paciente.setDataNascimento(dto.dataNascimento());
        paciente.setSenhaHash(dto.senha());

        Paciente salvo = repository.save(paciente);
        return converterParaDTO(salvo);
    }

    public PacienteResponseDTO atualizar(Integer id, PacienteRequestDTO dto) {
        return repository.findById(id).map(paciente -> {
            paciente.setNome(dto.nome());
            paciente.setCpf(dto.cpf());
            paciente.setEmail(dto.email());
            paciente.setTelefone(dto.telefone());
            paciente.setDataNascimento(dto.dataNascimento());

            if (dto.senha() != null && !dto.senha().isBlank()) {
                paciente.setSenhaHash(dto.senha());
            }

            return converterParaDTO(repository.save(paciente));
        }).orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado com o ID: " + id));
    }

    public void deletar(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Paciente não encontrado para exclusão.");
        }
        if (consultaRepository.existsByPacienteId(id)) {
            throw new RegraNegocioException("Não é possível excluir um paciente que possui consultas agendadas.");
        }

        repository.deleteById(id);
    }

    public PacienteResponseDTO autenticar(String email, String senha) {
        Paciente paciente = repository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com este e-mail."));

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(senha, paciente.getSenhaHash())) {
            throw new RegraNegocioException("Senha incorreta.");
        }

        return converterParaDTO(paciente);
    }

    private PacienteResponseDTO converterParaDTO(Paciente p) {
        return new PacienteResponseDTO(
                p.getId(),
                p.getNome(),
                p.getCpf(),
                p.getEmail(),
                p.getTelefone(),
                p.getDataNascimento()
        );
    }
}