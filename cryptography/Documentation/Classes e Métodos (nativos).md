# Java Cryptography Documentation

## RSAUtil

### Core Classes and Interfaces

#### PublicKey Interface (java.security)
```java
public interface PublicKey extends java.security.AsymmetricKey
```
- Contains no methods or constants
- Serves to group and provide type safety for all public key interfaces

#### PrivateKey Interface (java.security)
```java
public interface PrivateKey extends java.security.AsymmetricKey, javax.security.auth.Destroyable
```
- Groups and provides type safety for all private key interfaces

### Cipher Class (javax.crypto)

#### Overview
- Provides functionality of a cryptographic cipher for encryption and decryption
- Forms the core of the Java Cryptographic Extension (JCE) framework
- Created using `Cipher.getInstance()` method

#### Key Methods

**getInstance(String transformation)**
```java
Cipher c = Cipher.getInstance("RSA");
// or with full specification (recommended)
Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
```
- Returns a Cipher object that implements the specified transformation
- **Recommendation**: Use transformation that fully specifies algorithm, mode, and padding
- Without full specification, provider uses defaults which may not meet security requirements

**init(int opmode, java.security.Key key)**
- Initializes Cipher object with a key
- Operation modes:
    - `ENCRYPT_MODE`: Encryption
    - `DECRYPT_MODE`: Decryption (constant used to initialize cipher to decryption mode)
    - `WRAP_MODE`: Key wrapping
    - `UNWRAP_MODE`: Key unwrapping
- **Important**: When initialized, cipher loses all previously-acquired state
- Equivalent to creating a new instance of the Cipher object

**doFinal(byte[] input)**
- Encrypts or decrypts data in single-part operation or finishes multiple-part operation
- Processes input buffer and any buffered bytes from previous operations
- Applies padding if requested
- Resets cipher object to state when previously initialized
- **Note**: If exception is thrown, cipher may need reset before reuse

### KeyPair and KeyPairGenerator Classes (java.security)

#### KeyPair Class
- Simple holder for a key pair (public key and private key)
- Does not enforce security
- Should be treated like a PrivateKey when initialized

#### KeyPairGenerator Class (Abstract)
- Used to generate pairs of public and private keys
- **Note**: Abstract class extending KeyPairGeneratorSpi for historical reasons
- Application developers should focus on KeyPairGenerator methods only

**Key Concepts:**
- **Keysize**: Interpreted differently for different algorithms (e.g., DSA keysize = modulus length)
- **Source of randomness**: Uses SecureRandom implementation

**Standard Required Algorithms and Keysizes:**
- **DiffieHellman**: (1024, 2048, 4096)
- **DSA**: (1024, 2048)
- **RSA**: (1024, 2048, 4096)

**Key Methods:**
- `getInstance(String algorithm)`: Creates generator for specified algorithm
- `initialize(int keysize)`: Initializes generator with keysize and default SecureRandom
- `generateKeyPair()`: Generates the key pair

### Base64 Encoding (java.util.Base64)

#### Overview
- Class with static methods for Base64 encoding/decoding
- Uses "The Base64 Alphabet" per RFC 4648 and RFC 2045
- Encoder adds no line feed characters
- Decoder rejects data with characters outside base64 alphabet

#### Key Methods
- `Base64.getEncoder()`: Returns Basic type base64 encoder
- `Base64.getEncoder().encodeToString(byte[] src)`: Encodes byte array to Base64 string
- `Base64.getDecoder()`: Returns Basic type base64 decoder
- `Base64.getDecoder().decode(String src)`: Decodes Base64 string to byte array

### String Methods

#### getBytes()
- From `java.lang.String`
- Encodes String into byte sequence using default charset
- Stores result in new byte array

---

## SignatureUtil

### Signature Class (java.security)

#### Overview
- Provides digital signature algorithm functionality
- Used for authentication and integrity assurance of digital data
- Can generate and verify digital signatures

#### Key Methods

**initSign(PrivateKey privateKey)**
```java
public final void initSign(java.security.PrivateKey privateKey) 
    throws java.security.InvalidKeyException
```
- Initialize object for signing
- Calling again with different argument negates previous call effect

**update(byte[] data)**
- Updates data to be signed or verified using specified byte array
- Example: `signature.update(data.getBytes())`

**sign()**
- Returns signature bytes of all updated data
- Format depends on underlying signature scheme

**verify(byte[] signature)**
```java
public final boolean verify(byte[] signature) 
    throws java.security.SignatureException
```
- Verifies the passed-in signature
- Returns `true` if signature verified, `false` if not
- Resets Signature object to state when initialized for verification
- Object becomes available to verify another signature

