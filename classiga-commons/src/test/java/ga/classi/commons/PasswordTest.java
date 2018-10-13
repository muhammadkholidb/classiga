/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.commons;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

@Slf4j
public class PasswordTest {
    
    @Test
    public void testPasswordWithSalt() {
        log.debug("Test password with salt ...");
        String password = "12345678";
        String salt = RandomStringUtils.randomAlphanumeric(32);
        log.debug("Password: " + password);
        log.debug("Salt: " + salt);
        String result = DigestUtils.sha256Hex(password + salt);
        log.debug("Result: " + result);
        assertEquals(64, result.length());
    }

    @Test
    public void testPasswordWithoutSalt() {
        log.debug("Test password without salt ...");
        String password = "12345678";
        log.debug("Password: " + password);
        String result = DigestUtils.sha256Hex(password);
        log.debug("Result: " + result);
        assertEquals(64, result.length());
    }
    
}
