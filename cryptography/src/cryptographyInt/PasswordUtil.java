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



    /* DOCUMENTATION

    *import org.mindrot.jbcrypt.BCrypt

        The line imports the BCrypt password hashing library, which is a well-known implementation of the Blowfish password hashing algorithm. `import org.mindrot.jbcrypt.BCrypt;`
    BCrypt is designed specifically for password hashing and includes:
    1. **Salt Generation**: Automatically generates unique salts for each password hash
    2. **Work Factor**: Allows configuration of the computational cost to make brute-force attacks more difficult
    3. **Built-in Security**: Implements the Blowfish encryption algorithm in a way that's specifically optimized for password hashing

    This library is commonly used for:
    - Securely hashing passwords before storing them in databases
    - Verifying passwords during authentication

    BASIC USE:

    // Hash a password
    String hashedPassword = BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());

    // Check if a password matches the hash
    boolean matches = BCrypt.checkpw(attemptedPassword, hashedPassword);

            The key advantage of BCrypt over simple hashing algorithms
            (like the SHA-256 you're using in the commented code) is that it's specifically designed to be
            slow and computationally intensive, making it much more resistant to brute-force attacks.

     */
}
