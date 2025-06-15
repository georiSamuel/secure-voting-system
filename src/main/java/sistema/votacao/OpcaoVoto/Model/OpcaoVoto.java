package sistema.votacao.OpcaoVoto.Model;

import jakarta.persistence.*;
import lombok.Data;
import sistema.votacao.Util.VoteCountEncryptor;
import sistema.votacao.Votacao.Model.Votacao;
import sistema.votacao.Voto.Model.VotoModel;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "tb_opcao_voto")

public class OpcaoVoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String descricao;

    // A MUDANÇA AQUI (Georis):
    @Convert(converter = VoteCountEncryptor.class) // INDICA QUAL CONVERSOR USAR
    @Lob // RECOMENDADO PARA CAMPOS DE DADOS GRANDES (COMO BYTES CRIPTOGRAFADOS)
    @Column(name = "quantidade_votos", nullable = false)
    private Long quantidadeVotos = 0L;
    // FIM DA MUDANÇA

    @ManyToOne
    @JoinColumn(name = "votacao_id", nullable = false)
    private Votacao votacao;

    @OneToMany(mappedBy = "opcaoVoto", cascade = CascadeType.ALL)
    private List<VotoModel> votos = new ArrayList<>();

    public OpcaoVoto() {
    }

    public OpcaoVoto(String descricao, Votacao votacao) {
        this.descricao = descricao;
        this.votacao = votacao;
    }

    public void incrementarVotos() {
        if (this.quantidadeVotos == null) {
            this.quantidadeVotos = 0L;
        }
        this.quantidadeVotos++;
    }
}