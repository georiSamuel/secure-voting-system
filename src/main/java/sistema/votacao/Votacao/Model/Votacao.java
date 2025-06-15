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

/**
 * Classe abstrata que representa uma votação genérica.
 * Contém informações comuns a todas as votações, como título, período de início
 * e fim,
 * lista de opções de voto e votos associados.
 * 
 * @author Lethycia
 * @version 1.0
 * @since 26/05/25
 */

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

    /**
     * Método abstrato que define a regra de validação para o voto
     * com base no tipo específico de votação.
     *
     * @param voto o voto a ser validado
     * @return {@code true} se o voto for válido, {@code false} caso contrário
     */
    public abstract boolean validarVoto(VotoModel voto);

    /**
     * Método abstrato usado para retornar a descrição do cargo,
     * sendo necessário para evitar conflitos no mapeamento da enum nas subclasses.
     *
     * @return descrição do cargo relacionado à votação
     */
    public abstract String getDescricaoCargo();

    /**
     * Verifica se a votação está atualmente ativa com base nas datas de início e
     * fim.
     *
     * @return {@code true} se a votação estiver ativa, {@code false} caso contrário
     */
    public boolean isAtiva() {
        Timestamp agora = new Timestamp(System.currentTimeMillis());
        return inicio.before(agora) && fim.after(agora);
    }

}