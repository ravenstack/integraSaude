package org.example.repository;

import org.example.model.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProfissionalRepository extends JpaRepository<Profissional, Integer> {

    Optional<Profissional> findByEmail(String email);
}