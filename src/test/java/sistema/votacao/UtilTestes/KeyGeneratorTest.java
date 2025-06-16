package sistema.votacao.UtilTestes;

import org.junit.jupiter.api.Test;
import sistema.votacao.Util.KeyGenerator;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Base64;

/**
 * Teste simples para KeyGenerator
 *
 * @author Georis
 * @version 1.0
 * @since 13/06/2025
 */
public class KeyGeneratorTest {

    @Test
    void deveGerarChaveValida() {
        // Captura a saída do console
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(output));

        try {
            // Executa o gerador
            KeyGenerator.main(new String[]{});

            String saida = output.toString();
            String[] linhas = saida.split(System.lineSeparator());
            String chave = linhas[linhas.length - 1].trim();

            // Testa se é Base64 válido
            assertDoesNotThrow(() -> Base64.getDecoder().decode(chave));

            // Testa se tem 32 bytes (256 bits)
            byte[] bytes = Base64.getDecoder().decode(chave);
            assertEquals(32, bytes.length);

            // Testa se não está vazia
            assertFalse(chave.isEmpty());

        } finally {
            System.setOut(originalOut);
        }
    }

}