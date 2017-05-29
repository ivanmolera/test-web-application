package controllers;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import constants.Roles;
import constants.Server;
import model.User;
import model.dao.UserDAO;
import model.dao.UserRoleDAO;
import util.Views;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Created by ivanmolera on 27/5/17.
 */
public class UserController implements HttpHandler {

    private StringBuilder response;
    public StringBuilder getResponse() { return response; }
    public void setResponse(StringBuilder response) { this.response = response; }

    private UserDAO userDAO;
    public void setUserDAO(UserDAO userDAO) { this.userDAO = userDAO; }

    private UserRoleDAO userRoleDAO;
    public void setUserRoleDAO(UserRoleDAO userRoleDAO) { this.userRoleDAO = userRoleDAO; }

    public UserController() {
        response = new StringBuilder();
    }

    public void handle(HttpExchange exchange) throws IOException {

        Headers header = exchange.getResponseHeaders();
        header.add("Content-Type", "application/json");

        String paramUsername = parseLastQueryParam(exchange.getRequestURI().toString());;
        response = new StringBuilder();

        if (exchange.getRequestMethod().equals("GET")) {
            getUser(paramUsername, exchange);
        }
        else {
            User currentUser = userDAO.findByUsername(exchange.getPrincipal().getUsername());

            // User has permission?
            if (currentUser.hasRole(Roles.ADMIN.getRole())) {

                InputStream is = exchange.getRequestBody();
                is.read();
                String request = readRequestBody(is);

                // Well formed request?
                if(!"".equals(paramUsername) || isValidJSON(request)) {
                        User user = null;
                        if("".equals(paramUsername)) {
                            ObjectMapper mapper = new ObjectMapper();
                            user = mapper.readValue(request, User.class);
                        }

                        if (exchange.getRequestMethod().equals("POST"))
                        {
                            addUser(user, exchange);
                        }
                        else if (exchange.getRequestMethod().equals("PUT"))
                        {
                            updateUser(user, exchange);
                        }
                        else if (exchange.getRequestMethod().equals("DELETE"))
                        {
                            deleteUser(paramUsername, exchange);
                        }
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, response.length());
                }
            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_FORBIDDEN, response.length());
            }
        }

        writeResponse(exchange);
    }

    public static String parseLastQueryParam(String query) {
        String param = "";
        String[] splittedQuery = query.split("/");
        for(int i=0; i<=splittedQuery.length; i++) {
            if(splittedQuery[i].equalsIgnoreCase(Server.USER_REALM)) {
                try {
                    param = splittedQuery[i+1];
                }
                catch (IndexOutOfBoundsException e) {

                }
                break;
            }
        }
        return param;
    }

    public void writeResponse(HttpExchange exchange) throws IOException {
        OutputStream os = exchange.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();
    }

    public User getUser(String username, HttpExchange exchange) throws IOException {
        User user = null;
        try {
            user = userDAO.findByUsername(username);

            ObjectMapper mapper = new ObjectMapper();
            String jsonInString = mapper.writerWithView(Views.Normal.class).writeValueAsString(user);
            response.append(jsonInString);

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length());
        }
        catch (Exception e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_CONFLICT, response.length());
        }
        return user;
    }

    public void addUser(User user, HttpExchange exchange) throws IOException {
        try {
            userDAO.add(user);
            for(Iterator it = user.getRoles().iterator(); it.hasNext();) {
                userRoleDAO.add(user.getUsername(), (String)it.next());
            }
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length());
        }
        catch(Exception e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_CONFLICT, response.length());
        }
    }

    public void updateUser(User user, HttpExchange exchange) throws IOException {
        try {
            userDAO.update(user);
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length());
        }
        catch(Exception e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_CONFLICT, response.length());
        }
    }

    public void deleteUser(String username, HttpExchange exchange) throws IOException {
        if(!username.equalsIgnoreCase(exchange.getPrincipal().getUsername())) {
            try {
                userDAO.delete(username);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length());
            }
            catch(Exception e) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_CONFLICT, response.length());
            }
        }
        else {
            response.append("Cannot delete yourself");
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_CONFLICT, response.length());
        }
    }

    private String readRequestBody(InputStream requestBody) throws IOException {
        Scanner s = new Scanner(requestBody).useDelimiter("\\A");
        return s.hasNext() ? "{" + s.next() : "";
    }

    public boolean isValidJSON(String json) {
        boolean valid = false;
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.readTree(json);
            valid = true;
        } catch (JsonParseException jpe) {

        } catch (IOException ioe) {

        }
        return valid;
    }
}
