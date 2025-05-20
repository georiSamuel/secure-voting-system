package com.example.sistema_votacao.Usuario.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.yaml.snakeyaml.events.Event.ID;

import com.example.sistema_votacao.Usuario.Model.UsuarioModel;
import com.example.sistema_votacao.Usuario.Model.TipoUsuario.Tipo;
import com.example.sistema_votacao.Usuario.Repository.UsuarioRepository;

import jakarta.persistence.Id;

import java.util.Optional;

public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public UsuarioModel cadastrarUsuario(UsuarioModel usuario){

        String email = usuario.getEmail();
        
        if(email.endsWith("@admin")){
            usuario.setTipo(Tipo.ADMIN);
        }
        else if(email.endsWith("@usuario")){
            usuario.setTipo(Tipo.COMUM);
        }
        else{
            throw new TipoUsuarioInvalido(email);
        }

        return usuarioRepository.save(usuario);
    }

    public Optional <UsuarioModel> buscarPorId(Long id){
        return Optional.ofNullable(usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario não encontrado")));
    }
    
    public UsuarioModel autenticar(String email, String senha){

        Optional<UsuarioModel> usuarioOptional = usuarioRepository.findByEmail(email);

        if(usuarioOptional.isPresent()){
            UsuarioModel usuario = usuarioOptional.get();

            if(usuario.getSenha().equals(senha)){
                return usuario;
            }
        }

        return null;
        
    }
}
