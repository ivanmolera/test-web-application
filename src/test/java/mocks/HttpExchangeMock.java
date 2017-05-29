package mocks;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;
import constants.Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by ivanmolera on 28/5/17.
 */
public class HttpExchangeMock extends HttpExchange {

    private HttpPrincipal currentUser;
    private String requestMethod;
    private URI requestURI;
    
    public Headers getRequestHeaders() {
        return new Headers();
    }

    public Headers getResponseHeaders() {
        return new Headers();
    }

    public void setRequestURI(String uri) {
        try {
            requestURI = new URI(uri);
        }
        catch (URISyntaxException e) {

        }
    }

    public URI getRequestURI() {
        return requestURI;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getRequestMethod() {
        return this.requestMethod;
    }

    public HttpContext getHttpContext() {
        return null;
    }

    public void close() {

    }

    public InputStream getRequestBody() {
        return new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
    }

    public OutputStream getResponseBody() {
        return new OutputStream() {
            @Override
            public void write(int b) throws IOException {

            }
        };
    }

    public void sendResponseHeaders(int i, long l) throws IOException {

    }

    public InetSocketAddress getRemoteAddress() {
        return null;
    }

    public int getResponseCode() {
        return 0;
    }

    public InetSocketAddress getLocalAddress() {
        return null;
    }

    public String getProtocol() {
        return null;
    }

    public Object getAttribute(String s) {
        return null;
    }

    public void setAttribute(String s, Object o) {

    }

    public void setStreams(InputStream inputStream, OutputStream outputStream) {

    }

    public void setCurrentUser(String username) {
        currentUser = new HttpPrincipal(username, Server.USER_REALM);
    }

    public HttpPrincipal getPrincipal() {
        return currentUser;
    }
}
