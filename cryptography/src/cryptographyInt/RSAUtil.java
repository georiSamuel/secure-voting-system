package cryptographyInt;

//Criptografar e Decriptar Votos (criptografia assimétrica)
import java.nio.ByteBuffer;
import java.security.*;
import javax.crypto.Cipher;
import java.util.Base64;

public class RSAUtil {

    // Método para gerar um par de chaves RSA (precisa dessa configuração)
    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator generate = KeyPairGenerator.getInstance("RSA"); //Returns a KeyPairGenerator object that generates public/private key pairs for the specified algorithm.
        generate.initialize(2048); // Tamanho da chave
        return generate.generateKeyPair();
    }

    //TODO: Onde diacho a gente vai guardar a private key

    /* DOCUMENTATION

    Classe KeyPair (from java.security): This class is a simple holder for a key pair (a public key and a private key).
      It does not enforce any security, and, when initialized, should be treated like a PrivateKey.

     *The KeyPairGenerator abstract class is used to generate pairs of public and private keys. It's an abstract class

        Note that this class is abstract and extends from KeyPairGeneratorSpi for historical reasons. Application developers should only take notice of the methods defined in this KeyPairGenerator class; all the methods in the superclass are intended for cryptographic service providers who wish to supply their own implementations of key pair generators.
        All key pair generators share the concepts of a keysize and a source of randomness. The keysize is interpreted differently for different algorithms (e.g., in the case of the DSA algorithm, the keysize corresponds to the length of the modulus).
        There is an initialize method in this KeyPairGenerator class that takes these two universally shared types of arguments. There is also one that takes just a keysize argument, and uses the SecureRandom implementation of the highest-priority installed provider as the source of randomness.
        (If none of the installed providers supply an implementation of SecureRandom, a system-provided source of randomness is used.)


        Every implementation of the Java platform is required to support the following standard KeyPairGenerator algorithms and keysizes in parentheses:
        DiffieHellman (1024, 2048, 4096)
        DSA (1024, 2048)
        RSA (1024, 2048, 4096)

    *algorithm (KeyPairGenerator.getInstance) – the standard string name of the algorithm. See the KeyPairGenerator section in the Java Security Standard Algorithm Names

    *gen.initialize Initializes the key pair generator for a certain keysize using a default parameter set and the SecureRandom implementation of the highest-priority installed provider as the source of randomness.
        (If none of the installed providers supply an implementation of SecureRandom, a system-provided source of randomness is use


    *generateKeyPair() é o mesmo nome do meu método e é o responsável por gerar o key par

     */


    // Método para criptografar um texto usando a chave pública
    public static String encrypt(int num, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        // Por padrão Java vai interpretar "RSA" como "RSA/ECB/PKCS1Padding", que é seguro para muitos casos de uso simples.
        //"RSA/ECB/OAEPWithSHA-256AndMGF1Padding"	Mais moderno, mais seguro contra ataques adaptativos

        cipher.init(Cipher.ENCRYPT_MODE, publicKey); //É preciso inicializar
        byte[] encryptedBytes = cipher.doFinal(ByteBuffer.allocate(4).putInt(num).array());
        return Base64.getEncoder().encodeToString(encryptedBytes); // Retorna o texto criptografado em Base64, pra ficar mais bonitinho do que um array de bytes

         /* TIP

            EU poderia criar uma função rápida só pra fazer esse trabalho de encode e decode,
            mas fica mais fácil colocar tudo no mesmo método que eu criptograafo e descriptografo:

            private String encode(byte[] data) {return Base64.getEncoder().encodeToString(data);}

        */
    }





    /*DOCUMENTATION

    *PublicKey class (from java.security): public interface PublicKey extends java.security.AsymmetricKey
    This interface contains no methods or constants. It merely serves to group
    (and provide type safety for) all public key interfaces.

    *Cipher class (from javax.crypto): This class provides the functionality of a cryptographic cipher for encryption and decryption.
    It forms the core of the Java Cryptographic Extension (JCE) framework. In order to create a Cipher object, the application calls the cipher's getInstance method,
    and passes the name of the requested transformation to it. Optionally, the name of a provider may be specified.

        A transformation is a string that describes the operation (or set of operations) to be performed on the given input, to produce some output. A transformation always
        includes the name of a cryptographic algorithm (e.g., AES), and may be followed by a feedback mode and padding scheme.

        or example, the following is a valid transformation:
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");



    *Cipher.getInstance("RSA"); Returns a Cipher object that implements the specified transformation. (Params: transformation)


            It is recommended to use a transformation that fully specifies the algorithm, mode, and padding. By not doing so, the provider
            will use a default for the mode and padding which may not meet the security requirements of your application.


    *cipher.init(int opmode, java.security.Key key) : Initializes this Cipher object with a key.
    The Cipher object is initialized for one of the following four operations:
    encryption, decryption, key wrapping or key unwrapping, depending on the value of opcode.

        If this cipher requires any algorithm parameters that cannot be derived from the given key,
        the underlying cipher implementation is supposed to generate the required parameters itself (using provider-specific default or random values)

        if it is being initialized for encryption or key wrapping, and raise an InvalidKeyException if it is being initialized for decryption or key unwrapping.
        The generated parameters can be retrieved using getParameters or getIV (if the parameter is an IV).
        
        Note that when a Cipher object is initialized, it loses all previously-acquired state. In other words, 
        initializing a Cipher object is equivalent to creating a new instance of that Cipher object and initializing it.
        
        Params:
        opmode – the operation mode of this Cipher object (this is one of the following: ENCRYPT_MODE, DECRYPT_MODE, WRAP_MODE or UNWRAP_MODE)
        key – the key
        
        
    *public final byte[] doFinal( @NotNull   byte[] input): Encrypts or decrypts data in a single-part operation, or finishes a multiple-part operation. The data is encrypted or decrypted, depending on how this Cipher object was initialized.
    The bytes in the input buffer, and any input bytes that may have been buffered during
    a previous update operation, are processed, with padding (if requested) being applied

        Upon finishing, this method resets this Cipher object to the state it was in when previously initialized via a call to init. That is, the object is reset and available to encrypt or decrypt (depending on the operation mode that was specified in the call to init) more data.
        Note: if any exception is thrown, this Cipher object may need to be reset before it can be used again.

        Params:
        input – the input buffer
        Returns:
        the new buffer with the result


     *getBytes() (from java.lang.String): Encodes this String into a sequence of bytes using the default charset, storing the result into a new byte array.


     *Base 64: This class consists exclusively of static methods for obtaining encoders and decoders for the Base64 encoding scheme.

        Uses "The Base64 Alphabet" as specified in Table 1 of RFC 4648 and RFC 2045 for encoding and decoding operation.
        The encoder does not add any line feed (line separator) character. The decoder rejects data that contains characters outside the base64 alphabet.

        *Base64.getEncoder(): Returns a Base64.Encoder that encodes using the Basic type base64 encoding scheme.
        *Base64.getEncoder().encodeToString( byte[] src): Encodes the specified byte array into a String using the Base64 encoding scheme.
        */




    // Método para decriptar um texto usando a chave privada
    public static int decrypt(String cipherText, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        return ByteBuffer.wrap(decrypted).getInt(); // Retorna o texto original


         /* TIP

            EU poderia criar uma função rápida só pra fazer esse trabalho de encode e decode,
            mas fica mais fácil colocar tudo no mesmo método que eu criptograafo e descriptografo:

            private String encode(byte[] data) {return Base64.getEncoder().encodeToString(data);}
        */
    }

    /* DOCUMENTATION

    PivateKey class (from  java.security): public interface PrivateKey
    extends java.security.AsymmetricKey, javax.security.auth.Destroyable

    public interface PublicKey extends java.security.AsymmetricKey
    The purpose of this interface is to group (and provide type safety for) all private key interfaces.


    *Cipher.DECRYPT_MODE: Constant used to initialize cipher to decryption mode.


    *cipher.init(int opmode, java.security.Key key) : Initializes this Cipher object with a key.
    The Cipher object is initialized for one of the following four operations:
    encryption, decryption, key wrapping or key unwrapping, depending on the value of opmode.

        If this cipher requires any algorithm parameters that cannot be derived from the given key,
        the underlying cipher implementation is supposed to generate the required parameters itself (using provider-specific default or random values)

        if it is being initialized for encryption or key wrapping, and raise an InvalidKeyException if it is being initialized for decryption or key unwrapping.
        The generated parameters can be retrieved using getParameters or getIV (if the parameter is an IV).

        Note that when a Cipher object is initialized, it loses all previously-acquired state. In other words,
        initializing a Cipher object is equivalent to creating a new instance of that Cipher object and initializing it.

        Params:
        opmode – the operation mode of this Cipher object (this is one of the following: ENCRYPT_MODE, DECRYPT_MODE, WRAP_MODE or UNWRAP_MODE)
        key – the key


    *public final byte[] doFinal( @NotNull   byte[] input): Encrypts or decrypts data in a single-part operation, or finishes a multiple-part operation. The data is encrypted or decrypted, depending on how this Cipher object was initialized.
    The bytes in the input buffer, and any input bytes that may have been buffered during
    a previous update operation, are processed, with padding (if requested) being applied

        Upon finishing, this method resets this Cipher object to the state it was in when previously initialized via a call to init. That is, the object is reset and available to encrypt or decrypt (depending on the operation mode that was specified in the call to init) more data.
        Note: if any exception is thrown, this Cipher object may need to be reset before it can be used again.

        Params:
        input – the input buffer
        Returns:
        the new buffer with the result


     *getBytes() (from java.lang.String): Encodes this String into a sequence of bytes using the default charset, storing the result into a new byte array.


     *Base 64:

        *Base64.getDecoder(): Returns a Base64.Decoder that decodes using the Basic type base64 encoding scheme.
        *Base64.getDecoder().decodeToString( byte[] src): Decodes a Base64 encoded String into a newly-allocated byte array using the Base64 encoding scheme.

                An invocation of this method has exactly the same effect as invoking decode(src.getBytes(StandardCharsets.ISO_8859_1))
                Params:
                src–the string to decode

     */
}