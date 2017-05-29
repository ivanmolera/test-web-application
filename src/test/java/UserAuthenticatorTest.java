import constants.Server;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.DatabaseManager;
import util.UserAuthenticator;

/**
 * Created by ivanmolera on 27/5/17.
 */
public class UserAuthenticatorTest {

    DatabaseManager dbm = null;

    @Before
    public void setUp() {
        dbm = DatabaseManager.initDatabase();
    }

    @Test
    public void testGivenAdminCredentialsWhenUserIsCheckedThenReturnsTrue() {
        UserAuthenticator authenticator = new UserAuthenticator(Server.USER_REALM);
        Assert.assertTrue(authenticator.checkCredentials("admin", "admin"));
    }

    @Test
    public void testGivenWrongCredentialsWhenUserIsCheckedThenReturnsFalse() {
        UserAuthenticator authenticator = new UserAuthenticator(Server.USER_REALM);
        Assert.assertFalse(authenticator.checkCredentials("admin", "xxxx"));
    }
}
