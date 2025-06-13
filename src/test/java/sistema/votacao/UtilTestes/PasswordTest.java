package sistema.votacao.UtilTestes;

import org.junit.jupiter.api.Test;
import sistema.votacao.Util.Password;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordTest {

    @Test
    public void testSenhaValida() {
        String senhaOriginal = "minhasenha123";

        // Gera hash da senha
        String hash = Password.hashPassword(senhaOriginal);

        // Verifica se a senha original confere com o hash
        boolean valida = Password.verifyPassword(senhaOriginal, hash);

        assertTrue(valida, "Senha original deve ser válida com seu hash");
    }

    @Test
    public void testSenhaInvalida() {
        String senhaOriginal = "minhasenha123";
        String senhaErrada = "senhaerrada456";

        // Gera hash da senha original
        String hash = Password.hashPassword(senhaOriginal);

        // Tenta verificar com senha errada
        boolean valida = Password.verifyPassword(senhaErrada, hash);

        assertFalse(valida, "Senha errada deve ser inválida");
    }



}