package controllers;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import constants.Server;
import model.User;
import model.dao.UserDAO;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ivanmolera on 27/5/17.
 */
public class PageController implements HttpHandler {

    private UserDAO userDAO;
    public void setUserDAO(UserDAO userDAO) { this.userDAO = userDAO; }

    private HashMap<String, List<String>> accessRoles = new HashMap<String, List<String>>();
    public void setAccessRoles(HashMap<String, List<String>> accessRoles) { this.accessRoles = accessRoles; }

    private StringBuilder response;
    public StringBuilder getResponse() { return response; }
    public void setResponse(StringBuilder response) { this.response = response; }

    public void handle(HttpExchange exchange) throws IOException {

        Headers header = exchange.getResponseHeaders();
        header.add("Content-Type", "text/html");

        // Cookie to handle session timeout
        String cookie = exchange.getRequestHeaders().getFirst(Server.COOKIE_ID);
        if(cookie != null) {
            if(!cookieIsValid(cookie)) {
                response.append("Bye!\n");
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
                writeResponse(exchange);
                exchange.close();
            }
        }
        else {
            header.add(Server.COOKIE_ID, createCookie());
        }

        response = new StringBuilder();

        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        System.out.println("User: " + exchange.getPrincipal().getUsername() + " looking for: " + path);

        if(!path.equalsIgnoreCase(Server.PAGE_LOGOUT)) {
            List<String> pageRoles = accessRoles.get(path);
            if (pageRoles != null && pageRoles.size() > 0) {
                User user = userDAO.findByUsername(exchange.getPrincipal().getUsername());

                if (userHasAccessToPage(pageRoles, user.getRoles())) {
                    response = buildPageContent(user);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length());
                } else {
                    response.append("403 (Forbidden)\n");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_FORBIDDEN, 0);
                }
            }
            else
            {
                response.append("404 (Not Found)\n");
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
            }
            writeResponse(exchange);
        }
        else
        {
            response.append("Bye!\n");
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
            writeResponse(exchange);
            exchange.close();
        }
    }

    public String createCookie() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        return df.format(calendar.getTime());
    }

    public boolean cookieIsValid(String cookieDateTime) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        boolean isValid = false;
        try {
            Calendar now = Calendar.getInstance();
            Calendar cookieTime = Calendar.getInstance();

            cookieTime.setTime(df.parse(cookieDateTime));
            isValid = now.compareTo(cookieTime) > 0;
        }
        catch (ParseException pe) {
            System.out.println(pe.getMessage());
        }
        return isValid;
    }

    private void writeResponse(HttpExchange exchange) throws IOException {
        OutputStream os = exchange.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();
    }

    public StringBuilder buildPageContent(User user) throws IOException {
        StringBuilder response = new StringBuilder();

        HashMap<String, Object> scopes = new HashMap<String, Object>();
        scopes.put("user", user);

        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile(new StringReader(Server.PAGE_TEMPLATE), "page");

        StringWriter writer = new StringWriter();
        mustache.execute(writer, scopes);
        writer.flush();

        response.append(writer.toString());

        return response;
    }

    public boolean userHasAccessToPage(List<String> pageRoles, List<String> userRoles) {
        boolean hasAccess = false;
        for(Iterator it = userRoles.iterator(); it.hasNext();) {
            if(pageRoles.contains(it.next())) {
                hasAccess = true;
                break;
            }
        }
        return hasAccess;
    }
}
