package sistema.votacao.Votacao.Model;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import sistema.votacao.OpcaoVoto.Model.OpcaoVoto;
import sistema.votacao.Voto.Model.VotoModel;

@Data
@Entity
@Table(name = "tb_votacao")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_votacao", discriminatorType = DiscriminatorType.STRING)
public abstract class Votacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private Timestamp inicio;
    private Timestamp fim;

    @OneToMany(mappedBy = "votacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OpcaoVoto> opcoes = new ArrayList<>();

    @OneToMany(mappedBy = "votacao")
    private List<VotoModel> votos = new ArrayList<>();

    public abstract boolean validarVoto(VotoModel voto);

    public abstract String getDescricaoCargo(); // Método para resolver problema de enum's sendo sobrescritos ou mal interpretados devido à forma como o mapeamento JPA está lidando com a herança e a coluna de cargo único.

    public boolean isAtiva() {
        Timestamp agora = new Timestamp(System.currentTimeMillis());
        return inicio.before(agora) && fim.after(agora);
    }

}