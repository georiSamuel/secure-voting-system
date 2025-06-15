package sistema.votacao.OpcaoVoto.Model;

import jakarta.persistence.*;
import lombok.Data;
import sistema.votacao.Util.VoteCountEncryptor;
import sistema.votacao.Votacao.Model.Votacao;
import sistema.votacao.Voto.Model.VotoModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa uma opção de voto em uma votação.
 * Cada opção de voto tem uma descrição, um número de votos e está associada a
 * uma votação específica.
 * A quantidade de votos é criptografada para segurança.
 * 
 * @author Lethycia
 * @version 1.0
 * @since 26/05/25
 */
@Data
@Entity
@Table(name = "tb_opcao_voto")
public class OpcaoVoto {

    /**
     * Identificador único da opção de voto.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Descrição da opção de voto.
     */
    @Column(nullable = false, length = 100)
    private String descricao;

    /**
     * Quantidade de votos recebidos, armazenada de forma criptografada.
     */
    @Convert(converter = VoteCountEncryptor.class)
    @Lob
    @Column(name = "quantidade_votos", nullable = false)
    private Long quantidadeVotos = 0L;

    /**
     * Votação à qual esta opção pertence.
     */
    @ManyToOne
    @JoinColumn(name = "votacao_id", nullable = false)
    private Votacao votacao;

    /**
     * Lista de votos associados a esta opção de voto.
     */
    @OneToMany(mappedBy = "opcaoVoto", cascade = CascadeType.ALL)
    private List<VotoModel> votos = new ArrayList<>();

    /**
     * Construtor padrão.
     */
    public OpcaoVoto() {
    }

    /**
     * Construtor com descrição e votação associada.
     */
    public OpcaoVoto(String descricao, Votacao votacao) {
        this.descricao = descricao;
        this.votacao = votacao;
    }

    /**
     * Incrementa a quantidade de votos em 1.
     * Garante que o valor não seja nulo antes de incrementar.
     */
    public void incrementarVotos() {
        if (this.quantidadeVotos == null) {
            this.quantidadeVotos = 0L;
        }
        this.quantidadeVotos++;
    }
}
