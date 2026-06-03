package org.example.service;

import org.example.model.Profissional;
import org.example.repository.ProfissionalRepository;
import org.example.repository.ConsultaRepository;
import org.example.repository.AgendaRepository;
import org.example.dto.ProfissionalRequestDTO;
import org.example.dto.ProfissionalResponseDTO;
import org.example.exception.ResourceNotFoundException;
import org.example.exception.RegraNegocioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfissionalService {

    @Autowired
    private ProfissionalRepository repository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private AgendaRepository agendaRepository;

    public List<ProfissionalResponseDTO> listarTodos() {
        return repository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public ProfissionalResponseDTO buscarPorId(Integer id) {
        return repository.findById(id)
                .map(this::converterParaDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Profissional não encontrado com o ID: " + id));
    }

    public ProfissionalResponseDTO salvar(ProfissionalRequestDTO dto) {
        Profissional profissional = new Profissional();
        profissional.setTipo(dto.tipo());
        profissional.setNome(dto.nome());
        profissional.setRegistroProfissional(dto.registroProfissional());
        profissional.setEspecialidade(dto.especialidade());
        profissional.setValorSessao(dto.valorSessao());
        profissional.setEmail(dto.email());

        // CORRIGIDO: era "HASH_DA_SENHA_" + senha (hash falso, vulnerabilidade grave)
        // O setSenhaHash() em Profissional.java já aplica BCrypt automaticamente
        profissional.setSenhaHash(dto.senha());

        Profissional salvo = repository.save(profissional);
        return converterParaDTO(salvo);
    }

    public ProfissionalResponseDTO atualizar(Integer id, ProfissionalRequestDTO dto) {
        return repository.findById(id).map(profissional -> {
            profissional.setTipo(dto.tipo());
            profissional.setNome(dto.nome());
            profissional.setRegistroProfissional(dto.registroProfissional());
            profissional.setEspecialidade(dto.especialidade());
            profissional.setValorSessao(dto.valorSessao());
            profissional.setEmail(dto.email());

            if (dto.senha() != null && !dto.senha().isBlank()) {
                // CORRIGIDO: mesma correção do salvar()
                profissional.setSenhaHash(dto.senha());
            }

            return converterParaDTO(repository.save(profissional));
        }).orElseThrow(() -> new ResourceNotFoundException("Profissional não encontrado com o ID: " + id));
    }

    public void deletar(Integer id) throws RegraNegocioException {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Profissional não encontrado para exclusão.");
        }

        if (consultaRepository.existsByProfissionalId(id)) {
            throw new RegraNegocioException("Não é possível excluir um profissional que possui consultas agendadas.");
        }
        if (agendaRepository.existsByProfissionalId(id)) {
            throw new RegraNegocioException("Não é possível excluir um profissional que possui horários na agenda.");
        }

        repository.deleteById(id);
    }

    public ProfissionalResponseDTO autenticar(String email, String senha) throws RegraNegocioException {
        Profissional profissional = repository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Profissional não encontrado com este e-mail."));

        // CORRIGIDO: era comparação de string com hash falso
        // Agora usa BCrypt.matches() igual ao PacienteService — consistente e seguro
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(senha, profissional.getSenhaHash())) {
            throw new RegraNegocioException("Senha incorreta.");
        }

        return converterParaDTO(profissional);
    }

    private ProfissionalResponseDTO converterParaDTO(Profissional p) {
        return new ProfissionalResponseDTO(
                p.getId(),
                p.getTipo(),
                p.getNome(),
                p.getRegistroProfissional(),
                p.getEspecialidade(),
                p.getValorSessao(),
                p.getEmail()
        );
    }
}