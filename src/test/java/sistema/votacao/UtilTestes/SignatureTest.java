package sistema.votacao.UtilTestes;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import sistema.votacao.Util.Signature;

import static org.junit.jupiter.api.Assertions.*;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

public class SignatureTest {

    private static PrivateKey privateKey;
    private static PublicKey publicKey;

    @BeforeAll
    public static void setup() throws Exception {
        // Gera par de chaves RSA para os testes
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();

        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();
    }

    @Test
    public void testAssinaturaValida() throws Exception {
        // Dados para assinar
        String dados = "Voto do eleitor 123";

        // Assina os dados
        String assinatura = Signature.sign(dados, privateKey);

        // Verifica se a assinatura é válida
        boolean valida = Signature.verify(dados, assinatura, publicKey);

        assertTrue(valida, "Assinatura deve ser válida");
    }

    @Test
    public void testAssinaturaInvalida() throws Exception {
        String dados = "Voto do eleitor 123";
        String dadosAlterados = "Voto do eleitor 456"; // dados diferentes

        // Assina os dados originais
        String assinatura = Signature.sign(dados, privateKey);

        // Tenta verificar com dados alterados
        boolean valida = Signature.verify(dadosAlterados, assinatura, publicKey);

        assertFalse(valida, "Assinatura deve ser inválida para dados alterados");
    }

    @Test
    public void testAssinaturaConsistente() throws Exception {
        String dados = "Dados de teste";

        // Assina os mesmos dados duas vezes
        String assinatura1 = Signature.sign(dados, privateKey);
        String assinatura2 = Signature.sign(dados, privateKey);

        // RSA com padding aleatório gera assinaturas diferentes mesmo para dados iguais
        // Mas ambas devem ser válidas
        assertTrue(Signature.verify(dados, assinatura1, publicKey));
        assertTrue(Signature.verify(dados, assinatura2, publicKey));
    }

    @Test
    public void testAssinaturaFormatoBase64() throws Exception {
        String dados = "Teste formato";
        String assinatura = Signature.sign(dados, privateKey);

        // Verifica se está em formato Base64 válido
        assertDoesNotThrow(() -> {
            java.util.Base64.getDecoder().decode(assinatura);
        }, "Assinatura deve estar em formato Base64 válido");

        // Base64 só deve conter caracteres válidos
        assertTrue(assinatura.matches("^[A-Za-z0-9+/]*={0,2}$"),
                "Assinatura deve conter apenas caracteres Base64 válidos");
    }

    @Test
    public void testDadosVazios() throws Exception {
        String dados = "";

        // Deve conseguir assinar dados vazios
        String assinatura = Signature.sign(dados, privateKey);
        boolean valida = Signature.verify(dados, assinatura, publicKey);

        assertTrue(valida, "Deve conseguir assinar e verificar dados vazios");
    }

    @Test
    public void testDadosGrandes() throws Exception {
        // Testa com dados maiores
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append("Dados de teste ").append(i).append(" ");
        }
        String dadosGrandes = sb.toString();

        String assinatura = Signature.sign(dadosGrandes, privateKey);
        boolean valida = Signature.verify(dadosGrandes, assinatura, publicKey);

        assertTrue(valida, "Deve conseguir assinar dados grandes");
    }
}