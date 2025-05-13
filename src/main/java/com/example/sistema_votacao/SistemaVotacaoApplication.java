package com.example.sistema_votacao;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.sistema_votacao.modelo.Usuario;

@SpringBootApplication
public class SistemaVotacaoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemaVotacaoApplication.class, args);
	}

}

@Bean
CommandLineRunner init(UsuarioRepository usuarioRepo) {
    return args -> {
        if (usuarioRepo.findByCpf("123.456.789-00") == null) {
            Usuario admin = new Usuario();
            admin.setCpf("123.456.789-00");
            admin.setNome("Admin");
            admin.setSenha(BCrypt.hashpw("senha123", BCrypt.gensalt())); // criar classe ou sla, n sei usar bcrypt direito ainda
            admin.setDataCadastro(LocalDateTime.now());
            
            usuarioRepo.save(admin);
            System.out.println("✅ Admin criado com sucesso");
        } else {
            System.out.println("⚠️ Admin já existe");
        }
    };
}
