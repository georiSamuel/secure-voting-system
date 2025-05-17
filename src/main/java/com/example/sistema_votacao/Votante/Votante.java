package com.example.sistema_votacao.Votante;

import com.example.sistema_votacao.Voto.Voto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
public class Votante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 11)
    private String cpf;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false)
    private String senha;

    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;

    @Column(name = "ja_votou", nullable = false)
    private boolean jaVotou = false;

    @OneToMany(mappedBy = "votante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Voto> votos = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.dataCadastro = LocalDateTime.now();
    }

    public void adicionarVoto(Voto voto) {
        votos.add(voto);
        voto.setVotante(this);
    }

    public void removerVoto(Voto voto) {
        votos.remove(voto);
        voto.setVotante(null);
    }

    public void criptografarSenha() {
        // implementar criptografia
    }
}
