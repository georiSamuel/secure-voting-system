package cryptographyStr;

//SHA-256 – Hash para integridade
import java.security.MessageDigest;

public class HashUtilStr {
    // Método para gerar um hash SHA-256 de uma string
    public static String sha256(String data) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data.getBytes());
        return bytesToHex(hash); // Retorna o hash em formato hexadecimal

    }

    // Método auxiliar para converter bytes em uma string hexadecimal
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes)
            sb.append(String.format("%02x", b));  // %02x  converte o valor de b (um byte) em uma representação hexadecimal com 2 dígitos, preenchendo com zero à esquerda se necessário
        return sb.toString();
    }
}


/*DOCUMENTATION


 *java.security.MessageDigest: This MessageDigest class provides applications the functionality of a message digest
 algorithm, such as SHA-1 or SHA-256. Message digests
 are secure one-way hash functions that take arbitrary-sized data and output a fixed-length hash value

    public abstract class MessageDigest
    extends java.security.MessageDigestSp

    A MessageDigest object starts out initialized.

    Note that this class is abstract and extends from MessageDigestSpi for historical reasons. Application developers should only take notice of the methods defined in this MessageDigest

 *MessageDigest.getInstance("SHA-256"): Returns a MessageDigest object that implements the specified digest algorithm.

       java.security.MessageDigest
    public static MessageDigest getInstance(
        @NonNls   @NotNull   String algorithm
    )
    throws java.security.NoSuchAlgorithmException


 *digest.digest(data.getBytes())

    Performs a final update on the digest using the specified array of bytes,
    then completes the digest computation. That is,
    this method first calls update(input), passing the input array to the update method, then calls digest().

 *StringBuilder:Class to create a mutable sequence of characters.


    public final class StringBuilder
    extends AbstractStringBuilder
    implements Appendable,
               java.io.Serializable,
               Comparable<StringBuilder>,
               CharSequence

     The principal operations on a StringBuilder are the append and insert methods,
     which are overloaded so as to accept data of any type.

     For example, if z refers to a string builder object whose current contents are "start",
     then the method call z.append("le") would cause the string builder to contain "startle",
     whereas z.insert(4, "le") would alter the string builder to contain "starle

 *String.format(): é um método da classe String que permite criar uma nova string formatada de acordo com um padrão especificado

    String.format(format, arg1, arg2, ...);

    -format: A string que define o formato desejado. Ela pode incluir marcadores de formato (como %d, %s, %f, etc.).
    -arg1, arg2, ...: Os argumentos que serão inseridos na string formatada de acordo com os marcadores.

    "(%02x,b)"
    %: Indica que o seguinte texto será um especificador de formato.
    02: Define a largura mínima e o preenchimento do valor. Nesse caso:
    0 significa que, se o valor for menor que 2 dígitos, será preenchido com zeros à esquerda.
    2 significa que a largura mínima do valor formatado será de 2 caracteres.
    x: Indica que o valor será formatado como um número hexadecimal em minúsculas.
 */
