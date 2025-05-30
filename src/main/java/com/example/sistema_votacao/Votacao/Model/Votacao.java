package com.example.sistema_votacao.Votacao.Model;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.example.sistema_votacao.Voto.Model.VotoModel;

@Entity
@Table(name = "tb_votacao")
public class Votacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private Timestamp inicio;
    private Timestamp fim;

    @OneToMany(mappedBy = "votacao")
    private List<VotoModel> votos = new ArrayList<>();

    // Construtores
    public Votacao() {
    }

    public Votacao(Long id, String titulo, Timestamp inicio, Timestamp fim) {
        this.id = id;
        this.titulo = titulo;
        this.inicio = inicio;
        this.fim = fim;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Timestamp getInicio() {
        return inicio;
    }

    public void setInicio(Timestamp inicio) {
        this.inicio = inicio;
    }

    public Timestamp getFim() {
        return fim;
    }

    public void setFim(Timestamp fim) {
        this.fim = fim;
    }

    public List<VotoModel> getVotos() {
        return votos;
    }

    public void setVotos(List<VotoModel> votos) {
        this.votos = votos;
    }
}