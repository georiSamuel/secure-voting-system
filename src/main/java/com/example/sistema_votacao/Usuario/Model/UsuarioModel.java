package com.example.sistema_votacao.Usuario.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "tb_usuario")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UsuarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nome;
    private String email;
    private String senha;

    @Enumerated(EnumType.STRING) // essa anotação diz ao banco de dados para salvar como um nome literal do enum, como ADMIN E COMUM;
    private TipoUsuario.Tipo tipo;    
}
