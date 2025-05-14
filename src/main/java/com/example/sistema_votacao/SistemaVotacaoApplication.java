package com.example.sistema_votacao;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.sistema_votacao.Repository.UsuarioRepository;
import com.example.sistema_votacao.modelo.Usuario;

import org.mindrot.jbcrypt.BCrypt;

@SpringBootApplication
public class SistemaVotacaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SistemaVotacaoApplication.class, args);
    }

    @Bean
    CommandLineRunner init(UsuarioRepository usuarioRepo) {
        return args -> {
            if (usuarioRepo.findByCpf("122.709.134-00") == null) {
                Usuario admin = new Usuario();
                admin.setCpf("122.709.134-00");
                admin.setNome("Administrador");
                admin.setSenha(BCrypt.hashpw("senha123", BCrypt.gensalt())); // ajeitar o bcrypt dps
                admin.setDataCadastro(LocalDateTime.now());

                usuarioRepo.save(admin);
                System.out.println("Cadastro criado com sucesso");
            } else {
                System.out.println("Cadastro já existente");
            }
        };
    }
}
