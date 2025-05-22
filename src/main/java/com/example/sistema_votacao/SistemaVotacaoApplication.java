package com.example.sistema_votacao;

import com.example.sistema_votacao.Usuario.Model.UsuarioModel;
import com.example.sistema_votacao.Usuario.Repository.UsuarioRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.example.sistema_votacao.Usuario.Model.TipoUsuario;

import java.time.LocalDateTime;

@SpringBootApplication
public class SistemaVotacaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SistemaVotacaoApplication.class, args);
    }

    @Bean
    CommandLineRunner initAdminUser(UsuarioRepository usuarioRepository) {
        return args -> {
            final String ADMIN_EMAIL = "admin@sistema.votacao";
            final String ADMIN_SENHA = "SenhaAdmin@123";

            if (usuarioRepository.findByEmail(ADMIN_EMAIL).isEmpty()) {
                UsuarioModel admin = new UsuarioModel();
                admin.setNome("Administrador do Sistema");
                admin.setEmail(ADMIN_EMAIL);
                admin.setSenha(BCrypt.hashpw(ADMIN_SENHA, BCrypt.gensalt()));
                admin.setDataCadastro(LocalDateTime.now());
                admin.setTipo(TipoUsuario.Tipo.ADMIN);
                admin.setCpf("00000000000");
                admin.setJaVotou(false);

                usuarioRepository.save(admin);
                System.out.println("Cadastro criado com sucesso.");
                System.out.println("Email: " + ADMIN_EMAIL);
                System.out.println("Senha: " + ADMIN_SENHA);
            } else {
                System.out.println("O cadastro já existe no sistema.");
            }
        };
    }
}