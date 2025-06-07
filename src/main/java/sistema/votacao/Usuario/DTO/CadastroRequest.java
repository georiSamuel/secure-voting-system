package sistema.votacao.Usuario.DTO;

import lombok.Data;

@Data
public class CadastroRequest {
    private String nome;
    private String email;
    private String senha;
    private String cpf;
    private String tipo; // p/ definir o tipo de usuário no cadastro (ex: "COMUM")
}