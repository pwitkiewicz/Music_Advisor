public class Auth {
    private static boolean authStatus = false;
    private static final String clientID = "1a3b4e258da845a4b2478cb266af39d0";
    private static final String redirectURL = "http://localhost:8080";


    public static boolean getStatus() {
        return authStatus;
    }

    public static String sendRequest() {

        String request = "https://accounts.spotify.com/authorize?" +
                "client_id=" + clientID +
                "&redirect_uri=" + redirectURL +
                "&response_type=code";

        authStatus = true;

        return request;
    }
}
