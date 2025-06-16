package sistema.votacao.util_testes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sistema.votacao.util.VoteCountEncryptor;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Base64;
import java.security.SecureRandom;

/**
 * Teste simples para VoteCountEncryptor
 *
 * @author Georis
 * @version 1.0
 * @since 13/06/2025
 */
public class VoteCountEncryptorTest {

    private VoteCountEncryptor encryptor;
    private String chaveSecreta;

    @BeforeEach
    void setUp() throws Exception {
        // Gera uma chave de teste válida (32 bytes)
        byte[] key = new byte[32];
        new SecureRandom().nextBytes(key);
        chaveSecreta = Base64.getEncoder().encodeToString(key);

        encryptor = new VoteCountEncryptor(chaveSecreta);
    }

    @Test
    void deveCriptografarEDescriptografarCorretamente() {
        Long valorOriginal = 1000L;

        // Criptografa
        byte[] valorCriptografado = encryptor.convertToDatabaseColumn(valorOriginal);

        // Descriptografa
        Long valorDescriptografado = encryptor.convertToEntityAttribute(valorCriptografado);

        assertEquals(valorOriginal, valorDescriptografado);
    }

    @Test
    void deveRetornarNullParaValorNull() {
        byte[] resultado = encryptor.convertToDatabaseColumn(null);
        assertNull(resultado);
    }

}