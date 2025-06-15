package sistema.votacao.Util;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * Classe Utilitária para gerar chave secreta codificada em Base64 usada pela classe VoteCountEncryptor para criptografar a quantidade de votos
 * de cada opção antes de enviá-la ao banco de dados. Além disso, é usada para a chave HMAC de registro de integridade dos votos
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
 * @see HMAC
 */
public class KeyGenerator {
    public static void main(String[] args) {
        // Usa uma fonte de aleatoriedade criptograficamente segura (ao invés de só usar o Random)
        SecureRandom secureRandom = new SecureRandom();

        // Cria um array de bytes para a chave. 32 bytes = 256 bits.
        byte[] key1 = new byte[32];
        byte[] key2 = new byte[32];

        // Preenche os arrays com bytes aleatórios
        secureRandom.nextBytes(key1);
        secureRandom.nextBytes(key2);

        // Codifica os bytes aleatórios para o formato Base64 (texto)
        String base64Key1 = Base64.getEncoder().encodeToString(key1);
        String base64Key2 = Base64.getEncoder().encodeToString(key2);

        System.out.println("<--- Chave Mestra (AES-256 / Base64) --->");
        System.out.println("Copiar esta chave para o arquivo application.properties (VOTING_APP_AES_KEY):");
        System.out.println(base64Key1 + "\n");

        System.out.println("<--- Chave Secreta (HMAC / Base64) --->");
        System.out.println("Copiar esta chave para o arquivo application.properties (VOTING_APP_HMAC_SECRET):");
        System.out.println(base64Key2);
    }
}