package sistema.votacao.UtilTestes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import sistema.votacao.Util.AES;
import sistema.votacao.Util.RSA;

import static org.junit.jupiter.api.Assertions.*;

import java.security.KeyPair;
import java.security.PrivateKey;
import javax.crypto.SecretKey;
import java.util.Base64;

class AESTest {

    private String password;
    private byte[] salt;
    private SecretKey secretKey;
    private PrivateKey originalPrivateKey;

    @BeforeEach
    void setUp() throws Exception {
        password = "senhaSegura123!";
        salt = AES.generateSalt();
        secretKey = AES.getKeyFromPassword(password, salt);

        // Gera uma chave privada RSA para usar nos testes
        KeyPair keyPair = RSA.generateKeyPair();
        originalPrivateKey = keyPair.getPrivate();
    }

    @Test
    void testGenerateSalt() {
        byte[] salt1 = AES.generateSalt();
        byte[] salt2 = AES.generateSalt();

        assertEquals(32, salt1.length);
        assertEquals(32, salt2.length);
        assertFalse(java.util.Arrays.equals(salt1, salt2));
    }

    @Test
    void testGenerateIv() {
        byte[] iv1 = AES.generateIv();
        byte[] iv2 = AES.generateIv();

        assertEquals(12, iv1.length);
        assertEquals(12, iv2.length);
        assertFalse(java.util.Arrays.equals(iv1, iv2));
    }

    @Test
    void testGetKeyFromPassword() throws Exception {
        SecretKey key1 = AES.getKeyFromPassword(password, salt);
        SecretKey key2 = AES.getKeyFromPassword(password, salt);

        assertNotNull(key1);
        assertNotNull(key2);
        assertEquals("AES", key1.getAlgorithm());
        assertArrayEquals(key1.getEncoded(), key2.getEncoded());
    }

    @Test
    void testDifferentPasswordsDifferentKeys() throws Exception {
        SecretKey key1 = AES.getKeyFromPassword("senha1", salt);
        SecretKey key2 = AES.getKeyFromPassword("senha2", salt);

        assertFalse(java.util.Arrays.equals(key1.getEncoded(), key2.getEncoded()));
    }

    @Test
    void testEncryptDecryptPrivateKey() throws Exception {
        byte[] privateKeyBytes = originalPrivateKey.getEncoded();

        String encrypted = AES.encryptPrivateKey(privateKeyBytes, secretKey);
        PrivateKey decrypted = AES.decryptPrivateKey(encrypted, secretKey);

        assertNotNull(encrypted);
        assertFalse(encrypted.isEmpty());
        assertNotNull(decrypted);
        assertEquals(originalPrivateKey.getAlgorithm(), decrypted.getAlgorithm());
        assertArrayEquals(originalPrivateKey.getEncoded(), decrypted.getEncoded());
    }

    @Test
    void testEncryptedDataIsBase64() throws Exception {
        byte[] privateKeyBytes = originalPrivateKey.getEncoded();
        String encrypted = AES.encryptPrivateKey(privateKeyBytes, secretKey);

        // Deve ser possível decodificar como Base64
        assertDoesNotThrow(() -> Base64.getDecoder().decode(encrypted));

        // Dados decodificados devem ter pelo menos 12 bytes (IV) + dados criptografados
        byte[] decoded = Base64.getDecoder().decode(encrypted);
        assertTrue(decoded.length > 12);
    }

    @Test
    void testEncryptionDeterministic() throws Exception {
        byte[] privateKeyBytes = originalPrivateKey.getEncoded();

        String encrypted1 = AES.encryptPrivateKey(privateKeyBytes, secretKey);
        String encrypted2 = AES.encryptPrivateKey(privateKeyBytes, secretKey);

        // Deve ser diferente a cada criptografia (IV aleatório)
        assertNotEquals(encrypted1, encrypted2);

        // Mas ambos devem descriptografar para a mesma chave
        PrivateKey decrypted1 = AES.decryptPrivateKey(encrypted1, secretKey);
        PrivateKey decrypted2 = AES.decryptPrivateKey(encrypted2, secretKey);

        assertArrayEquals(decrypted1.getEncoded(), decrypted2.getEncoded());
    }

    @Test
    void testDecryptWithWrongKey() throws Exception {
        byte[] privateKeyBytes = originalPrivateKey.getEncoded();
        String encrypted = AES.encryptPrivateKey(privateKeyBytes, secretKey);

        // Chave diferente
        byte[] wrongSalt = AES.generateSalt();
        SecretKey wrongKey = AES.getKeyFromPassword(password, wrongSalt);

        assertThrows(Exception.class, () -> {
            AES.decryptPrivateKey(encrypted, wrongKey);
        });
    }

    @Test
    void testInvalidInputs() throws Exception {
        byte[] privateKeyBytes = originalPrivateKey.getEncoded();

        // Testa entradas nulas
        assertThrows(Exception.class, () -> AES.encryptPrivateKey(null, secretKey));
        assertThrows(Exception.class, () -> AES.encryptPrivateKey(privateKeyBytes, null));
        assertThrows(Exception.class, () -> AES.decryptPrivateKey(null, secretKey));
        assertThrows(Exception.class, () -> AES.decryptPrivateKey("encrypted", null));

        // Testa Base64 inválido
        assertThrows(Exception.class, () -> AES.decryptPrivateKey("invalid_base64", secretKey));

        // Testa dados corrompidos
        String validEncrypted = AES.encryptPrivateKey(privateKeyBytes, secretKey);
        String corruptedData = validEncrypted.substring(0, validEncrypted.length() - 4) + "XXXX";
        assertThrows(Exception.class, () -> AES.decryptPrivateKey(corruptedData, secretKey));
    }
}