package com.example.sistema_votacao.Usuario.Service;

public class TipoUsuarioInvalido extends RuntimeException {

    public TipoUsuarioInvalido(String email) {
        super("Tipo inválido de email para os tipos de usuários(ADMIN ou COMUM): " + email);
    }

}
