import model.User;
import model.dao.UserDAO;
import model.dao.UserDAOImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.DatabaseManager;

import java.util.List;

/**
 * Created by ivanmolera on 27/5/17.
 */
public class DatabaseManagerTest {

    DatabaseManager dbm = null;

    @Before
    public void setUp() {
        dbm = DatabaseManager.initDatabase();
    }

    @Test
    public void testWhenDatabaseIsInitializedThenNoExceptionIsThrown() {
        Assert.assertTrue(dbm instanceof DatabaseManager);
    }

    @Test
    public void testWhenDatabaseIsInitializedThenDefaultUsersAreLoaded() {
        UserDAO userDAO = new UserDAOImpl();
        List<User> usersList = userDAO.findAllUsers();

        Assert.assertTrue(usersList != null && usersList.size() > 0);
    }

}
