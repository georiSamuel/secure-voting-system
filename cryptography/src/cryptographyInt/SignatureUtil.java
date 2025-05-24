package cryptographyInt;

//  Assinatura Digital (SHA-256 + RSA)
import java.security.*;
import java.util.Base64;



public class SignatureUtil {
    // Método para assinar dados usando a chave privada
    public static String sign(String data, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(data.getBytes()); // Atualiza os dados a serem assinados
        return Base64.getEncoder().encodeToString(signature.sign()); // Retorna a assinatura em Base64
    }

    // Método para verificar a assinatura usando a chave pública
    public static boolean verify(String data, String signatureStr, PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(data.getBytes()); // Atualiza os dados a serem verificados
        byte[] signatureBytes = Base64.getDecoder().decode(signatureStr); // Decodifica a assinatura
        return signature.verify(signatureBytes); // Retorna verdadeiro se a assinatura for válida
    }
}


/* DOCUMENTATION

 *Class Signature:The Signature class is used to provide applications the functionality of a digital signature algorithm.
 Digital signatures are used for authentication and integrity assurance of digital data.

     A Signature object can be used to generate and verify digital signatures.


 *signature.initSign(privateKey): Initialize this object for signing. If this method is called again with a different argument, it negates the effect of this call.


    public final void initSign(
    java.security.PrivateKey privateKey
    )
    throws java.security.InvalidKeyException

 *signature.update(data.getBytes()): Updates the data to be signed or verified, using the specified array of bytes.

    Params:
    data – the byte array to use for the update.


 *Base64:
    O Base64 aqui é uma classe do Java, que está localizada no package java.util. Ela é usada para codificar e decodificar dados em Base64, um formato de codificação comumente utilizado para transmitir dados binários como texto.

    Classe: Base64 (localizada no package java.util).

    getEncoder():
    O método getEncoder() é um método estático da classe Base64, que retorna uma instância de Base64.Encoder. Essa instância será usada para realizar a codificação em Base64.

    Método estático: getEncoder() retorna um objeto de codificador.

    encodeToString():
    O método encodeToString() é um método de Base64.Encoder (o objeto retornado pelo getEncoder()), e é utilizado para codificar os dados em Base64 e retornar o resultado como uma string.


 *signature.sign(): Returns the signature bytes of all the data updated. The format of the signature depends on the underlying signature scheme.

 *signature.verify(signatureBytes): Verifies the passed-in signature.

    A call to this method resets this Signature object to the state it was in when previously initialized for verification via a call to initVerify(PublicKey). That is, the object is reset and available to verify another signature from the identity whose public key was specified in the call to initVerify.

    Params:
    signature – the signature bytes to be verified.

    Returns:
    true if the signature was verified, false if not.

     public final boolean verify(
        byte[] signature
    )
    throws java.security.SignatureException
 */


