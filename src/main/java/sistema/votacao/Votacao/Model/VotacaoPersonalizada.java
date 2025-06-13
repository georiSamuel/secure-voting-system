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
    public String gerarResultado() {
        //todo - ver se precisa ainda
        return "Resultado da votação personalizada: Utilize o serviço de votação para obter o total de votos.";
    }

    // Getters e Setters
    public boolean isPermiteVotoMultiplo() {
        return permiteVotoMultiplo;
    }

    public void setPermiteVotoMultiplo(boolean permiteVotoMultiplo) {
        this.permiteVotoMultiplo = permiteVotoMultiplo;
    }
}
