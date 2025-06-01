package Testes;

import cryptographyUtil.RSAUtil;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class ShowKeys {

    public static void main(String[] args) {
        try {
            // Geração de chaves e separação
            KeyPair pair = RSAUtil.generateKeyPair();
            PublicKey pubKey = pair.getPublic();
            PrivateKey privKey = pair.getPrivate();

            // Codificação em Base64
            String publicKeyBase64 = Base64.getEncoder().encodeToString(pubKey.getEncoded());
            String privateKeyBase64 = Base64.getEncoder().encodeToString(privKey.getEncoded());

            // Impressão formatada
            System.out.println("-----BEGIN PUBLIC KEY-----");
            System.out.println(publicKeyBase64);
            System.out.println("-----END PUBLIC KEY-----");

            System.out.println();

            System.out.println("-----BEGIN PRIVATE KEY-----");
            System.out.println(privateKeyBase64);
            System.out.println("-----END PRIVATE KEY-----");



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

