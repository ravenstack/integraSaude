package org.example.repository;

import org.example.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Integer> {
    Optional<Paciente> findByEmail(String email);

}