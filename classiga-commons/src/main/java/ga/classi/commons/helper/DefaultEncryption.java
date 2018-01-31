package ga.classi.commons.helper;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author eatonmunoz
 */
@Slf4j
public class DefaultEncryption {

    // With modification from https://javapointers.com/tutorial/how-to-encrypt-and-decrypt-using-aes-in-java/
    // Add try-catch to prevent throwing exception, return null if errors occurred on encrypt / decrypt process
    private static final String SECRET_KEY_1 = "ssdkF$HUy2A#D%kd";
    private static final String SECRET_KEY_2 = "weJiSEvR5yAC5ftB";

    private IvParameterSpec ivParameterSpec;
    private SecretKeySpec secretKeySpec;
    private Cipher cipher;

    public DefaultEncryption() {
        try {
            ivParameterSpec = new IvParameterSpec(SECRET_KEY_1.getBytes("UTF-8"));
            secretKeySpec = new SecretKeySpec(SECRET_KEY_2.getBytes("UTF-8"), "AES");
            cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        } catch (UnsupportedEncodingException ex) {
            log.error("Failed to create DefaultEncryption instance!", ex);
        } catch (NoSuchAlgorithmException ex) {
            log.error("Failed to create DefaultEncryption instance!", ex);
        } catch (NoSuchPaddingException ex) {
            log.error("Failed to create DefaultEncryption instance!", ex);
        }
    }

    /**
     * Encrypt the string with this internal algorithm.
     *
     * @param plainText string object to be encrypt.
     * @return returns encrypted string.
     *
     */
    public String encrypt(String plainText) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] encrypted = cipher.doFinal(plainText.getBytes());
            return Base64.encodeBase64String(encrypted);
        } catch (InvalidKeyException ex) {
            log.error("Failed to encrypt: " + plainText, ex);
        } catch (InvalidAlgorithmParameterException ex) {
            log.error("Failed to encrypt: " + plainText, ex);
        } catch (IllegalBlockSizeException ex) {
            log.error("Failed to encrypt: " + plainText, ex);
        } catch (BadPaddingException ex) {
            log.error("Failed to encrypt: " + plainText, ex);
        }
        return null;
    }

    /**
     * Decrypt this string with the internal algorithm. The passed argument
     * should be encrypted using {@link #encrypt(String) encrypt} method of this
     * class.
     *
     * @param encrypted encrypted string that was encrypted using
     * {@link #encrypt(String) encrypt} method.
     * @return decrypted string.
     *
     */
    public String decrypt(String encrypted) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] decryptedBytes = cipher.doFinal(Base64.decodeBase64(encrypted));
            return new String(decryptedBytes);
        } catch (InvalidKeyException ex) {
            log.error("Failed to decrypt: " + encrypted, ex);
        } catch (InvalidAlgorithmParameterException ex) {
            log.error("Failed to decrypt: " + encrypted, ex);
        } catch (IllegalBlockSizeException ex) {
            log.error("Failed to decrypt: " + encrypted, ex);
        } catch (BadPaddingException ex) {
            log.error("Failed to decrypt: " + encrypted, ex);
        }
        return null;
    }
}
