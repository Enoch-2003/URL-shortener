import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class URLShortenerServer {
    public static void startServer(URLShortenerService service) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
            server.createContext("/", new RedirectHandler(service));
            server.setExecutor(null); // creates a default executor
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class RedirectHandler implements HttpHandler {
        private URLShortenerService service;

        public RedirectHandler(URLShortenerService service) {
            this.service = service;
        }

        @Override
        public void handle(HttpExchange t) throws IOException {
            String path = t.getRequestURI().getPath().substring(1);
            String longUrl = service.expandURL("http://localhost:8000/" + path);
            if (longUrl.equals("Invalid short URL")) {
                String response = "404 Not Found";
                t.sendResponseHeaders(404, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                t.getResponseHeaders().set("Location", longUrl);
                t.sendResponseHeaders(302, -1);
            }
        }
    }
}