#### Base64 Usage in SignatureUtil
- Located in `java.util` package
- Used for encoding/decoding data in Base64 format
- Common for transmitting binary data as text
- `getEncoder()`: Static method returning `Base64.Encoder` instance
- `encodeToString()`: Method of `Base64.Encoder` to encode data and return as string

---

## PasswordUtil

### BCrypt Library (org.mindrot.jbcrypt.BCrypt)

#### Overview
- Well-known implementation of Blowfish password hashing algorithm
- Designed specifically for password hashing
- Import: `import org.mindrot.jbcrypt.BCrypt;`

#### Key Features
1. **Salt Generation**: Automatically generates unique salts for each password hash
2. **Work Factor**: Configurable computational cost to resist brute-force attacks
3. **Built-in Security**: Blowfish encryption optimized for password hashing

#### Basic Usage
```java
// Hash a password
String hashedPassword = BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());

// Check if password matches hash
boolean matches = BCrypt.checkpw(attemptedPassword, hashedPassword);
```

#### Key Advantage
- Specifically designed to be slow and computationally intensive
- Much more resistant to brute-force attacks than simple hashing (like SHA-256)
- Superior to basic hashing algorithms for password security

#### Common Use Cases
- Securely hashing passwords before database storage
- Verifying passwords during authentication

---

## AESUtil

### Core Classes and Interfaces

#### SecureRandom Class (java.security)
```java
public class SecureRandom extends java.util.Random
```
- Cryptographically strong random number generator (RNG)
- Provides secure source of randomness for cryptographic operations
- Extends `java.util.Random` but uses cryptographically secure algorithms

**Key Methods:**
- `nextBytes(byte[] bytes)`: Generates random bytes and places them into user-supplied byte array
- Each call produces cryptographically strong random values

#### SecretKeyFactory Class (javax.crypto)
```java
public class SecretKeyFactory extends Object
```
- Factory class for converting specifications (key material) into secret keys
- Provides functionality to convert between different key representations

**Key Methods:**
- `getInstance(String algorithm)`: Returns SecretKeyFactory object for specified algorithm
    - Example: `SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")`
- `generateSecret(KeySpec keySpec)`: Generates SecretKey object from provided key specification

#### KeySpec Interface (java.security.spec)
```java
public interface KeySpec
```
- Transparent representation of the key material that constitutes a cryptographic key
- Serves as base interface for key specifications

#### PBEKeySpec Class (javax.crypto.spec)
```java
public class PBEKeySpec extends Object implements KeySpec
```
- Key specification for Password-Based Encryption (PBE) keys
- Used with PBKDF2 (Password-Based Key Derivation Function 2)

**Constructor:**
```java
public PBEKeySpec(char[] password, byte[] salt, int iterationCount, int keyLength)
```
- `password`: Password as character array
- `salt`: Salt bytes for key derivation
- `iterationCount`: Number of iterations for key stretching
- `keyLength`: Desired key length in bits

#### SecretKeySpec Class (javax.crypto.spec)
```java
public class SecretKeySpec extends Object implements KeySpec, SecretKey
```
- Specification of a secret key constructed from given byte array
- Implements both KeySpec and SecretKey interfaces

**Constructor:**
```java
public SecretKeySpec(byte[] key, String algorithm)
```
- `key`: Key material as byte array
- `algorithm`: Name of secret-key algorithm (e.g., "AES")

#### GCMParameterSpec Class (javax.crypto.spec)
```java
public class GCMParameterSpec extends Object implements AlgorithmParameterSpec
```
- Algorithm parameters for Galois/Counter Mode (GCM)
- GCM provides both confidentiality and authenticity

**Constructor:**
```java
public GCMParameterSpec(int tLen, byte[] src)
```
- `tLen`: Authentication tag length in bits (typically 128)
- `src`: Initialization Vector (IV) bytes

**Key Features of GCM:**
- **Authenticated Encryption**: Provides both encryption and authentication
- **IV Requirements**: Requires unique IV for each encryption operation
- **Tag Authentication**: Automatically validates data integrity during decryption

#### System.arraycopy() Method (java.lang.System)
```java
public static void arraycopy(Object src, int srcPos, Object dest, int destPos, int length)
```
- Copies array from source to destination
- **Parameters:**
    - `src`: Source array
    - `srcPos`: Starting position in source array
    - `dest`: Destination array
    - `destPos`: Starting position in destination array
    - `length`: Number of array elements to copy

**Performance Note:** Highly optimized native method, faster than manual copying

#### PKCS8EncodedKeySpec Class (java.security.spec)
```java
public class PKCS8EncodedKeySpec extends EncodedKeySpec
```
- Key specification for PKCS#8 encoded private keys
- PKCS#8 is standard format for storing private key information

