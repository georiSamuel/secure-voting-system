package sistema.votacao.Util;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * Classe Utilitária para gerar chave secreta codificada em Base64 usada pela classe VoteCountEncryptor para criptogravar a quantidade de votos
 * de cada opção antes de enviá-la ao banco de dados
 *
 *
 *<h3>Ação necessária</h3>
 * <pre># copiar saída para o application.properties
 * voting.app.aes-key=saída
 * </pre>
 * @author Georis
 * @version 1.0
 * @since 13/06/2025
 *
 * @see VoteCountEncryptor
 */
public class KeyGenerator {
    public static void main(String[] args) {
        // Usa uma fonte de aleatoriedade criptograficamente segura (ao invés de só usar o Random)
        SecureRandom secureRandom = new SecureRandom();

        // Cria um array de bytes para a chave. 32 bytes = 256 bits.
        byte[] key = new byte[32];
        secureRandom.nextBytes(key); // Preenche o array com bytes aleatórios

        // Codifica os bytes aleatórios para o formato Base64 (texto)
        String base64Key = Base64.getEncoder().encodeToString(key);

        System.out.println("--- Chave Mestra (AES-256 / Base64) ---");
        System.out.println("Copiar esta chave para o arquivo application.properties:");
        System.out.println(base64Key);
    }
}