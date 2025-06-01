package sistema_votacao.Votacao.Model;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import sistema_votacao.Voto.Model.OpcaoVoto;
import sistema_votacao.Voto.Model.VotoModel;

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

    // Métodos abstratos que subclasses implementarão
    public abstract boolean validarVoto(VotoModel voto);

    public abstract String gerarResultado();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Timestamp getInicio() {
        return inicio;
    }

    public void setInicio(Timestamp inicio) {
        this.inicio = inicio;
    }

    public Timestamp getFim() {
        return fim;
    }

    public void setFim(Timestamp fim) {
        this.fim = fim;
    }

    public List<OpcaoVoto> getOpcoes() {
        return opcoes;
    }

    public List<VotoModel> getVotos() {
        return votos;
    }

    public void setOpcoes(List<OpcaoVoto> opcoes) {
        this.opcoes = opcoes;
    }

    public void setVotos(List<VotoModel> votos) {
        this.votos = votos;
    }

    public boolean isAtiva() {
        Timestamp agora = new Timestamp(System.currentTimeMillis());
        return inicio.before(agora) && fim.after(agora);
    }

}