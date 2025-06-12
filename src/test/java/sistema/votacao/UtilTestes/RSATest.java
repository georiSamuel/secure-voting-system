package sistema.votacao.UtilTestes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import sistema.votacao.Util.RSA;

import static org.junit.jupiter.api.Assertions.*;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

class RSATest {

    private KeyPair keyPair;
    private PublicKey publicKey;
    private PrivateKey privateKey;

    @BeforeEach
    void setUp() throws Exception {
        keyPair = RSA.generateKeyPair();
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
    }

    @Test
    void testGenerateKeyPair() throws Exception {
        KeyPair pair = RSA.generateKeyPair();

        assertNotNull(pair.getPublic());
        assertNotNull(pair.getPrivate());
        assertEquals("RSA", pair.getPublic().getAlgorithm());
    }

    @Test
    void testEncryptDecryptCycle() throws Exception {
        int[] numeros = {0, 123, -456, Integer.MAX_VALUE, Integer.MIN_VALUE};

        for (int numero : numeros) {
            String encrypted = RSA.encrypt(numero, publicKey);
            int decrypted = RSA.decrypt(encrypted, privateKey);

            assertNotNull(encrypted);
            assertFalse(encrypted.isEmpty());
            assertEquals(numero, decrypted);
        }
    }

    @Test
    void testDecryptWithWrongKey() throws Exception {
        int numero = 123;
        String encrypted = RSA.encrypt(numero, publicKey);

        KeyPair wrongKeyPair = RSA.generateKeyPair();
        PrivateKey wrongPrivateKey = wrongKeyPair.getPrivate();

        assertThrows(Exception.class, () -> {
            RSA.decrypt(encrypted, wrongPrivateKey);
        });
    }

    @Test
    void testInvalidInputs() throws Exception {
        int numero = 123;
        String encrypted = RSA.encrypt(numero, publicKey);

        assertThrows(Exception.class, () -> RSA.encrypt(123, null));
        assertThrows(Exception.class, () -> RSA.decrypt(encrypted, null));
        assertThrows(Exception.class, () -> RSA.decrypt("invalid", privateKey));
        assertThrows(Exception.class, () -> RSA.decrypt(null, privateKey));
    }
}