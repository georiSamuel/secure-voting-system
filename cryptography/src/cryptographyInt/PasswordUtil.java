package cryptographyInt;

// Hash de Senhas - BCrypt
import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    // Método para gerar o hash da senha que vai ser armazenada no banco de dados
    public static String hashPassword(String plainPassword) {
        // Gera um hash da senha usando BCrypt
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    // Método para verificar se a senha fornecida corresponde ao hash armazenado
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        // Verifica se a senha fornecida corresponde ao hash
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }



}
