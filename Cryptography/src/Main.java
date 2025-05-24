    import cryptographyInt.RSAUtil;
    import cryptographyInt.HashUtil;
    import cryptographyInt.SignatureUtil;
    import cryptographyInt.PasswordUtil;

    import java.security.KeyPair;
    import java.security.PrivateKey;
    import java.security.PublicKey;

    public class Main {
        public static void main(String[] args) {
            try {
                // Geração de chaves e separação
                KeyPair pair = RSAUtil.generateKeyPair();
                PublicKey pubKey = pair.getPublic();
                PrivateKey privKey = pair.getPrivate();


                // User
                User anora = new User("Anora", "123456");
                String hashSenha = PasswordUtil.hashPassword(anora.getSenha());
                System.out.printf("Hash da Senha de %s : " + hashSenha + "\n", anora.getSenha());
                System.out.println("Verificando a senha de Anora...");
                boolean senhaValida = PasswordUtil.verifyPassword(anora.getSenha(), hashSenha);
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
                String assinatura = SignatureUtil.sign(votoCriptografado, privKey);
                System.out.println("Assinatura: " + assinatura);

                //assinatura = assinatura + "georis"; //Daria erro pois tiraria a string do formato base64

                // Verifica a assinatura
                boolean valido = SignatureUtil.verify(votoCriptografado, assinatura, pubKey);
                System.out.println("Assinatura Válida: " + valido);


                System.out.println("\n");


                // Hash do voto para integridade
                String hashVoto = HashUtil.sha256(Bolsonaro.getVotosRecebidos());
                System.out.println("Hash do Voto: " + hashVoto);


                // Voto descptografado
                int votosDescriptografados = RSAUtil.decrypt(votoCriptografado, privKey);
                System.out.println("Votos Descriptografados: " + votosDescriptografados);


            } catch (Exception e) {
                e.printStackTrace();
                //imprime a "stack trace" (rastreamento de pilha) da exceção no console. A stack trace fornece informações sobre onde a exceção ocorreu
                // Incluindo: o nome da exceção, a mensagem de erro e a lista de chamadas de método que estavam em execução no momento em que a exceção foi lançada
            }
        }
    }