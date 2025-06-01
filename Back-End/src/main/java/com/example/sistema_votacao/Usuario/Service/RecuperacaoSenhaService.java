package com.example.sistema_votacao.Usuario.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.sistema_votacao.Usuario.Model.TokenRecSenhaModel;
import com.example.sistema_votacao.Usuario.Model.UsuarioModel;
import com.example.sistema_votacao.Usuario.Repository.TokenRecSenhaRepository;
import com.example.sistema_votacao.Usuario.Repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RecuperacaoSenhaService {

    @Autowired
    private TokenRecSenhaRepository tokenRecSenhaRepo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public String gerarToken(String email) {
        UsuarioModel usuario = usuarioRepo.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        
        String token = UUID.randomUUID().toString();
        LocalDateTime expiracao = LocalDateTime.now().plusMinutes(30);

        TokenRecSenhaModel entidade = new TokenRecSenhaModel();
        entidade.setToken(token);
        entidade.setExpiracao(expiracao);
        entidade.setUsuario(usuario);
        entidade.setUsado(false);

        tokenRecSenhaRepo.save(entidade);
        return token;
    }

    public void redefinirSenha(String token, String novaSenha) {
        TokenRecSenhaModel entidade = tokenRecSenhaRepo.findByToken(token)
            .orElseThrow(() -> new RuntimeException("Token inválido ou expirado."));

        if (entidade.isUsado() || entidade.getExpiracao().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token inválido ou expirado.");
        }

        UsuarioModel usuario = entidade.getUsuario();
        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuarioRepo.save(usuario);

        entidade.setUsado(true);
        tokenRecSenhaRepo.save(entidade);
    }
}


