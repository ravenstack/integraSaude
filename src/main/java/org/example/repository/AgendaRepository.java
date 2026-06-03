package org.example.repository;

import org.example.model.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Integer> {


    boolean existsByProfissionalId(Integer id);

    List<Agenda> findByProfissionalId(Integer profissionalId);
}