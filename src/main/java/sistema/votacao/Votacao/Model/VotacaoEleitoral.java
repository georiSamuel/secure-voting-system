package sistema.votacao.Votacao.Model;

import lombok.Data;
import sistema.votacao.Voto.Model.VotoModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    private boolean votoObrigatorio = true;
    private boolean permiteVotoEmBranco = true;
    private boolean votoSegundoTurno = false;

    public VotacaoEleitoral() {
        super();
    }

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

    @Override
    public String getDescricaoCargo() {
        return this.cargo != null ? this.cargo.getDescricao() : "Cargo Eleitoral Não Definido"; // Usa o método getDescricao do enum TipoCargoEleitoral
    }

}
