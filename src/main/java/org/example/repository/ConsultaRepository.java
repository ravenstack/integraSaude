package org.example.repository;

import org.example.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Integer> {

    boolean existsByProfissionalId(Integer profissionalId);
    boolean existsByPacienteId(Integer pacienteId);

    List<Consulta> findByPacienteId(Integer pacienteId);
    List<Consulta> findByProfissionalId(Integer profissionalId);

}