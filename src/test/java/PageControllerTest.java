import constants.Roles;
import constants.Server;
import controllers.PageController;
import mocks.HttpExchangeMock;
import mocks.UserDAOMock;
import model.User;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import util.DatabaseManager;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivanmolera on 28/5/17.
 */
public class PageControllerTest {

    private static PageController pageController = null;
    private static HttpExchangeMock exchangeMock = null;
    private static UserDAOMock userDAOMock = null;
    private static String hostName = null;

    @BeforeClass
    public static void setUp() throws Exception {
        DatabaseManager.initDatabase();

        pageController = new PageController();

        userDAOMock = new UserDAOMock();
        pageController.setUserDAO(userDAOMock);
        pageController.setAccessRoles(SimpleHttpServer.getAccessRoles());

        exchangeMock = new HttpExchangeMock();
        exchangeMock.setRequestURI("http://" + hostName + ":" + Server.SERVER_PORT + Server.PAGE_ONE);

        hostName = InetAddress.getLocalHost().getCanonicalHostName();
    }

    @Before
    public void resetCallers() {
        userDAOMock.resetCallers();
        pageController.setResponse(new StringBuilder());
    }

    @Test
    public void testWhenUserHasAccessThenPageIsShown() throws IOException {
        exchangeMock.setCurrentUser("user01");
        userDAOMock.setIsAdmin(false);
        pageController.handle(exchangeMock);
        //Assert.assertThat(pageController.getResponse().toString(), CoreMatchers.containsString("<html>"));
        Assert.assertNotEquals("", pageController.getResponse().toString());
    }

    @Test
    public void testWhenPageIsRequestedThenWeReceiveResponse() throws IOException {
        StringBuilder content = pageController.buildPageContent(userDAOMock.createDummyUser());
        Assert.assertNotEquals(null, content);
        Assert.assertNotEquals("", content.toString());
    }

    @Test
    public void testWhenUserHasNoAccessThenPageIsNotShown() throws IOException {
        exchangeMock.setCurrentUser("user01");
        userDAOMock.setIsAdmin(false);
        exchangeMock.setRequestURI("http://" + hostName + ":" + Server.SERVER_PORT + Server.PAGE_THREE);
        pageController.handle(exchangeMock);
        Assert.assertEquals("403 (Forbidden)\n", pageController.getResponse().toString());
    }

    @Test
    public void testWhenUserRequestsPageThenUserDAOIsCalled() throws IOException {
        exchangeMock.setCurrentUser("user01");
        userDAOMock.setIsAdmin(false);
        pageController.handle(exchangeMock);
        Assert.assertTrue(userDAOMock.findIsCalled());
    }

    @Test
    public void testWhenCookieIsCreatedThenWeReceiveString() throws IOException {
        String cookie = pageController.createCookie();
        Assert.assertNotEquals(null, cookie);
        Assert.assertNotEquals("", cookie);
    }

    @Test
    public void testWhenPageRequestIsHandledThenNoExceptionIsThrown() throws IOException {
        try {
            exchangeMock.setCurrentUser("admin");
            userDAOMock.setIsAdmin(true);
            pageController.handle(exchangeMock);
        }
        catch (Exception e) {
            Assert.fail("Exception thrown");
        }
    }

    @Test
    public void testWhenSessionEndedThenCookieIsNotValid() throws IOException {
        String cookieDateTime = "29/05/2017 20:30:00";
        boolean isValid = pageController.cookieIsValid(cookieDateTime);
        Assert.assertFalse(isValid);
    }

    @Test
    public void testWhenLogoutIsTriggeredThenSessionIsClosed() throws IOException {
        exchangeMock.setCurrentUser("user01");
        exchangeMock.setRequestURI("http://" + hostName + ":" + Server.SERVER_PORT + Server.PAGE_LOGOUT);
        pageController.handle(exchangeMock);
        Assert.assertEquals("Bye!\n", pageController.getResponse().toString());
    }

    @Test
    public void testWhenUserHasRoleThenAccessIsGranted() throws IOException {
        List<String> pageRoles = new ArrayList<String>();
        pageRoles.add(Roles.PAGE_ONE.getRole());
        User user = userDAOMock.createDummyUser();
        boolean hasAccess = pageController.userHasAccessToPage(pageRoles, user.getRoles());
        Assert.assertTrue(hasAccess);
    }
}
