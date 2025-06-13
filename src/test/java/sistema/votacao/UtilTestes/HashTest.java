package sistema.votacao.UtilTestes;

import org.junit.jupiter.api.Test;
import sistema.votacao.Util.Hash;

import static org.junit.jupiter.api.Assertions.*;

public class HashTest {

    @Test
    public void testHashConsistencia() throws Exception {
        // Mesmo valor deve gerar mesmo hash
        int valor = 123;
        String hash1 = Hash.sha256(valor);
        String hash2 = Hash.sha256(valor);

        assertEquals(hash1, hash2, "Mesmo valor deve gerar mesmo hash");
    }

    @Test
    public void testHashDiferentes() throws Exception {
        // Valores diferentes devem gerar hashes diferentes
        String hash1 = Hash.sha256(123);
        String hash2 = Hash.sha256(456);

        assertNotEquals(hash1, hash2, "Valores diferentes devem gerar hashes diferentes");
    }

    @Test
    public void testTamanhoHash() throws Exception {
        // SHA-256 sempre gera hash de 64 caracteres
        String hash = Hash.sha256(123);

        assertEquals(64, hash.length(), "Hash SHA-256 deve ter 64 caracteres");
    }

    @Test
    public void testHashFormatoHexadecimal() throws Exception {
        // Hash deve conter apenas caracteres hexadecimais (0-9, a-f)
        String hash = Hash.sha256(789);

        assertTrue(hash.matches("^[0-9a-f]+$"), "Hash deve conter apenas caracteres hexadecimais");
    }

    @Test
    public void testValoresEspeciais() throws Exception {
        // Testa valores especiais
        assertDoesNotThrow(() -> Hash.sha256(0), "Deve funcionar com zero");
        assertDoesNotThrow(() -> Hash.sha256(-1), "Deve funcionar com negativos");
        assertDoesNotThrow(() -> Hash.sha256(Integer.MAX_VALUE), "Deve funcionar com valor máximo");
        assertDoesNotThrow(() -> Hash.sha256(Integer.MIN_VALUE), "Deve funcionar com valor mínimo");
    }
}