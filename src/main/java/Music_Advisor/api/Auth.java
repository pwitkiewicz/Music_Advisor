package Music_Advisor.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class Auth {
    private static boolean authorized = false;

    private static String authCode = "";
    private static String accessToken = "";

    private static String redirectURL = "http://localhost:8080";
    private static String authServer = "https://accounts.spotify.com";

    private static final String CLIENT_ID = "1a3b4e258da845a4b2478cb266af39d0";
    private static final String CLIENT_SECRET = "d9d6f245e2e3456dbda2d3aef2a2d84a";


    public static boolean isAuthorized() {
        return authorized;
    }

    public static void setAuthServer(String address) {
        authServer = address;
    }

    static String getAccessToken() {
        return accessToken;
    }

    public static void init() throws IOException, InterruptedException {
        System.out.println("use this link to request the access code:" +
                "\n" + generateRequestURL() +
                "\n" + "waiting for code...");

        listenForQuery();

        if (authCode.isEmpty()) {
            return;
        } else {
            authorized = true;
        }

        System.out.println("code received" +
                "\n" + "making http request for access_token...");
        requestToken();
    }

    private static void listenForQuery() throws IOException {
        // start the server & listen incoming TCP connections on 8080 port
        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(8080), 0);
        server.start();

        server.createContext("/",
                new HttpHandler() {
                    public void handle(HttpExchange exchange) throws IOException {
                        String query = "";
                        query += exchange.getRequestURI().getQuery();

                        String response = "Not found authorization code. Try again.";


                        if (query.contains("code")) {
                            response = "Got the code. Return back to your program.";
                        }

                        exchange.sendResponseHeaders(200, response.length());
                        exchange.getResponseBody().write(response.getBytes());
                        exchange.getResponseBody().close();

                        authCode = query.substring(5);
                    }
                }
        );

        // wait for the query
        while (authCode.isEmpty()) {
            Thread.onSpinWait();
        }

        // stop the server
        server.stop(1);
    }

    private static void requestToken() throws IOException, InterruptedException {
        // create HttpClient instance
        HttpClient client = HttpClient.newBuilder().build();

        // encode client credentials
        String toEncode = CLIENT_ID + ":" + CLIENT_SECRET;
        String credentials = Base64.getEncoder().encodeToString(toEncode.getBytes());

        // create POST request for accessToken
        HttpRequest request = HttpRequest.newBuilder()
                .headers("Content-Type", "application/x-www-form-urlencoded",
                        "Authorization", "Basic " + credentials)
                .uri(URI.create(authServer + "/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString(
                        "grant_type=authorization_code" +
                                "&code=" + authCode +
                                "&redirect_uri=" + redirectURL))
                .build();

        // send & catch http response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("response:" + response.body());

        // save accessToken
        JsonObject responseParsed = JsonParser.parseString(response.body()).getAsJsonObject();
        accessToken = responseParsed.get("access_token").getAsString();
    }

    private static String generateRequestURL() {
        return authServer + "/authorize?" +
                "client_id=" + CLIENT_ID +
                "&redirect_uri=" + redirectURL +
                "&response_type=code";
    }
}
