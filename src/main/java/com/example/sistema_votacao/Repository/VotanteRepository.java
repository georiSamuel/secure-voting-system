package com.example.sistema_votacao.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.sistema_votacao.Model.Votante;

public interface VotanteRepository extends JpaRepository<Votante, Long> {
    Votante findByCpf(String cpf); //
}