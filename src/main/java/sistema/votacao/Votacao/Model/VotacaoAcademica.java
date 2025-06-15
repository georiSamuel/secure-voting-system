package sistema.votacao.Votacao.Model;

import lombok.Data;
import sistema.votacao.Voto.Model.VotoModel;
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
    @Column(nullable = true, length = 50, columnDefinition = "VARCHAR(50)")
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
    public String getDescricaoCargo() {
        return this.cargo != null ? this.cargo.name() : "Cargo Acadêmico Não Definido"; // Retorna o nome do enum como string
    }


    public void setCargo(TipoCargoAcademico cargo) {
        this.cargo = cargo;
    }
}
