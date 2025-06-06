package sistema.votacao.Voto.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import sistema.votacao.Voto.Model.OpcaoVoto;

import java.util.List;
import java.util.Optional;

@Repository
public interface OpcaoVotoRepository extends JpaRepository<OpcaoVoto, Long> {

    List<OpcaoVoto> findByVotacaoId(Long votacaoId);

    Optional<OpcaoVoto> findByDescricaoAndVotacaoId(String descricao, Long votacaoId);

    @Modifying
    @Query("UPDATE OpcaoVoto o SET o.quantidadeVotos = o.quantidadeVotos + 1 WHERE o.id = :id")
    void incrementarVotos(Long id);

    long countByVotacaoId(Long votacaoId);

    @Query("SELECT o FROM OpcaoVoto o WHERE o.votacao.id = :votacaoId ORDER BY o.quantidadeVotos DESC")
    List<OpcaoVoto> findRankingByVotacaoId(Long votacaoId);

    @Query("SELECT o FROM OpcaoVoto o WHERE o.votacao.id = :votacaoId ORDER BY o.quantidadeVotos DESC LIMIT 1")
    Optional<OpcaoVoto> findVencedoraByVotacaoId(Long votacaoId);
}