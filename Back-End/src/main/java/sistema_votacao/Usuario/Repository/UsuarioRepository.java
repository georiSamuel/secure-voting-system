package sistema_votacao.Usuario.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sistema_votacao.Usuario.Model.TipoUsuario.Tipo;
import sistema_votacao.Usuario.Model.UsuarioModel;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {

    Optional<UsuarioModel> findByEmail(String email);

    boolean existsByTipo(Tipo tipo); // Verifica se já existe um usuário com o tipo especificado, ele vai ser usado
                                     // para verificar se já existe um administrador cadastrado no sistema.
    // Optional<UsuarioModel> findById(Long id); // já existe no JpaRepository,
    // adicionei para apresentar o código completo
}
