package com.example.sistema_votacao.Usuario.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.sistema_votacao.Usuario.Model.TokenRecSenhaModel;
import com.example.sistema_votacao.Usuario.Model.UsuarioModel;
import com.example.sistema_votacao.Usuario.Repository.TokenRecSenhaRepository;
import com.example.sistema_votacao.Usuario.Repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public class RecuperacaoSenhaService {

    @Autowired
    private TokenRecSenhaRepository tokenRecSenhaRepo;

    @Autowired
    private UsuarioRepository  UsuarioRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public String gerarToken(String email){
        
        UsuarioModel Usuario = UsuarioRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        String Token = UUID.randomUUID().toString();
        LocalDateTime expiracao = LocalDateTime.now().plusMinutes(30);

        TokenRecSenhaModel entidade = new TokenRecSenhaModel();
        entidade.setToken(Token);
        entidade.setExpiracao(expiracao);
        entidade.setUsuario(Usuario);
        entidade.setUsado(false);

        tokenRecSenhaRepo.save(entidade);
        return Token;
    }

    public void redefinirSenha(String token, String novaSenha){

        TokenRecSenhaModel entidade = tokenRecSenhaRepo.findByToken(token).orElseThrow(() -> new RuntimeException("Token inválido ou expirado."));

        if(entidade.isUsado() || entidade.getExpiracao().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token inválido ou expirado.");
        }

        UsuarioModel Usuario = entidade.getUsuario();
        Usuario.setSenha(passwordEncoder.encode(Usuario.getSenha())); // Aqui você deve aplicar a lógica de hash da senha
        UsuarioRepo.save(Usuario);

        entidade.setUsado(true);

        tokenRecSenhaRepo.save(entidade);

    }
    
}
