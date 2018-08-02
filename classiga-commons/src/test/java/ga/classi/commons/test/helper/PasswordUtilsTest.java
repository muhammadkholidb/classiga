/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.commons.test.helper;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ga.classi.commons.helper.PasswordUtils;

public class PasswordUtilsTest {

    private static final Logger log = LoggerFactory.getLogger(PasswordUtilsTest.class);
    
    @Test
    public void testStirWithSalt() {
        log.debug("Test stir with salt ...");
        String password = "12345678";
        String salt = RandomStringUtils.randomAlphanumeric(32);
        log.debug("Password: " + password);
        log.debug("Salt: " + salt);
        String result = PasswordUtils.stir(password, salt);
        log.debug("Result: " + result);
        assertEquals(PasswordUtils.DEFAULT_LEGTH, result.length());
    }

    @Test
    public void testStirWithoutSalt() {
        log.debug("Test stir without salt ...");
        String password = "12345678";
        log.debug("Password: " + password);
        String result = PasswordUtils.stir(password);
        log.debug("Result: " + result);
        assertEquals(PasswordUtils.DEFAULT_LEGTH, result.length());
    }
    
}
