package sistema.votacao.voto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sistema.votacao.voto.model.VotoModel;

import java.util.List;

/**
 * Repositório para gerenciar operações de persistência de votos. Ele fornece 
 * métodos para buscar votos por votação, usuário e verificar existência de votos.
 * @author Lethycia
 * @author Horlan
 * @version 2.0
 * @since 29/05/2025
 */
@Repository
public interface VotoRepository extends JpaRepository<VotoModel, Long> {

    /**
     * Método os votos associados a uma votação específica.
     * @param votacaoId
     * @return Lista de votos associados à votação especificada.
     */
    List<VotoModel> findByVotacaoId(Long votacaoId); // voto da votacao

    /**
     * Método para buscar votos por ID do usuário.
     * @param usuarioId
     * @return  Lista de votos associados ao usuário especificado.
     */
    List<VotoModel> findByUsuario_Id(Long usuarioId); // Correção: busca pelo ID do objeto 'usuario' dentro de VotoModel

    /**
     *  Método para verificar se já existe um voto de um usuário específico em uma votação específica.
     * @param usuarioId
     * @param votacaoId
     * @return  true se existir um voto do usuário na votação, false caso contrário.
     */
    boolean existsByUsuarioIdAndVotacaoId(Long usuarioId, Long votacaoId);

    /**
     *  Método para contar o número de votos associados a uma votação específica.
     * @param votacaoId
     * @return O número total de votos associados à votação especificada.
     */
    long countByVotacaoId(Long votacaoId); // conta votos de uma votacao

}