package controllers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 * Created by ivanmolera on 27/5/17.
 */
public class DefaultController implements HttpHandler {

    private StringBuilder response;
    public StringBuilder getResponse() { return response; }
    public void setResponse(StringBuilder response) { this.response = response; }

    public void handle(HttpExchange t) throws IOException {
        InputStream is = t.getRequestBody();
        is.read(); // read request body

        response = new StringBuilder();

        response.append("This is the default handler");
        t.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length());

        OutputStream os = t.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();
    }
}
