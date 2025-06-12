package sistema.votacao.Usuario.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe de transferência de dados (DTO) para o login de usuários no sistema de votação.
 * @author Horlan
 * @version 1.0
 * @since 20/05/2025
 */

@Getter
@Setter
@Data
public class LoginRequest {

    private String email;
    private String senha;

    /**
     * Construtor para inicializar os campos necessários para o login de um usuário.
     * @param email 
     * @param senha 
     */
    public LoginRequest(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

}
