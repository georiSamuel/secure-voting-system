package com.example.sistema_votacao.Usuario.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.yaml.snakeyaml.events.Event.ID;

import com.example.sistema_votacao.Usuario.Model.UsuarioModel;
import com.example.sistema_votacao.Usuario.Model.TipoUsuario.Tipo;
import com.example.sistema_votacao.Usuario.Repository.UsuarioRepository;
import com.example.sistema_votacao.Votacao.Repository.VotoRepository;

import jakarta.persistence.Id;

import java.util.Optional;

public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    private VotoRepository votoRepository; // eu coloquei isso aqui tbm
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
            VotoRepository votoRepository,
            BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.votoRepository = votoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioModel cadastrarUsuario(UsuarioModel usuario) {

        String email = usuario.getEmail();

        if (email.endsWith("@admin.com")) {
            usuario.setTipo(Tipo.ADMIN);
        } else if (email.endsWith("@usuario.com")) {
            usuario.setTipo(Tipo.COMUM);
        } else {
            throw new TipoUsuarioInvalido(email);
        }
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    public Optional<UsuarioModel> buscarPorId(Long id) {
        return Optional.ofNullable(
                usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario não encontrado")));
    }

    public UsuarioModel autenticar(String email, String senha) {

        Optional<UsuarioModel> usuarioOptional = usuarioRepository.findByEmail(email);

        if (usuarioOptional.isPresent()) {
            UsuarioModel usuario = usuarioOptional.get();

            if (usuario.getSenha().equals(senha)) {
                return usuario;
            }
        }

        return null;

    }

    // horlan, tem problema eu colocar isso aqui?
    public boolean verificarSeUsuarioJaVotou(Long usuarioId, Long votacaoId) {
        return votoRepository.existsByUsuarioIdAndVotacaoId(usuarioId, votacaoId); // Verifica se o voto existe
    }

    public void atualizarStatusVotacao(Long usuarioId, boolean status) {
        UsuarioModel usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário com ID " + usuarioId + " não foi encontrado"));
        usuario.setJaVotou(status); // Atualiza o status de já votou
        usuarioRepository.save(usuario);
    }

    public void atualizarStatusVoto(Long usuarioId, boolean status) {
        UsuarioModel usuario = this.buscarPorId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado")); // Verifica se o usuário existe
        usuario.setJaVotou(status);
        usuarioRepository.save(usuario);
    }
}
