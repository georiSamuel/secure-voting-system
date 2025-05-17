package com.example.sistema_votacao.Model;

import jakarta.persistence.*;
import java.sql.Timestamp;

//dependendo de votante e votacao
@Entity
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String votoCriptografado;

    private Timestamp horario;

    @ManyToOne
    @JoinColumn(name = "votante_id")
    private Votante votante;

    @ManyToOne
    @JoinColumn(name = "votacao_id")
    private Votacao votacao;

    public Voto() {
    }

    public Voto(String votoCriptografado, Timestamp horario, Votante votante, Votacao votacao) {
        this.votoCriptografado = votoCriptografado;
        this.horario = horario;
        this.votante = votante;
        this.votacao = votacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVotoCriptografado() {
        return votoCriptografado;
    }

    public void setVotoCriptografado(String votoCriptografado) {
        this.votoCriptografado = votoCriptografado;
    }

    public Timestamp getHorario() {
        return horario;
    }

    public void setHorario(Timestamp horario) {
        this.horario = horario;
    }

    public Votante getVotante() {
        return votante;
    }

    public void setVotante(Votante votante) {
        this.votante = votante;
    }

    public Votacao getVotacao() {
        return votacao;
    }

    public void setVotacao(Votacao votacao) {
        this.votacao = votacao;
    }
}
