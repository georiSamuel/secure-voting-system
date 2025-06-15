package sistema.votacao.OpcaoVoto.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sistema.votacao.OpcaoVoto.Model.OpcaoVoto;

import java.util.List;
import java.util.Optional;

/**
 * Interface de repositório para gerenciar as operações de persistência da
 * entidade OpcaoVoto.
 * Extende JpaRepository para fornecer métodos CRUD e consultas personalizadas.
 * 
 * @author Lethycia
 * @version 1.0
 * @since 26/05/25
 */
@Repository
public interface OpcaoVotoRepository extends JpaRepository<OpcaoVoto, Long> {

    /**
     * Retorna a lista de opções de voto associadas a uma votação específica.
     * 
     * @param votacaoId ID da votação
     * @return lista de opções de voto
     */
    List<OpcaoVoto> findByVotacaoId(Long votacaoId);

    /**
     * Busca uma opção de voto pela descrição e pelo ID da votação.
     * 
     * @param descricao descrição da opção
     * @param votacaoId ID da votação
     * @return opção de voto correspondente, se existir
     */
    Optional<OpcaoVoto> findByDescricaoAndVotacaoId(String descricao, Long votacaoId);

    /**
     * Incrementa a quantidade de votos de uma opção de voto com base no seu ID.
     * 
     * @param id ID da opção de voto
     */
    @Modifying
    @Query("UPDATE OpcaoVoto o SET o.quantidadeVotos = o.quantidadeVotos + 1 WHERE o.id = :id")
    void incrementarVotos(Long id);

    /**
     * Conta quantas opções de voto existem em uma determinada votação.
     * 
     * @param votacaoId ID da votação
     * @return número de opções de voto
     */
    long countByVotacaoId(Long votacaoId);

    /**
     * Retorna as opções de voto de uma votação ordenadas pela quantidade de votos
     * (ranking).
     * 
     * @param votacaoId ID da votação
     * @return lista de opções de voto ordenadas por votos (decrescente)
     */
    @Query("SELECT o FROM OpcaoVoto o WHERE o.votacao.id = :votacaoId ORDER BY o.quantidadeVotos DESC")
    List<OpcaoVoto> findRankingByVotacaoId(Long votacaoId);

    /**
     * Retorna a opção de voto vencedora (com mais votos) de uma votação específica.
     * 
     * @param votacaoId ID da votação
     * @return opção de voto com maior quantidade de votos, se existir
     */
    @Query("SELECT o FROM OpcaoVoto o WHERE o.votacao.id = :votacaoId ORDER BY o.quantidadeVotos DESC LIMIT 1")
    Optional<OpcaoVoto> findVencedoraByVotacaoId(Long votacaoId);
}
