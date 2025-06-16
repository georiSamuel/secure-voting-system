package sistema.votacao.votacao.model;

import sistema.votacao.voto.model.VotoModel;
import jakarta.persistence.*;

/**
 * Classe que representa uma votação personalizada no sistema de votação.
 * Ela estende a classe Votacao e inclui um campo para permitir votos múltiplos.
 * 
 * @author Lethycia
 * @version 1.0
 * @since 26/05/25
 */
@Entity
@DiscriminatorValue("PERSONALIZADA")
public class VotacaoPersonalizada extends Votacao {

    private boolean permiteVotoMultiplo;

    /**
     * Valida se um voto é válido para esta votação personalizada.
     * Atualmente permite todos os votos sem restrição.
     * 
     * @param voto o voto a ser validado
     * @return sempre, indicando que o voto é válido
     */
    @Override
    public boolean validarVoto(VotoModel voto) {
        // todo - ver se precisa ainda
        return true;
    }

    /**
     * Retorna a descrição do cargo relacionado a esta votação personalizada.
     * Como essa votação não possui cargo associado, retorna uma descrição genérica.
     * 
     * @return uma string informando que não é aplicável
     */
    @Override
    public String getDescricaoCargo() {
        // Votações personalizadas não têm um "cargo" como acadêmicas ou eleitorais.
        // Você pode retornar uma string vazia, "N/A", "Não Aplicável", ou uma descrição
        // genérica.
        return "Não Aplicável"; // Ou "" ou "Votação Personalizada"
    }

    /**
     * Indica se esta votação permite votos múltiplos.
     * 
     * @return se permite voto múltiplo; caso contrário,
     */
    public boolean isPermiteVotoMultiplo() {
        return permiteVotoMultiplo;
    }

    /*
     * Define se esta votação permite votos múltiplos.
     * 
     * @param permiteVotoMultiplo para permitir voto múltiplo;
     */
    public void setPermiteVotoMultiplo(boolean permiteVotoMultiplo) {
        this.permiteVotoMultiplo = permiteVotoMultiplo;
    }
}
