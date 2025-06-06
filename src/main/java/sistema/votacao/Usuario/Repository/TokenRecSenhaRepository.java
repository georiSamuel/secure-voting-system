package sistema.votacao.Usuario.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import sistema.votacao.Usuario.Model.TokenRecSenhaModel;

public interface TokenRecSenhaRepository extends JpaRepository<TokenRecSenhaModel, Long> {

    Optional<TokenRecSenhaModel> findByToken(String token);
}