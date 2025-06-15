package sistema.votacao.Votacao.Model;

import lombok.Data;
import sistema.votacao.Voto.Model.VotoModel;
import jakarta.persistence.*;

/**
 * VotacaoAcademica.java
 * Esta classe representa uma votação acadêmica no sistema de votação.
 * Ela estende a classe Votacao e inclui um campo para o tipo de cargo acadêmico
 * em disputa.
 * 
 * @author Lethycia
 * @version 1.0
 * @since 26/05/25
 */
@Data
@Entity
@DiscriminatorValue("ACADEMICA")
public class VotacaoAcademica extends Votacao {

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, length = 50, columnDefinition = "VARCHAR(50)")
    private TipoCargoAcademico cargo;

    /**
     * Construtor padrão da classe VotacaoAcademica.
     */
    public VotacaoAcademica() {
        super();
    }

    /**
     * Valida se um voto é válido para esta votação acadêmica.
     * Verifica se a votação está ativa e se o usuário ainda não votou.
     * 
     * @param voto o voto a ser validado
     * @return true se o voto for válido
     * @throws IllegalStateException se a votação estiver encerrada ou se o usuário
     *                               já tiver votado
     */
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
     * Retorna a descrição do cargo acadêmico relacionado a esta votação.
     * 
     * @return o nome do cargo acadêmico ou "Cargo Acadêmico Não Definido" caso não
     *         esteja definido
     */
    @Override
    public String getDescricaoCargo() {
        return this.cargo != null ? this.cargo.name() : "Cargo Acadêmico Não Definido";
    }

    /**
     * Define o cargo acadêmico para esta votação.
     * 
     * @param cargo o tipo de cargo acadêmico
     */
    public void setCargo(TipoCargoAcademico cargo) {
        this.cargo = cargo;
    }
}
