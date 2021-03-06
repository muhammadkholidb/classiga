/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.commons.utility;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author muhammad
 */
@Slf4j
public class AES {

    private AES() {}
    
    // With modification from https://aesencryption.net/

    private static final String DEFAULT_KEY    = "DfVlt@s3CrEt#k3Y";
    private static final String ALGORITHM      = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

    private static SecretKeySpec createSecretKeySpec(String key) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] digest = sha.digest(key.getBytes("UTF-8"));
            digest = Arrays.copyOf(digest, 16); // use only first 128 bit
            return new SecretKeySpec(digest, ALGORITHM);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            log.error("Failed to create secret key!", e);
        }
        return null;
    }

    public static String encrypt(String plainText) {
        return encrypt(plainText, null);
    }

    public static String encrypt(String plainText, String key) {
        try {
            SecretKeySpec secretKey = (key == null) ? createSecretKeySpec(DEFAULT_KEY) : createSecretKeySpec(key);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return DatatypeConverter.printBase64Binary(encrypted);
        } catch (InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            log.error("Encryption failed!", e);
        }
        return null;
    }

    public static String decrypt(String encrypted) {
        return decrypt(encrypted, null);
    }

    public static String decrypt(String encrypted, String key) {
        try {
            SecretKeySpec secretKey = (key == null) ? createSecretKeySpec(DEFAULT_KEY) : createSecretKeySpec(key);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decrypted = cipher.doFinal(DatatypeConverter.parseBase64Binary(encrypted));
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            log.error("Decryption failed!", e);
        }
        return null;
    }

}
