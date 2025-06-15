package sistema.votacao.Util;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.ByteBuffer;
import java.security.SecureRandom;



/**
 * Classe utilitária para operações de criptografia AES (Advanced Encryption Standard).
 *
 * <p>Esta classe fornece métodos estáticos para criptografar e descriptografar dados
 * usando o algoritmo AES. Utiliza o modo GCM (Galois/Counter Mode) para garantir
 * confidencialidade e autenticidade dos dados.</p>
 *
 * <p>O AES-GCM é um dos padrões mais modernos e seguros para criptografia simétrica autenticada.</p>
 *
 * - Configuração de criptografia -
 * <ul>
 * <li><strong>Algoritmo:</strong> AES (Advanced Encryption Standard)</li>
 * <li><strong>Modo:</strong> GCM (Galois/Counter Mode)</li>
 * <li><strong>Padding:</strong> NoPadding (GCM não requer padding)</li>
 * <li><strong>Formato:</strong> "AES/GCM/NoPadding"</li>
 * <li><strong>Tamanho do IV (Vetor de Inicialização):</strong> 12 bytes (96 bits)</li>
 * <li><strong>Tamanho da Tag de Autenticação:</strong> 16 bytes (128 bits)</li>
 * </ul>
 *
 *
 * @author Georis
 * @version 3.0
 * @since 10/05/2025
 *
 * @see javax.crypto.Cipher
 * @see javax.crypto.SecretKey
 * @see GCMParameterSpec
 */
public class AES {

    private static final String ENCRYPTION_ALGORITHM = "AES/GCM/NoPadding";
    private static final int IV_LENGTH_BYTES = 12; // 96 bits é o recomendado para GCM
    private static final int AUTH_TAG_LENGTH_BITS = 128; // Tamanho padrão da tag de autenticação

    /**
     * Criptografa dados usando uma SecretKey. O Vetor de Inicialização (IV) é gerado e adicionado ao início do resultado.
     * @param data Os dados em bytes a serem criptografados.
     * @param key A chave secreta a ser usada.
     * @return Os dados criptografados, formatados como [IV + ciphertext].
     * @throws Exception se ocorrer um erro durante a criptografia.
     */
    public static byte[] encryptWithKey(byte[] data, SecretKey key) throws Exception {
        if (data == null || key == null) {
            throw new IllegalArgumentException("Os dados e a chave não podem ser nulos.");
        }

        byte[] iv = new byte[IV_LENGTH_BYTES];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);

        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(AUTH_TAG_LENGTH_BITS, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, gcmParameterSpec);

        byte[] cipherText = cipher.doFinal(data);

        // Concatena o IV com o texto cifrado para que possa ser usado na descriptografia
        ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + cipherText.length);
        byteBuffer.put(iv);
        byteBuffer.put(cipherText);
        return byteBuffer.array();
    }

    /**
     * Descriptografa dados usando uma SecretKey. O IV é extraído do início dos dados recebidos.
     * @param encryptedData Os dados criptografados no formato [IV + ciphertext].
     * @param key A chave secreta a ser usada.
     * @return Os dados descriptografados.
     * @throws Exception se ocorrer um erro durante a descriptografia.
     */
    public static byte[] decryptWithKey(byte[] encryptedData, SecretKey key) throws Exception {
        if (encryptedData == null || key == null) {
            throw new IllegalArgumentException("Os dados criptografados e a chave não podem ser nulos.");
        }

        // Extrai o IV e o texto cifrado do array de bytes
        ByteBuffer byteBuffer = ByteBuffer.wrap(encryptedData);
        byte[] iv = new byte[IV_LENGTH_BYTES];
        byteBuffer.get(iv);

        byte[] cipherText = new byte[byteBuffer.remaining()];
        byteBuffer.get(cipherText);

        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(AUTH_TAG_LENGTH_BITS, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, gcmParameterSpec);

        return cipher.doFinal(cipherText);
    }
}