package org.example.repository;

import org.example.model.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Integer> {
    List<Documento> findByPacienteId(Integer pacienteId);
    List<Documento> findByPacienteIdOrderByDataEnvioDesc(Integer pacienteId);
}
