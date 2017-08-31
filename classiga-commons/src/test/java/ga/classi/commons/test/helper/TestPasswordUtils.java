package ga.classi.commons.test.helper;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ga.classi.commons.helper.PasswordUtils;

public class TestPasswordUtils {

    private static final Logger log = LoggerFactory.getLogger(TestPasswordUtils.class);
    
    @Test
    public void testStirWithSalt() {
        log.debug("Test stir with salt ...");
        String password = "12345678";
        String salt = RandomStringUtils.randomAlphanumeric(32);
        log.debug("Password: " + password);
        log.debug("Salt: " + salt);
        String result = PasswordUtils.stirWithSalt(password, salt);
        log.debug("Result: " + result);
        assertEquals(PasswordUtils.DEFAULT_LEGTH, result.length());
    }

    @Test
    public void testStirWithoutSalt() {
        log.debug("Test stir without salt ...");
        String password = "pwd!@#A";
        log.debug("Password: " + password);
        String result = PasswordUtils.stir(password);
        log.debug("Result: " + result);
        assertEquals(PasswordUtils.DEFAULT_LEGTH, result.length());
    }
    
}
