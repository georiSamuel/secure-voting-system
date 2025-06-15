package sistema.votacao.Votacao.Model;

import sistema.votacao.Voto.Model.VotoModel;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("PERSONALIZADA")
public class VotacaoPersonalizada extends Votacao {

    private boolean permiteVotoMultiplo;

    @Override
    public boolean validarVoto(VotoModel voto) {
        //todo - ver se precisa ainda
        return true;
    }


    @Override
    public String getDescricaoCargo() {
        // Votações personalizadas não têm um "cargo" como acadêmicas ou eleitorais.
        // Você pode retornar uma string vazia, "N/A", "Não Aplicável", ou uma descrição genérica.
        return "Não Aplicável"; // Ou "" ou "Votação Personalizada"
    }

    // Getters e Setters
    public boolean isPermiteVotoMultiplo() {
        return permiteVotoMultiplo;
    }

    public void setPermiteVotoMultiplo(boolean permiteVotoMultiplo) {
        this.permiteVotoMultiplo = permiteVotoMultiplo;
    }
}
