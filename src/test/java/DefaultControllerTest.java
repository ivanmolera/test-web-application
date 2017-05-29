import constants.Server;
import controllers.DefaultController;
import mocks.HttpExchangeMock;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import util.DatabaseManager;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by ivanmolera on 28/5/17.
 */
public class DefaultControllerTest {

    private static DefaultController defaultController = null;
    private static HttpExchangeMock exchangeMock = null;
    private static String hostName = null;

    @BeforeClass
    public static void setUp() throws Exception {
        DatabaseManager.initDatabase();

        defaultController = new DefaultController();
        defaultController.setResponse(new StringBuilder());

        exchangeMock = new HttpExchangeMock();
        exchangeMock.setRequestURI("http://" + hostName + ":" + Server.SERVER_PORT + Server.PAGE_ONE);

        hostName = InetAddress.getLocalHost().getCanonicalHostName();
    }

    @Test
    public void testWhenDefaultRequestThenPageIsShown() throws IOException {
        exchangeMock.setCurrentUser("user01");
        defaultController.handle(exchangeMock);
        Assert.assertEquals("This is the default handler", defaultController.getResponse().toString());
    }
}
