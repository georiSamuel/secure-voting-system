package com.example.sistema_votacao.Usuario.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.sistema_votacao.Usuario.Model.UsuarioModel;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {
    
    Optional<UsuarioModel> findByEmail(String email);
}
