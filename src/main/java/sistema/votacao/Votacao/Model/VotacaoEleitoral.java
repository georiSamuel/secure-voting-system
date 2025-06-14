package sistema.votacao.Votacao.Model;

import lombok.Data;
import sistema.votacao.OpcaoVoto.Model.OpcaoVoto;
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
    public String gerarResultado() {
        long totalVotos = getVotos().size();
        long votosValidos = getOpcoes().stream()
                .mapToLong(OpcaoVoto::getQuantidadeVotos)
                .sum();
        long votosEmBranco = totalVotos - votosValidos;

        StringBuilder resultado = new StringBuilder();
        resultado.append("Resultado Eleitoral - ").append(getTitulo()).append("\n");
        resultado.append("Zona Eleitoral: ").append(zonaEleitoral).append("\n");
        resultado.append("Seção: ").append(secaoEleitoral).append("\n");
        resultado.append("Total de votantes: ").append(totalVotos).append("\n");
        resultado.append("Votos válidos: ").append(votosValidos).append("\n");
        resultado.append("Votos em branco: ").append(votosEmBranco).append("\n\n");

        resultado.append("Candidatos:\n");
        getOpcoes().stream()
                .sorted((o1, o2) -> Long.compare(o2.getQuantidadeVotos(), o1.getQuantidadeVotos()))
                .forEach(opcao -> {
                    double percentual = votosValidos > 0 ? (opcao.getQuantidadeVotos() * 100.0 / votosValidos) : 0;
                    resultado.append(String.format("- %s: %d votos (%.2f%%)%n",
                            opcao.getDescricao(),
                            opcao.getQuantidadeVotos(),
                            percentual));
                });

        // Segundo turno
        if (getOpcoes().size() >= 2) {
            OpcaoVoto primeiro = getOpcoes().get(0);
            OpcaoVoto segundo = getOpcoes().get(1);

            double diferencaPercentual = votosValidos > 0
                    ? ((primeiro.getQuantidadeVotos() - segundo.getQuantidadeVotos()) * 100.0 / votosValidos)
                    : 0;

            if (diferencaPercentual < 50.0) {
                resultado.append("\nATENÇÃO: Será necessário segundo turno!\n");
                this.votoSegundoTurno = true;
            }

            // Resultado de empate
            if (primeiro.getQuantidadeVotos() == segundo.getQuantidadeVotos()) {
                if (primeiro.getIdadeCandidato() > segundo.getIdadeCandidato()) {
                    resultado.append("\nEMPATE! Vencedor por desempate (candidato mais velho): ")
                            .append(primeiro.getDescricao()).append("\n");
                } else {
                    resultado.append("\nEMPATE! Vencedor por desempate (candidato mais velho): ")
                            .append(segundo.getDescricao()).append("\n");
                }
            }
        }

        return resultado.toString();
    }

    @Override
    public String getDescricaoCargo() {
        return this.cargo != null ? this.cargo.getDescricao() : "Cargo Eleitoral Não Definido"; // Usa o método getDescricao do enum TipoCargoEleitoral
    }

}
