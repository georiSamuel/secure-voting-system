package sistema.votacao.Util;

//Criptografar e Decriptar Votos (criptografia assimétrica)
import java.nio.ByteBuffer;
import java.security.*;
import javax.crypto.Cipher;
import java.util.Base64;

public class RSA {

    // Método para gerar um par de chaves RSA (precisa dessa configuração)
    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator generate = KeyPairGenerator.getInstance("RSA"); //Returns a KeyPairGenerator object that generates public/private key pairs for the specified algorithm.
        generate.initialize(2048); // Tamanho da chave
        return generate.generateKeyPair();
    }



    // Método para criptografar um texto usando a chave pública
    public static String encrypt(int num, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        // Por padrão Java vai interpretar "RSA" como "RSA/ECB/PKCS1Padding", que é seguro para muitos casos de uso simples.
        //"RSA/ECB/OAEPWithSHA-256AndMGF1Padding"	Mais moderno, mais seguro contra ataques adaptativos

        cipher.init(Cipher.ENCRYPT_MODE, publicKey); //É preciso inicializar
        byte[] encryptedBytes = cipher.doFinal(ByteBuffer.allocate(4).putInt(num).array());
        return Base64.getEncoder().encodeToString(encryptedBytes); // Retorna o texto criptografado em Base64, pra ficar mais bonitinho do que um array de bytes

         /* TIP

            EU poderia criar uma função rápida só pra fazer esse trabalho de encode e decode,
            mas fica mais fácil colocar tudo no mesmo método que eu criptograafo e descriptografo:

            private String encode(byte[] data) {return Base64.getEncoder().encodeToString(data);}

        */
    }




    // Método para decriptar um texto usando a chave privada
    public static int decrypt(String cipherText, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        return ByteBuffer.wrap(decrypted).getInt(); // Retorna o texto original


         /* TIP

            EU poderia criar uma função rápida só pra fazer esse trabalho de encode e decode,
            mas fica mais fácil colocar tudo no mesmo método que eu criptograafo e descriptografo:

            private String encode(byte[] data) {return Base64.getEncoder().encodeToString(data);}
        */
    }


}