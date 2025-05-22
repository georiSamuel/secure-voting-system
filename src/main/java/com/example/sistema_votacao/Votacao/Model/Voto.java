package com.example.sistema_votacao.Votacao.Model;

import jakarta.persistence.*;
import java.sql.Timestamp;

import com.example.sistema_votacao.Usuario.Model.UsuarioModel;

@Entity
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String votoCriptografado;

    private Timestamp horario;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private UsuarioModel usuariomodel;

    @ManyToOne
    @JoinColumn(name = "votacao_id")
    private Votacao votacao;

    public Voto() {
    }

    public Voto(String votoCriptografado, Timestamp horario, UsuarioModel usuarioModel, Votacao votacao) {
        this.votoCriptografado = votoCriptografado;
        this.horario = horario;
        this.usuariomodel = usuarioModel;
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

    public UsuarioModel getUsuario() {
        return usuariomodel;
    }

    public void setUsuario(UsuarioModel usuarioModel) {
        this.usuariomodel = usuarioModel;
    }

    public Votacao getVotacao() {
        return votacao;
    }

    public void setVotacao(Votacao votacao) {
        this.votacao = votacao;
    }
}
