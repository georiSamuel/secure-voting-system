package com.example.sistema_votacao.modelo;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String votoCriptografado;

    private Timestamp horario;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "votacao_id")
    private Votacao votacao;

    public Voto() {
    }

    public Voto(String votoCriptografado, Timestamp horario, Usuario usuario, Votacao votacao) {
        this.votoCriptografado = votoCriptografado;
        this.horario = horario;
        this.usuario = usuario;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Votacao getVotacao() {
        return votacao;
    }

    public void setVotacao(Votacao votacao) {
        this.votacao = votacao;
    }
}
