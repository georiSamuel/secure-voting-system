package com.example.sistema_votacao.Voto.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.sistema_votacao.Voto.Model.VotoModel;

import java.util.List;

@Repository
public interface VotoRepository extends JpaRepository<VotoModel, Long> {

    List<VotoModel> findByVotacaoId(Long votacaoId); // voto da votacao

    List<VotoModel> findByVotanteId(Long votanteId); // voto do votante

    boolean existsByUsuario_IdAndVotacao_Id(Long usuarioId, Long votacaoId);

    boolean existsByUsuarioIdAndVotacaoId(Long usuarioId, Long votacaoId);


}

