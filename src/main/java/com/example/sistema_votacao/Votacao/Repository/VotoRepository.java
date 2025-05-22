package com.example.sistema_votacao.Votacao.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.sistema_votacao.Votacao.Model.Voto;

import java.util.List;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {

    List<Voto> findByVotacaoId(Long votacaoId); // voto da votacao

    List<Voto> findByVotanteId(Long votanteId); // voto do votante

    boolean existsByUsuarioIdAndVotacaoId(Long usuarioId, Long votacaoId);
}