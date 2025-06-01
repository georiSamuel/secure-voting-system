package cryptographyUtil;

//SHA-256 – Hash para integridade do voto (NÃO É NECESSÁRIO NO PROJETO, MAS DECIDIR DEIXAR PARA MOSTRAR OUTRA TÉCNICA DE SEGURANÇA)
import java.nio.ByteBuffer;
import java.security.MessageDigest;

public class HashUtil {
    // Método para gerar um hash SHA-256 de uma string
    public static String sha256(int data) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(ByteBuffer.allocate(4).putInt(data).array());
        return bytesToHex(hash); // Retorna o hash em formato hexadecimal

    }

    // Método auxiliar para converter bytes em uma string hexadecimal
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes)
            sb.append(String.format("%02x", b));  // %02x  converte o valor de b (um byte) em uma representação hexadecimal com 2 dígitos, preenchendo com zero à esquerda se necessário
        return sb.toString();
    }
}


