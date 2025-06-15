package sistema.votacao.Votacao.Repository;

import org.springframework.data.jpa.repository.JpaRepository; //salva, busca, deleta
import org.springframework.stereotype.Repository;

import sistema.votacao.Votacao.Model.Votacao;

import java.util.List;

/**
 * Interface de repositório para gerenciar as operações de persistência da
 * entidade Votacao.
 * Extende JpaRepository para fornecer métodos CRUD e consultas personalizadas.
 * 
 * @author Lethycia
 * @version 1.0
 * @since 26/05/25
 */
@Repository
public interface VotacaoRepository extends JpaRepository<Votacao, Long> { // por conta do private Long id
    List<Votacao> findByTituloContainingIgnoreCase(String titulo);

    List<Votacao> findByInicioBeforeAndFimAfter(java.sql.Timestamp agora1, java.sql.Timestamp agora2);

    List<Votacao> findByInicioAfterAndFimBefore(java.sql.Timestamp agora1, java.sql.Timestamp agora2);
}