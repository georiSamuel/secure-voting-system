package sistema.votacao.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.util.Base64;


/**
 *Conversor JPA responsável por criptografar e descriptografar contadores de votos
 * antes de armazenar no banco de dados.
 *
 * <p>Esta classe implementa criptografia AES para proteger garantir que os números de votos sejam armazenados de forma segura no banco de dados.
 * Os valores Long são automaticamente criptografados ao salvar e descriptografados ao carregar.</p>
 *
 * <p>A chave de criptografia deve ter 32 bytes/256 bits (para maior segurança), gerada pela classe KeyGenerator ser configurada no arquivo application.properties
 * com a propriedade {voting.app.aes-key} em formato Base64.</p>
 *
 *
 * - Configuração necessária -
 * <pre>
 * # application.properties
 * voting.app.aes-key=chaveBase64
 * </pre>
 *
 * @author Georis
 * @version 1.1
 * @since 13/06/2025
 * @see KeyGenerator
 * @see AES
 */
@Converter
@Component
public class VoteCountEncryptor implements AttributeConverter<Long, byte[]> {

    private final SecretKey masterKey;

    /**
     * Construtor que inicializa o encriptador com a chave secreta configurada.
     *
     * <p>A chave deve estar em formato Base64 e ser configurada através da
     * propriedade {@code voting.app.aes-key} no application.properties.</p>
     *
     * @param secret chave secreta em formato Base64 injetada pelo Spring através da assinatura @Value que
     * serve para injetar valores de configuração diretamente em campos de um componente gerenciado pelo Spring
     * @throws IllegalArgumentException se a chave for inválida
     */
    public VoteCountEncryptor(@Value("${VOTING_APP_AES_KEY}") String secret) {
        // A inicialização do Cipher foi removida daqui.
        byte[] decodedKey = Base64.getDecoder().decode(secret);
        this.masterKey = new SecretKeySpec(decodedKey, "AES");
    }


    /**
     * Converte um valor Long para formato criptografado para armazenamento no banco.
     *
     * <p>Este método é chamado automaticamente pelo JPA sempre que um valor
     * precisa ser salvo no banco de dados. O número de votos é criptografado
     * usando AES antes do armazenamento.</p>
     *
     * @param attribute o número de votos a ser criptografado (pode ser null)
     * @return array de bytes criptografado, ou null se attribute for null
     * @throws IllegalStateException se ocorrer erro durante a criptografia
     */
    @Override
    public byte[] convertToDatabaseColumn(Long attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
            buffer.putLong(attribute);
            // LÓGICA ALTERADA: Delega a criptografia para a classe AES
            return AES.encryptWithKey(buffer.array(), masterKey);
        } catch (Exception e) {
            throw new IllegalStateException("Não foi possível criptografar a contagem de votos.", e);
        }
    }

    /**
     * Converte dados criptografados do banco de volta para um valor Long.
     *
     * <p>Este método é chamado automaticamente pelo JPA ao carregar dados
     * do banco de dados. Os bytes criptografados são descriptografados
     * e convertidos de volta para o número de votos original.</p>
     *
     * @param dbData array de bytes criptografado do banco de dados (pode ser null)
     * @return número de votos descriptografado, ou 0L se dbData for null
     * @throws IllegalStateException se ocorrer erro durante a descriptografia
     */

    @Override
    public Long convertToEntityAttribute(byte[] dbData) {
        if (dbData == null) {
            return 0L;
        }
        try {
            // LÓGICA ALTERADA: Delega a descriptografia para a classe AES
            byte[] decryptedBytes = AES.decryptWithKey(dbData, masterKey);
            ByteBuffer buffer = ByteBuffer.wrap(decryptedBytes);
            return buffer.getLong();
        } catch (Exception e) {
            throw new IllegalStateException("Não foi possível descriptografar a contagem de votos.", e);
        }
    }
}