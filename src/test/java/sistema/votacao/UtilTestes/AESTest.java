package sistema.votacao.UtilTestes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import sistema.votacao.Util.AES;

import static org.junit.jupiter.api.Assertions.*;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * Teste simples para a classe AES
 *
 * @author Georis
 * @version 1.0
 * @since 13/06/2025
 */
public class AESTest {

    @Test
    @DisplayName("Deve criptografar e descriptografar texto corretamente")
    void testEncryptDecrypt() throws Exception {
        // Gera uma chave AES
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey key = keyGen.generateKey();

        // Dados de teste
        String texto = "Teste de criptografia";
        byte[] dados = texto.getBytes();

        // Criptografa
        byte[] criptografado = AES.encryptWithKey(dados, key);

        // Descriptografa
        byte[] descriptografado = AES.decryptWithKey(criptografado, key);

        // Verifica se funcionou
        String resultado = new String(descriptografado);
        assertEquals(texto, resultado);
    }

    @Test
    @DisplayName("Deve lançar exceção para chave nula")
    void testChaveNula() {
        assertThrows(Exception.class, () -> {
            AES.encryptWithKey("test".getBytes(), null);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção para dados nulos")
    void testDadosNulos() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        SecretKey key = keyGen.generateKey();

        assertThrows(Exception.class, () -> {
            AES.encryptWithKey(null, key);
        });
    }
}