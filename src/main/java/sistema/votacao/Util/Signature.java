package sistema.votacao.Util;

//  Assinatura Digital (SHA-256 + RSA)
import java.security.*;
import java.util.Base64;



public class Signature {
    // Método para assinar dados usando a chave privada
    public static String sign(String data, PrivateKey privateKey) throws Exception {
        java.security.Signature signature = java.security.Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(data.getBytes()); // Atualiza os dados a serem assinados
        return Base64.getEncoder().encodeToString(signature.sign()); // Retorna a assinatura em Base64
    }

    // Método para verificar a assinatura usando a chave pública
    public static boolean verify(String data, String signatureStr, PublicKey publicKey) throws Exception {
        java.security.Signature signature = java.security.Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(data.getBytes()); // Atualiza os dados a serem verificados
        byte[] signatureBytes = Base64.getDecoder().decode(signatureStr); // Decodifica a assinatura
        return signature.verify(signatureBytes); // Retorna verdadeiro se a assinatura for válida
    }
}




