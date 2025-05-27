package com.example.sistema_votacao.Usuario.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_token_rec_senha")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TokenRecSenhaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String token;

    private LocalDateTime expiracao;

    @ManyToOne // usuario pode ter vários tokens, mas o token pertence a um único usuário
    private UsuarioModel usuario;

    private boolean usado = false;

}
