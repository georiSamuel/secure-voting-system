package sistema.votacao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Aplicação do SpringBoot, inicializa para rodar em segundo plano
 * @author Suelle
 * @version 1.0
 * @since 26/05/25
 */
@SpringBootApplication
public class SistemaVotacaoBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SistemaVotacaoBackendApplication.class, args);
    }
}