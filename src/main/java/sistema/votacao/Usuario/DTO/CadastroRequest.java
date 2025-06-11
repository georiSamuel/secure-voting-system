package sistema.votacao.Usuario.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import sistema.votacao.Usuario.Model.TipoUsuario.Tipo;

@Getter
@Setter
@Data
public class CadastroRequest {
    private String nome;
    private String email;
    private String senha;
    private String cpf;
    private Tipo tipo;

    public CadastroRequest(String nome, String email, String senha, String cpf, Tipo tipo) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.cpf = cpf;
        this.tipo = tipo;
    }
}
