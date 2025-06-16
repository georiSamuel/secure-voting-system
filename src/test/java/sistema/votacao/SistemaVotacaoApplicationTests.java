package sistema.votacao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Teste geral da aplicação
 *
 * @version 1.0
 * @since 20/05/2025
 */

@SpringBootTest
public class SistemaVotacaoApplicationTests {

	@Test
	@DisplayName("A inicialização da aplicação não deve apresentar erros")
	void contextLoads() {
		// Teste passa se o contexto Spring carregar sem exceções
	}

}
