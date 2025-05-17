package com.example.sistema_votacao.Repository;

import org.springframework.data.jpa.repository.JpaRepository; //salva, busca, deleta
import org.springframework.stereotype.Repository;

import com.example.sistema_votacao.Model.Votacao;

import java.util.List;

@Repository
public interface VotacaoRepository extends JpaRepository<Votacao, Long> { // por conta do private Long id
    List<Votacao> findByTituloContainingIgnoreCase(String titulo);

    List<Votacao> findByInicioBeforeAndFimAfter(java.sql.Timestamp agora1, java.sql.Timestamp agora2);

    List<Votacao> findByInicioAfterAndFimBefore(java.sql.Timestamp agora1, java.sql.Timestamp agora2);
}