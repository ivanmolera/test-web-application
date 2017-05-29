import constants.Roles;
import mocks.UserDAOMock;
import model.User;
import model.dao.UserDAO;
import model.dao.UserDAOImpl;
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
public class UserDAOTest {

    UserDAO userDAO = null;
    UserDAOMock userDAOMock = new UserDAOMock();

    @Before
    public void setUp() {
        DatabaseManager.initDatabase();
        userDAO = new UserDAOImpl();
    }

    @Test
    public void testWhenUserDoesNotExistThenNullObjectIsReturned() {
        User user = userDAO.findByUsername("randomusername");
        Assert.assertTrue(user == null);
    }

    @Test
    public void testWhenUserExistsThenUserObjectIsReturned() {
        User user = userDAO.findByUsername("user01");
        Assert.assertTrue(user instanceof User);
    }

    @Test
    public void testWhenFindAllUsersIsTriggeredThenWeGotAList() {
        List<User> usersList = userDAO.findAllUsers();
        Assert.assertTrue(usersList != null && usersList.size() > 0);
    }

    @Test(expected = SQLException.class)
    public void testWhenTryingToAddExistingUserThenExceptionIsThrown() throws SQLException {
        User user = new User();
        user.setUsername("user01");
        user.setPassword("1111");
        userDAO.add(user);
    }

    @Test
    public void testWhenAddUserThenNoExceptionIsThrown() {
        User user = new User();
        user.setUsername("newuser");
        user.setPassword("xxxx");

        try
        {
            userDAO.delete("newuser");
            userDAO.add(user);
        }
        catch (SQLException ex)
        {
            fail("SQLException");
        }
    }

    @Test
    public void testWhenUpdateUserThenNoExceptionIsThrown() {
        User user = new User();
        user.setUsername("user01");
        user.setPassword("1111");
        List<String> roles = new ArrayList<String>();
        roles.add(Roles.PAGE_ONE.getRole());
        user.setRoles(roles);

        try
        {
            userDAO.update(user);
        }
        catch (SQLException ex)
        {
            fail("SQLException");
        }
    }

    @Test
    public void testWhenDeleteUserThenNoExceptionIsThrown() {
        try
        {
            userDAO.delete("randomusername");
        }
        catch (SQLException ex)
        {
            fail("SQLException");
        }
    }
}
