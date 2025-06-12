package sistema.votacao.Usuario.Model;

/**
 * Classe que define os tipos de usuário no sistema de votação.
 * @author Horlan
 * @version 1.0
 * @since 20/05/2025
 */
public class TipoUsuario {

    /**
     * Enum que representa os tipos de usuário.
     */
    public enum Tipo {

        /**
         * Representa um usuário com privilégios administrativos.
         */
        ADMIN,
        
        /**
         * Representa um usuário comum, sem privilégios administrativos.
         */
        COMUM
    }

}
