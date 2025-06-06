package sistema.votacao.cryptography.src.Testes;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Scanner;


 class HashMD5 {
    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);

            System.out.println("Qual texto você gostaria de encriptar?");
            String texto = sc.next();

            MessageDigest md = MessageDigest.getInstance("MD5"); //Criação da nosssa variável que amazena a hash

            byte[] bty = texto.getBytes(); //Função hash lida com bytes, então preciso dessa conversão
            md.update(bty, 0, texto.length());// método para carregar os dados de nosso array bty, mas a hash só fica pronta com o método digest()

            String hash = new BigInteger(1, md.digest()).toString(16); //Conversão para Hexadecimal, assim a representação da hash não fica no formato de array de bytes

            System.out.println("A sua hash criptografada é: " + hash);

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}