import constants.Server;
import controllers.UserController;
import mocks.HttpExchangeMock;
import mocks.UserDAOMock;
import mocks.UserRoleDAOMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import util.DatabaseManager;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by ivanmolera on 28/5/17.
 */
public class UserControllerTest {

    private static UserController userController = null;
    private static HttpExchangeMock exchangeMock = null;
    private static UserDAOMock userDAOMock = null;
    private static UserRoleDAOMock userRoleDAOMock = null;
    private static String hostName = null;

    @BeforeClass
    public static void setUp() throws Exception {
        DatabaseManager.initDatabase();

        userController = new UserController();

        userDAOMock = new UserDAOMock();
        userRoleDAOMock = new UserRoleDAOMock();

        userController.setUserDAO(userDAOMock);
        userController.setUserRoleDAO(userRoleDAOMock);

        exchangeMock = new HttpExchangeMock();
        exchangeMock.setRequestURI("http://" + hostName + ":" + Server.SERVER_PORT + Server.USER_CONTEXT);

        hostName = InetAddress.getLocalHost().getCanonicalHostName();
    }

    @Before
    public void resetCallers() {
        userDAOMock.resetCallers();
        userController.setResponse(new StringBuilder());
    }

    @Test
    public void testWhenCreatingNewUserThenAddIsCalled() throws IOException {
        userController.addUser(userDAOMock.createDummyUser(), exchangeMock);
        Assert.assertTrue(userDAOMock.addIsCalled());
    }

    @Test
    public void testWhenCurrentUserHasNotAdminRoleThenAddIsNotCalled() throws IOException {
        exchangeMock.setRequestMethod("POST");
        exchangeMock.setCurrentUser("user01");
        userDAOMock.setIsAdmin(false);

        userController.handle(exchangeMock);
        Assert.assertFalse(userDAOMock.addIsCalled());
    }

    @Test
    public void testWhenModifyingUserThenUpdateIsCalled() throws IOException {
        userController.updateUser(userDAOMock.createDummyUser(), exchangeMock);
        Assert.assertTrue(userDAOMock.updateIsCalled());
    }

    @Test
    public void testWhenCurrentUserHasNotAdminRoleThenUpdateIsNotCalled() throws IOException {
        exchangeMock.setRequestMethod("PUT");
        exchangeMock.setCurrentUser("user01");
        userDAOMock.setIsAdmin(false);

        userController.handle(exchangeMock);
        Assert.assertFalse(userDAOMock.addIsCalled());
    }

    @Test
    public void testWhenDeletingUserThenDeleteIsCalled() throws IOException {
        exchangeMock.setCurrentUser("admin");
        userController.deleteUser("username", exchangeMock);
        Assert.assertTrue(userDAOMock.deleteIsCalled());
    }

    @Test
    public void testWhenCurrentUserHasNotAdminRoleThenDeleteIsNotCalled() throws IOException {
        exchangeMock.setRequestMethod("DELETE");
        exchangeMock.setCurrentUser("user01");
        userDAOMock.setIsAdmin(false);

        userController.handle(exchangeMock);
        Assert.assertFalse(userDAOMock.addIsCalled());
    }

    @Test
    public void testWhenCreatingNewUserThenValidJsonIsShown() throws IOException {
        userController.getUser("user01", exchangeMock);
        Assert.assertTrue(userController.isValidJSON(userController.getResponse().toString()));
    }

    @Test
    public void testWhenCreatingNewUserThenNoJsonIsShown() throws IOException {
        userController.addUser(userDAOMock.createDummyUser(), exchangeMock);
        Assert.assertTrue("".equals(userController.getResponse().toString()));
    }

    @Test
    public void testGivenWrongJSONWhenParsingThenSendBadRequestStatus() {
        Assert.assertFalse(userController.isValidJSON("invalid json"));
    }
}
