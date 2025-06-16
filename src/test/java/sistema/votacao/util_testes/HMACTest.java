package sistema.votacao.util_testes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import sistema.votacao.util.HMAC;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste simples para HMAC
 *
 * @author Georis
 * @version 1.0
 * @since 13/06/2025
 */
public class HMACTest {

    @Test
    @DisplayName("Deve calcular HMAC corretamente")
    void testCalcularHmac() throws Exception {
        String dados = "Voto Candidato A";
        String chave = "chaveSecreta";

        String hmac = HMAC.calcularHmac(dados, chave);

        assertNotNull(hmac);
        assertFalse(hmac.isEmpty());
    }

    @Test
    @DisplayName("Deve gerar mesmo HMAC para mesmos dados")
    void testConsistencia() throws Exception {
        String dados = "Dados teste";
        String chave = "chave123";

        String hmac1 = HMAC.calcularHmac(dados, chave);
        String hmac2 = HMAC.calcularHmac(dados, chave);

        assertEquals(hmac1, hmac2);
    }

    @Test
    @DisplayName("Deve lançar exceção para dados nulos")
    void testDadosNulos() {
        assertThrows(NullPointerException.class, () -> {
            HMAC.calcularHmac(null, "chave");
        });
    }
}