package com.example.sistema_votacao.Votacao.Model;

import com.example.sistema_votacao.Voto.Model.OpcaoVoto;
import com.example.sistema_votacao.Voto.Model.VotoModel;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("ACADEMICA")
public class VotacaoAcademica extends Votacao {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
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

    @Override
    public String gerarResultado() {
        long total = getVotos().size();
        long validos = getOpcoes().stream().mapToLong(OpcaoVoto::getQuantidadeVotos).sum();
        long brancos = total - validos;

        StringBuilder r = new StringBuilder();
        r.append("Resultado da Votação Acadêmica: ").append(getTitulo()).append("\n");
        r.append("Cargo: ").append(cargo).append("\n");
        r.append("Total de votos: ").append(total).append("\n");
        r.append("Votos válidos: ").append(validos).append("\n");
        r.append("Votos em branco: ").append(brancos).append("\n\n");

        getOpcoes().stream()
                .sorted((a, b) -> b.getQuantidadeVotos().compareTo(a.getQuantidadeVotos()))
                .forEach(op -> r.append(String.format("- %s: %d votos\n", op.getDescricao(), op.getQuantidadeVotos())));

        return r.toString();
    }

    public TipoCargoAcademico getCargo() {
        return cargo;
    }

    public void setCargo(TipoCargoAcademico cargo) {
        this.cargo = cargo;
    }
}