**Constructor:**
```java
public PKCS8EncodedKeySpec(byte[] encodedKey)
```
- `encodedKey`: Key encoded according to PKCS#8 standard

**PKCS#8 Format:**
- **Standard**: Public-Key Cryptography Standards #8
- **Purpose**: Syntax for storing private key material
- **Structure**: Contains algorithm identifier and private key data
- **Compatibility**: Widely supported across cryptographic libraries

#### KeyFactory Class (java.security)
```java
public abstract class KeyFactory extends Object
```
- Used to convert keys (Key objects) into key specifications (KeySpec objects) and vice versa
- Algorithm-specific factory for key conversion operations

**Key Methods:**
- `getInstance(String algorithm)`: Returns KeyFactory instance for specified algorithm
    - Example: `KeyFactory.getInstance("RSA")`
- `generatePrivate(KeySpec keySpec)`: Generates private key object from provided specification
- `generatePublic(KeySpec keySpec)`: Generates public key object from provided specification

**Common Algorithms:**
- **RSA**: For RSA public/private key operations
- **DSA**: For Digital Signature Algorithm keys
- **ECDSA**: For Elliptic Curve Digital Signature Algorithm keys

### Advanced AES-GCM Concepts

#### Password-Based Key Derivation Function 2 (PBKDF2)
- **Purpose**: Derives cryptographic keys from passwords
- **Security**: Applies cryptographic hash function with salt and iteration count
- **Algorithm Used**: PBKDF2WithHmacSHA256
- **Key Stretching**: Makes brute-force attacks computationally expensive

**Security Parameters:**
- **Salt**: Random data to prevent rainbow table attacks
- **Iterations**: High count (65536+ recommended) to slow down attacks
- **Key Length**: 256 bits for AES-256

#### AES-GCM Mode Benefits
1. **Authenticated Encryption**: Single operation provides both confidentiality and authenticity
2. **Performance**: Highly efficient, often hardware-accelerated
3. **Parallelizable**: Can encrypt/decrypt blocks in parallel
4. **Industry Standard**: Widely adopted in modern cryptographic protocols

#### IV (Initialization Vector) Requirements for GCM
- **Uniqueness**: Must be unique for each encryption with same key
- **Length**: 96 bits (12 bytes) is recommended for GCM
- **Randomness**: Should be cryptographically random
- **Storage**: Can be stored alongside ciphertext (not secret)

#### Security Considerations
1. **IV Reuse**: Never reuse IV with same key in GCM mode
2. **Key Derivation**: Use strong password and sufficient iterations
3. **Salt Storage**: Salt should be unique per password but can be stored openly
4. **Authentication Tag**: GCM automatically provides integrity verification

---

## HSAUtil

### MessageDigest Class (java.security)

#### Overview
```java
public abstract class MessageDigest extends java.security.MessageDigestSpi
```
- Provides message digest algorithm functionality (SHA-1, SHA-256, etc.)
- Message digests are secure one-way hash functions
- Take arbitrary-sized data and output fixed-length hash value
- **Note**: Abstract class extending MessageDigestSpi for historical reasons
- Application developers should focus on MessageDigest methods only

#### Key Methods

**getInstance(String algorithm)**
```java
public static MessageDigest getInstance(@NonNls @NotNull String algorithm) 
    throws java.security.NoSuchAlgorithmException
```
- Returns MessageDigest object implementing specified algorithm
- Example: `MessageDigest.getInstance("SHA-256")`

**digest(byte[] input)**
- Performs final update on digest using specified byte array
- Completes digest computation
- Process: calls `update(input)` first, then calls `digest()`
- Example: `digest.digest(data.getBytes())`

### StringBuilder Class

#### Overview
```java
public final class StringBuilder extends AbstractStringBuilder 
    implements Appendable, java.io.Serializable, Comparable<StringBuilder>, CharSequence
```
- Creates mutable sequence of characters
- Principal operations: `append` and `insert` methods
- Overloaded to accept data of any type

#### Example Usage
```java
// If z contains "start"
z.append("le")    // Results in "startle"
z.insert(4, "le") // Results in "startle"
```

### String.format() Method

#### Overview
- Method of String class for creating formatted strings according to specified pattern
- Syntax: `String.format(format, arg1, arg2, ...)`

#### Parameters
- **format**: String defining desired format with format markers (%d, %s, %f, etc.)
- **arg1, arg2, ...**: Arguments inserted into formatted string according to markers

#### Format Specifier Example: `%02x`
- `%`: Indicates format specifier follows
- `02`: Defines minimum width and padding
    - `0`: Pad with zeros on left if value less than 2 digits
    - `2`: Minimum width of 2 characters
- `x`: Format value as lowercase hexadecimal number