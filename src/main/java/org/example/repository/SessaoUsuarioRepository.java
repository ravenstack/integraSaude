package org.example.repository;

import org.example.model.SessaoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessaoUsuarioRepository extends JpaRepository<SessaoUsuario, Integer> {
    List<SessaoUsuario> findByUsuarioIdAndPerfilAndAtivaTrueOrderByDataLoginDesc(Integer usuarioId, String perfil);
}