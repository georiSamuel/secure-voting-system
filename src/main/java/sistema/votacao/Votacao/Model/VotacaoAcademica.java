package sistema.votacao.Votacao.Model;

import lombok.Data;
import sistema.votacao.Voto.Model.OpcaoVoto;
import sistema.votacao.Voto.Model.VotoModel;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.*;

/**
 * VotacaoAcademica.java
 * Esta classe representa uma votação acadêmica no sistema de votação.
 */
@Data
@Entity
@DiscriminatorValue("ACADEMICA")
public class VotacaoAcademica extends Votacao {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50, columnDefinition = "VARCHAR(50)")
    private TipoCargoAcademico cargo;

    public VotacaoAcademica() {
        super();
    }

    @Override
    public boolean validarVoto(VotoModel voto) {
        if (!isAtiva()) {
            throw new IllegalStateException("Votação encerrada");
        }

        boolean jaVotou = getVotos().stream()
                .anyMatch(v -> v.getUsuario() != null &&
                        v.getUsuario().getId() == voto.getUsuario().getId());
        if (jaVotou) {
            throw new IllegalStateException("Usuário já votou nesta votação");
        }

        return true;
    }

    /**
     * Gera o resultado da votação acadêmica, incluindo total de votos, votos
     * válidos,
     * votos em branco e a lista de opções com suas respectivas quantidades de
     * votos.
     *
     * @return String formatada com o resultado da votação
     */
    @Override
    public String gerarResultado() {
        long total = this.getVotos().size();
        long validos = this.getOpcoes().stream().mapToLong(OpcaoVoto::getQuantidadeVotos).sum();
        long brancos = total - validos;

        StringBuilder r = new StringBuilder();
        r.append("Resultado da Votação Acadêmica: ").append(this.getTitulo()).append("\n");
        r.append("Cargo: ").append(this.cargo).append("\n");
        r.append("Total de votos: ").append(total).append("\n");
        r.append("Votos válidos: ").append(validos).append("\n");
        r.append("Votos em branco: ").append(brancos).append("\n\n");

        List<OpcaoVoto> opcoesOrdenadas = this.getOpcoes().stream()
                .sorted((a, b) -> {
                    int cmpVotos = b.getQuantidadeVotos().compareTo(a.getQuantidadeVotos());
                    if (cmpVotos != 0)
                        return cmpVotos;

                    return Integer.compare(b.getIdadeCandidato(), a.getIdadeCandidato());
                })
                .collect(Collectors.toList());

        opcoesOrdenadas.forEach(op -> r.append(String.format("- %s: %d votos (Idade: %d)\n",
                op.getDescricao(),
                op.getQuantidadeVotos(),
                op.getIdadeCandidato())));

        if (opcoesOrdenadas.size() >= 2) {
            OpcaoVoto primeiro = opcoesOrdenadas.get(0);
            OpcaoVoto segundo = opcoesOrdenadas.get(1);

            if (primeiro.getQuantidadeVotos() == segundo.getQuantidadeVotos()) {
                r.append("\nEMPATE DETECTADO!\n");
                r.append("Critério de desempate: candidato mais velho\n");
                r.append(String.format("Vencedor: %s (Idade: %d)\n",
                        primeiro.getDescricao(),
                        primeiro.getIdadeCandidato()));
            } else {
                r.append("\nVencedor: ").append(primeiro.getDescricao()).append("\n");
            }
        }

        return r.toString();
    }

    public TipoCargoAcademico getCargo() {
        return cargo;
    }

    public void setCargo(TipoCargoAcademico cargo) {
        this.cargo = cargo;
    }
}
