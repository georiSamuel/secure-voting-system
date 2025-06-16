package sistema.votacao.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Classe utilitária para gerar códigos de autenticação de mensagem baseados em Hash (HMAC).
 * Utiliza o algoritmo HmacSHA256 para garantir a integridade e autenticidade de cada voto. É formado com dados completos e específicos do ato de votar.
 * É uma espécie de "impressão digital" daquele evento único
 *
 * @author Georis
 * @version 1.0
 * @since 13/06/2025
 */
public final class HMAC {

    private static final String ALGORITHM = "HmacSHA256";

    /**
     * Construtor privado para impedir a instanciação da classe utilitária.
     */
    private HMAC() {}

    /**
     * Calcula o HMAC-SHA256 para uma dada string de dados e uma chave secreta.
     *
     * @param data A string de dados a ser autenticada.
     * @param secret A chave secreta a ser usada no cálculo.
     * @return Uma string Base64 representando o HMAC.
     * @throws NoSuchAlgorithmException se o algoritmo HmacSHA256 não for suportado.
     * @throws InvalidKeyException se a chave secreta for inválida.
     */
    public static String calcularHmac(String data, String secret) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac sha256Hmac = Mac.getInstance(ALGORITHM);
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        sha256Hmac.init(secretKey);
        byte[] hmacBytes = sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hmacBytes);
    }
}