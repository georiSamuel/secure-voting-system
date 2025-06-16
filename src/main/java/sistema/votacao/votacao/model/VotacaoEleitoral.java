package sistema.votacao.votacao.model;

import lombok.Data;
import sistema.votacao.voto.model.VotoModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

/**
 * Classe que representa uma votação eleitoral no sistema de votação.
 * Ela estende a classe Votacao e inclui campos específicos para o tipo de cargo
 * eleitoral,
 * zona eleitoral, seção eleitoral e regras de voto.
 * 
 * @author Lethycia
 * @version 1.0
 * @since 26/05/25
 */
@Data
@Entity
@DiscriminatorValue("ELEITORAL")

public class VotacaoEleitoral extends Votacao {

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, length = 50, columnDefinition = "VARCHAR(50)")
    private TipoCargoEleitoral cargo;

    @NotBlank(message = "Zona eleitoral é obrigatória")
    private String zonaEleitoral;

    @NotBlank(message = "Seção eleitoral é obrigatória")
    private String secaoEleitoral;

    private boolean permiteVotoEmBranco = true;

    /**
     * Construtor padrão da classe VotacaoEleitoral.
     */
    public VotacaoEleitoral() {
        super();
    }

    /**
     * Valida se um voto é válido para esta votação eleitoral.
     * Verifica se a votação está ativa, se o eleitor já votou e se o voto em branco
     * é permitido.
     * 
     * @param voto o voto a ser validado
     * @return true se o voto for válido
     * @throws IllegalStateException caso a votação esteja encerrada,
     *                               o eleitor já tenha votado ou voto em branco não
     *                               seja permitido
     */
    @Override
    public boolean validarVoto(VotoModel voto) {
        if (!isAtiva()) {
            throw new IllegalStateException("Votação encerrada");
        }

        if (voto.getUsuario() != null) {
            boolean jaVotou = getVotos().stream()
                    .anyMatch(v -> v.getUsuario() != null &&
                            v.getUsuario().getId() == voto.getUsuario().getId());
            if (jaVotou) {
                throw new IllegalStateException("Eleitor já votou nesta votação");
            }
        }

        if (voto.getOpcaoVoto() == null && !permiteVotoEmBranco) {
            throw new IllegalStateException("Voto em branco não permitido nesta votação");
        }

        return true;
    }

    /*
     * Retorna a descrição do cargo eleitoral relacionado a esta votação.
     * 
     * @return a descrição do cargo eleitoral ou "Cargo Eleitoral Não Definido" caso
     *         não esteja definido
     */
    @Override
    public String getDescricaoCargo() {
        return this.cargo != null ? this.cargo.getDescricao() : "Cargo Eleitoral Não Definido"; // Usa o método
                                                                                                // getDescricao do enum
                                                                                                // TipoCargoEleitoral
    }

}