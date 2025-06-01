package com.example.sistema_votacao.Votacao.Model;

import com.example.sistema_votacao.Voto.Model.VotoModel;
import com.example.sistema_votacao.Voto.Repository.VotoRepository;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
@DiscriminatorValue("PERSONALIZADA")
public class VotacaoPersonalizada extends Votacao {

    @Transient // Indica que não é para persistir no banco
    @Autowired
    private transient VotoRepository votoRepository;

    private boolean permiteVotoMultiplo;

    @Override
    public boolean validarVoto(VotoModel voto) {
        if (!permiteVotoMultiplo && voto.getUsuario() != null) {
            return !votoRepository.existsByUsuarioIdAndVotacaoId(
                    voto.getUsuario().getId(),
                    this.getId());
        }
        return true;
    }

    @Override
    public String gerarResultado() {
        return "Resultado da votação personalizada - Total de votos: " +
                (votoRepository != null ? votoRepository.countByVotacaoId(this.getId()) : "N/A");
    }

    // Getters e Setters
    public boolean isPermiteVotoMultiplo() {
        return permiteVotoMultiplo;
    }

    public void setPermiteVotoMultiplo(boolean permiteVotoMultiplo) {
        this.permiteVotoMultiplo = permiteVotoMultiplo;
    }
}