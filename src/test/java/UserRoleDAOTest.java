import constants.Roles;
import mocks.UserDAOMock;
import model.User;
import model.dao.UserDAO;
import model.dao.UserDAOImpl;
import model.dao.UserRoleDAO;
import model.dao.UserRoleDAOImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.DatabaseManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

/**
 * Created by ivanmolera on 29/5/17.
 */
public class UserRoleDAOTest {

    UserRoleDAO userRoleDAO = null;

    @Before
    public void setUp() {
        DatabaseManager.initDatabase();
        userRoleDAO = new UserRoleDAOImpl();
    }

    @Test
    public void testWhenFindUserRolesByUsernameIsTriggeredThenWeGotAList() {
        List<String> roles = userRoleDAO.findRolesByUsername("user01");
        Assert.assertTrue(roles != null && roles.size() > 0);
    }

    @Test
    public void testWhenFindUserRolesByUsernameIsTriggeredAndUserDoesNotExistThenWeGotAnEmptyList() {
        List<String> roles = userRoleDAO.findRolesByUsername("randomusernamexxx");
        Assert.assertTrue(roles.size() == 0);
    }

    @Test(expected = SQLException.class)
    public void testWhenTryingToAddExistingUserThenExceptionIsThrown() throws SQLException {
        userRoleDAO.add("user01", Roles.PAGE_ONE.getRole());
    }

    @Test
    public void testWhenAddUserRoleThenNoExceptionIsThrown() {
        try
        {
            userRoleDAO.deleteRolesByUsername("newuser");
            userRoleDAO.add("newuser", "newrole");
        }
        catch (SQLException ex)
        {
            fail("SQLException");
        }
    }
}
