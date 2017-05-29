import org.junit.Assert;
import org.junit.Test;
import util.PasswordManager;

/**
 * Created by ivanmolera on 27/5/17.
 */
public class PasswordManagerTest {

    @Test
    public void testWhenEncryptPasswordThenNoBlankStringIsReturned() {
        String encryptedPassword = PasswordManager.encrypt("xxxx");
        Assert.assertFalse(encryptedPassword.equals(""));
    }

    @Test
    public void testGivenAdminPasswordWhenEncryptedThenValueMatches() {
        String adminPassword = "DD94709528BB1C83D08F3088D4043F4742891F4F";
        String encryptedPassword = PasswordManager.encrypt("admin");
        Assert.assertTrue(adminPassword.equals(encryptedPassword));
    }
}
