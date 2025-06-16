package sistema.votacao.Usuario.Service;

/**
 * Classe que representa uma exceção lançada quando um tipo de usuário inválido é detectado.
 * @author Horlan
 * @version 1.0
 * @since 20/05/2025
 */

public class TipoUsuarioInvalido extends RuntimeException {

    /**
     * Construtor para inicializar a exceção com uma mensagem específica.
     * @param mensagem
     */
    public TipoUsuarioInvalido(String mensagem) {
        super(mensagem);
    }
}
