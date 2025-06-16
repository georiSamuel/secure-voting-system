package sistema.votacao.usuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sistema.votacao.usuario.model.TipoUsuario.Tipo;
import sistema.votacao.usuario.model.UsuarioModel;

import java.util.Optional;

/**
 * Interface responsável por definir os métodos de acesso aos dados de usuários no banco de dados.
 * @author Horlan
 * @version 1.0
 * @since 20/05/2025
 */
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {

    /**
     * Método para encontrar um usuário pelo seu email.
     * @param email O email do usuário a ser encontrado.
     * @return Um Optional contendo o usuário se encontrado, ou vazio caso contrário.
     */
    Optional<UsuarioModel> findByEmail(String email);

    /**
     * Método para encontrar um usuário pelo seu cpf.
     * @param cpf O cpf do usuário a ser encontrado.
     * @return Um Optional contendo o usuário se encontrado, ou vazio caso contrário.
     */
    Optional<UsuarioModel> findByCpf(String cpf);

    /**
     * Método para verificar se já existe um usuário com o tipo especificado.
     * O nome do método foi atualizado para corresponder ao nome da propriedade 'tipoUsuario' em UsuarioModel.
     * @param tipo O tipo de usuário a ser verificado.
     * @return true se existir um usuário com o tipo especificado, false caso contrário.
     */
    boolean existsByTipoUsuario(Tipo tipo);

    // Optional<UsuarioModel> findById(Long id); // já existe no JpaRepository,
    // adicionei para apresentar o código completo
}
