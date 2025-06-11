package sistema.votacao.Voto.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sistema.votacao.Voto.Model.VotoModel;

import java.util.List;

@Repository
public interface VotoRepository extends JpaRepository<VotoModel, Long> {

    List<VotoModel> findByVotacaoId(Long votacaoId); // voto da votacao

    List<VotoModel> findByUsuario_Id(Long usuarioId); // Correção: busca pelo ID do objeto 'usuario' dentro de VotoModel

    boolean existsByUsuarioIdAndVotacaoId(Long usuarioId, Long votacaoId);

    long countByVotacaoId(Long votacaoId); // conta votos de uma votacao

}