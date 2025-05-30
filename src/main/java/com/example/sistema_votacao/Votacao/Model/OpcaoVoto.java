package com.example.sistema_votacao.Votacao.Model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

import com.example.sistema_votacao.Voto.Model.VotoModel;

@Data
@Entity
@Table(name = "tb_opcao_voto")
public class OpcaoVoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String descricao;

    @Column(name = "quantidade_votos", nullable = false)
    private Long quantidadeVotos = 0L;

    @ManyToOne
    @JoinColumn(name = "votacao_id", nullable = false)
    private Votacao votacao;

    @OneToMany(mappedBy = "opcaoVoto", cascade = CascadeType.ALL)
    private List<VotoModel> votos = new ArrayList<>();

    // Construtores
    public OpcaoVoto() {
    }

    public OpcaoVoto(String descricao, Votacao votacao) {
        this.descricao = descricao;
        this.votacao = votacao;
    }

    // Método para incrementar votos
    public void incrementarVotos() {
        if (this.quantidadeVotos == null) {
            this.quantidadeVotos = 0L;
        }
        this.quantidadeVotos++;
    }
}