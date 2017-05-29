import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import constants.Roles;
import constants.Server;
import controllers.DefaultController;
import controllers.PageController;
import controllers.UserController;
import model.dao.UserDAOImpl;
import model.dao.UserRoleDAOImpl;
import util.DatabaseManager;
import util.UserAuthenticator;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SimpleHttpServer {

    public static void main(String[] args) {

        try {
            DatabaseManager.initDatabase();

            String hostName = InetAddress.getLocalHost().getCanonicalHostName();

            HttpServer server = HttpServer.create(new InetSocketAddress(Server.SERVER_PORT), Server.BACKLOG);
            server.createContext(Server.DEFAULT_CONTEXT, new DefaultController());

            UserController userController = new UserController();
            userController.setUserDAO(new UserDAOImpl());
            userController.setUserRoleDAO(new UserRoleDAOImpl());

            HttpContext userContext = server.createContext(Server.USER_CONTEXT, userController);
            userContext.setAuthenticator(new UserAuthenticator(Server.USER_REALM));

            PageController pageController = new PageController();
            pageController.setUserDAO(new UserDAOImpl());
            pageController.setAccessRoles(getAccessRoles());

            HttpContext pageContext = server.createContext(Server.PAGE_CONTEXT, pageController);
            pageContext.setAuthenticator(new UserAuthenticator(Server.PAGE_REALM));

            server.setExecutor(null); // creates a default executor
            server.start();

            System.out.println("\nSimple HTTP Server is running...");
            System.out.println("\nhttp://" + hostName + ":" + Server.SERVER_PORT + Server.DEFAULT_CONTEXT);

            System.out.println("\nhttp://" + hostName + ":" + Server.SERVER_PORT + Server.USER_CONTEXT + "/user01");

            System.out.println("\nhttp://" + hostName + ":" + Server.SERVER_PORT + Server.PAGE_CONTEXT + "/one.html");
            System.out.println("http://" + hostName + ":" + Server.SERVER_PORT + Server.PAGE_CONTEXT + "/two.html");
            System.out.println("http://" + hostName + ":" + Server.SERVER_PORT + Server.PAGE_CONTEXT + "/three.html");
        }
        catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

    public static HashMap<String, List<String>> getAccessRoles() {

        HashMap<String, List<String>> accessRoles = new HashMap<String, List<String>>();

        List<String> pageOneRoles = new ArrayList<String>();
        pageOneRoles.add(Roles.PAGE_ONE.getRole());
        pageOneRoles.add(Roles.ADMIN.getRole());
        accessRoles.put(Server.PAGE_ONE, pageOneRoles);

        List<String> pageTwoRoles = new ArrayList<String>();
        pageTwoRoles.add(Roles.PAGE_TWO.getRole());
        pageTwoRoles.add(Roles.ADMIN.getRole());
        accessRoles.put(Server.PAGE_TWO, pageTwoRoles);

        List<String> pageThreeRoles = new ArrayList<String>();
        pageThreeRoles.add(Roles.PAGE_THREE.getRole());
        pageThreeRoles.add(Roles.ADMIN.getRole());
        accessRoles.put(Server.PAGE_THREE, pageThreeRoles);

        return accessRoles;
    }
}
