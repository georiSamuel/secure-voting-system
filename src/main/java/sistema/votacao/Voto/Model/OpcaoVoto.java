package sistema.votacao.Voto.Model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

import sistema.votacao.Votacao.Model.Votacao;

@Data
@Entity
@Table(name = "tb_opcao_voto")
public class OpcaoVoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String descricao;

    @Column(name = "quantidade_votos", nullable = false)
    private Long quantidadeVotos = 0L;

    @Column(name = "idade_candidato")
    private Integer idadeCandidato; // nova propriedade

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

    // Getters e setters para idadeCandidato, se horlan quiser fazer aql lombock
    // dele tudo bem
    public Integer getIdadeCandidato() {
        return idadeCandidato;
    }

    public void setIdadeCandidato(Integer idadeCandidato) {
        this.idadeCandidato = idadeCandidato;
    }
}
