package sistema.votacao.Usuario.Model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_usuario")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UsuarioModel {

    @Enumerated(EnumType.STRING) // Armazena o enum como String no banco de dados
    @Column(nullable = false)
    private TipoUsuario.Tipo tipoUsuario;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private long id;

    @Column(nullable = false)
    private String nome;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(name = "ja_votou")
    private boolean jaVotou = false; // coloquei isso aqui para o usuario não poder votar mais de uma vez

    @Enumerated(EnumType.STRING) // essa anotação diz ao banco de dados para salvar como um nome literal do
                                 // enum,como ADMIN E COMUM;
    private TipoUsuario.Tipo tipo;

    @Column(unique = true, nullable = false)
    private String cpf;

    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;

    @Column(name = "pergunta_seguranca")
    private String perguntaSeguranca;

    public UsuarioModel(String nome, String email, String senha, String cpf, TipoUsuario.Tipo tipoUsuario) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.cpf = cpf;
        this.tipoUsuario = tipoUsuario;
    }

}
