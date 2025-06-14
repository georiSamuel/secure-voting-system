package sistema.votacao.Usuario.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import sistema.votacao.Usuario.Model.TipoUsuario.Tipo;

/**
 * Classe de tansferência de dados (DTO) para o cadastro de usuários no sistema de votação.
 * @author Horlan
 * @version 1.0
 * @since 20/05/2025
 */

@Getter
@Setter
@Data
public class CadastroRequest {
    private String nome;
    private String email;
    private String senha;
    private String cpf;
    private Tipo tipo;
}
