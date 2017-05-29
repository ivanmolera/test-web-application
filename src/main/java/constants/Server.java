package constants;

/**
 * Created by ivanmolera on 27/5/17.
 */
public class Server {
    public final static int SERVER_PORT             = 8000;
    public final static int BACKLOG                 = 0;

    public final static String DEFAULT_CONTEXT      = "/schibsted/test";
    public final static String USER_CONTEXT         = "/schibsted/user";
    public final static String PAGE_CONTEXT         = "/schibsted/page";

    public final static String PAGE_ONE             = PAGE_CONTEXT + "/one.html";
    public final static String PAGE_TWO             = PAGE_CONTEXT + "/two.html";
    public final static String PAGE_THREE           = PAGE_CONTEXT + "/three.html";
    public final static String PAGE_LOGOUT          = PAGE_CONTEXT + "/logout.html";

    public final static String USER_REALM           = "user";
    public final static String PAGE_REALM           = "page";

    public final static String PAGE_TEMPLATE        = "<html><body>Hello {{user.username}}<br/><br/><a href=\"" + PAGE_LOGOUT + "\">Logout</a></body></html>";

    public final static String COOKIE_ID            = "Cookie";
}
