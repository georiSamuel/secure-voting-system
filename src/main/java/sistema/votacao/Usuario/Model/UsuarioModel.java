package sistema.votacao.Usuario.Model;

import java.time.LocalDate;

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

/**
 *  Classe resposável por criar a entidade de usuário dentro do BD
 * @author Horlan
 * @version 1.0
 * @since 20/05/2025
 */

@Entity
@Table(name = "tb_usuario")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UsuarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private long id;

    @Column(nullable = false)
    private String nome;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String senha;

    /*
     Mesmo o usuário só digitando os números do CPF é necessário deixar esse número como string
     se não os 0's à esquerda seriam perdidos (no caso do tipo ser Long ou Int) e  CPFs podem começar com zero
     */
    @Column(unique = true, nullable = false)
    private String cpf;

    @Column(name = "data_cadastro")
    private LocalDate dataCadastro;

    @Enumerated(EnumType.STRING) // Armazena o enum como String no banco de dados
    @Column(nullable = false)
    private TipoUsuario.Tipo tipoUsuario;

}
