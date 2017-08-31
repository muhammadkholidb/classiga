package ga.classi.commons.helper;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

public class PasswordUtils {

    public static final int DEFAULT_LEGTH = 64;
    
    public static String stirWithSalt(String actual, String salt, int length) {
        String sha1 = DigestUtils.sha1Hex(DigestUtils.md5Hex(actual) + DigestUtils.md5Hex(salt)); // 40 characters
        String rightPad = StringUtils.rightPad(sha1, length, DigestUtils.sha1Hex(salt));
        return rightPad.substring(0, length); 
    }
    
    public static String stirWithSalt(String actual, String salt) {
        return stirWithSalt(actual, salt, DEFAULT_LEGTH);
    }
    
    public static String stir(String actual, int length) {
        String sha1 = DigestUtils.sha1Hex(DigestUtils.md5Hex(actual)); // 40 characters
        String rightPad = StringUtils.rightPad(sha1, length, DigestUtils.sha1Hex(actual));
        return rightPad.substring(0, length);
    }
    
    public static String stir(String actual) {
        return stir(actual, DEFAULT_LEGTH);
    }
    
}
