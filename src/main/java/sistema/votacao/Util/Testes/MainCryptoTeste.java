package sistema.votacao.Util.Testes;

import sistema.votacao.Util.*;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

public class MainCryptoTeste {
        public static void main(String[] args) {
            try {
                // Geração de chaves e separação
                KeyPair pair = RSAUtil.generateKeyPair();
                PublicKey pubKey = pair.getPublic();
                PrivateKey privKey = pair.getPrivate();


                // Testes.User
                User anora = new User("Anora", "123456");
                String hashSenha = Password.hashPassword(anora.getSenha());
                System.out.printf("Hash da Senha de %s : " + hashSenha + "\n", anora.getSenha());
                System.out.println("Verificando a senha de Anora...");
                boolean senhaValida = Password.verifyPassword(anora.getSenha(), hashSenha);
                System.out.println("Senha de Anora " + (senhaValida ? "Válida" : "Inválida"));

                System.out.println("\n");

                // Voto original
                Candidato Bolsonaro = new Candidato("Bolsonaro");
                Bolsonaro.votar();
                Bolsonaro.votar();

                // Criptografa o voto
                String votoCriptografado = RSAUtil.encrypt(Bolsonaro.getVotosRecebidos(), pubKey);
                System.out.println("Voto Criptografado: " + votoCriptografado);

                System.out.println("\n");

                // Assina o voto criptografado
                String assinatura = Signature.sign(votoCriptografado, privKey);
                System.out.println("Assinatura: " + assinatura);

                //assinatura = assinatura + "georis"; //Daria erro pois tiraria a string do formato base64

                // Verifica a assinatura
                boolean valido = Signature.verify(votoCriptografado, assinatura, pubKey);
                System.out.println("Assinatura Válida: " + valido);


                System.out.println("\n");


                // Hash do voto para integridade
                String hashVoto = Hash.sha256(Bolsonaro.getVotosRecebidos());
                System.out.println("Hash do Voto: " + hashVoto);


                // Voto descptografado
                int votosDescriptografados = RSAUtil.decrypt(votoCriptografado, privKey);
                System.out.println("Votos Descriptografados: " + votosDescriptografados);

                //Criptografando chave privada
                byte[] salt = AES.generateSalt();
                SecretKey secretKey = AES.getKeyFromPassword(anora.getSenha(), salt);
                String EncryptedPrivateKey = AES.encryptPrivateKey(privKey.getEncoded(),secretKey);


                // Salvar no banco:
                // - encrypted.encryptedText (String)
                // - encrypted.salt (byte[])

                System.out.println("Private Key Encrypted: " + EncryptedPrivateKey);

                //Descriptografando a chave privada e verificando se deu certo
                PrivateKey decryptedPrivateKey = AES.decryptPrivateKey(EncryptedPrivateKey,secretKey);
                boolean saoIguais = (decryptedPrivateKey == privKey); //DÁ ERRADO == compara referências de objetos na memória
                                                                          //privKey e decryptedPrivateKey são objetos diferentes (criados em momentos diferentes)
                System.out.println("Private Keys são iguais: " + saoIguais);

                boolean saoIguais2 = Arrays.equals(decryptedPrivateKey.getEncoded(), privKey.getEncoded());
                System.out.println("Private Keys são iguais: " + saoIguais2 + "\n"); //Dá certo (OBG DEUS) pois o conteúdo do array de bytes da chave é comparado

                //Testando descriptografia com a chave privada após ser descriptografada
                int votosDescriptografadosComPrivKeyDesciptografada = RSAUtil.decrypt(votoCriptografado, decryptedPrivateKey);
                System.out.println("Votos Descriptografados: " + votosDescriptografadosComPrivKeyDesciptografada); //Deus seja louvado


            } catch (Exception e) {
                e.printStackTrace();
                //imprime a "stack trace" (rastreamento de pilha) da exceção no console. A stack trace fornece informações sobre onde a exceção ocorreu
                // Incluindo: o nome da exceção, a mensagem de erro e a lista de chamadas de método que estavam em execução no momento em que a exceção foi lançada
            }
        }
    }