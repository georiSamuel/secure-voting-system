package com.example.sistema_votacao.Votante;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VotanteRepository extends JpaRepository<Votante, Long> {
    Votante findByCpf(String cpf); //
}