package org.example.repository;

import org.example.model.Diario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DiarioRepository extends JpaRepository<Diario, Long> {
    List<Diario> findByPacienteIdOrderByDataCriacaoDesc(Integer pacienteId);
}