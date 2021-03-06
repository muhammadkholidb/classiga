/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.commons.utility;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

public final class PasswordUtils {

    public static final int DEFAULT_LEGTH = 64;
    
    private PasswordUtils() {
        // Restrict instantiation
    }
    
    public static String stir(String actual, String salt, int length) {
        if (StringUtils.isBlank(salt)) {
            String sha1 = DigestUtils.sha1Hex(DigestUtils.md5Hex(actual));
            String rightPad = StringUtils.rightPad(sha1, length, DigestUtils.sha1Hex(actual));
            return StringUtils.reverse(rightPad);
        } else {            
            String sha1 = DigestUtils.sha1Hex(DigestUtils.md5Hex(actual) + DigestUtils.md5Hex(salt));
            String rightPad = StringUtils.rightPad(sha1, length, DigestUtils.sha1Hex(salt));
            return StringUtils.reverse(rightPad);
        }
    }

    public static String stir(String actual, String salt) {
        return stir(actual, salt, DEFAULT_LEGTH);
    }
    
    public static String stir(String actual, int length) {
        return stir(actual, null, length);
    }
    
    public static String stir(String actual) {
        return stir(actual, DEFAULT_LEGTH);
    }
    
}
