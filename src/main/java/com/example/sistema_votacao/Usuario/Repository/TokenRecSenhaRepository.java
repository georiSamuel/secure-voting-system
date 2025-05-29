package com.example.sistema_votacao.Usuario.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.sistema_votacao.Usuario.Model.TokenRecSenhaModel;

public interface TokenRecSenhaRepository extends JpaRepository<TokenRecSenhaModel, Long> {

    Optional<TokenRecSenhaModel> findByToken(String token);
}