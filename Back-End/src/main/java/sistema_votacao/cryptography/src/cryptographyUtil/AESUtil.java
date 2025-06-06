package sistema_votacao.cryptography.src.cryptographyUtil;

// Classe para criptografar chave privada com criptografia simétrica baseada em senha (AES-GCM)
// TODO: Vou criar uma nova senha para o admin descriptografar a chave privada com a chave simétrica??

import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class AESUtil {
    private static final int SALT_LENGTH = 32;
    private static final int IV_LENGTH = 12;
    private static final int ITERATIONS = 100022;


    // Gera salt criptograficamente seguro que será armazenado no BD
    public static byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    // Gera a chave AES a partir da senha e salt usando PBKDF2
    public static SecretKey getKeyFromPassword(String password, byte[] salt)
            throws Exception {

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, 256);
        return new SecretKeySpec(factory.generateSecret(spec)
                .getEncoded(), "AES");
    }

    // Gera um IV aleatório de 12 bytes (96 bits) para GCM
    public static byte[] generateIv() {
        byte[] iv = new byte[IV_LENGTH];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    // Criptografa os bytes da chave privada, concatena IV + ciphertext e retorna em Base64
    public static String encryptPrivateKey(byte[] privateKeyBytes, SecretKey key) throws Exception {
        byte[] iv = generateIv();
        GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key, gcmSpec);

        byte[] cipherText = cipher.doFinal(privateKeyBytes);

        // Concatena IV + ciphertext para guardar IV em Base64
        byte[] encryptedIvAndText = new byte[iv.length + cipherText.length];
        System.arraycopy(iv, 0, encryptedIvAndText, 0, iv.length);
        System.arraycopy(cipherText, 0, encryptedIvAndText, iv.length, cipherText.length);

        // Codifica em Base64 para armazenamento/transmissão
        return Base64.getEncoder().encodeToString(encryptedIvAndText);
    }

    // Recebe Base64 com IV + ciphertext, separa e descriptografa, retornando o objeto PrivateKey
    public static PrivateKey decryptPrivateKey(String base64CipherKey, SecretKey key) throws Exception {
        byte[] decoded = Base64.getDecoder().decode(base64CipherKey);

        // Extrai IV (12 bytes)
        byte[] iv = new byte[12];
        System.arraycopy(decoded, 0, iv, 0, iv.length);

        // Extrai só a parte criptografada da chave
        int cipherKeyLength = decoded.length - iv.length;
        byte[] cipherText = new byte[cipherKeyLength];
        System.arraycopy(decoded, iv.length, cipherText, 0, cipherKeyLength);

        GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, key, gcmSpec);

        byte[] privateKeyBytes = cipher.doFinal(cipherText);

        // Converte os bytes descriptografados para objeto PrivateKey
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA"); // Mesmo algoritmo usado na geração

        return keyFactory.generatePrivate(keySpec);
    }

}
