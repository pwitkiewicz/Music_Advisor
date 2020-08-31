package Music_Advisor.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.URI;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class Client {
    private static ArrayList<String> categoryIDs = new ArrayList();
    private static String apiServerPath = "https://api.spotify.com";

    public static void setApiServerPath(String path) {
        apiServerPath = path;
    }

    public static JsonArray getNewReleases() throws IOException, InterruptedException {
        String apiPath = "/v1/browse/new-releases";

        return sendGETRequest(apiServerPath + apiPath)
                .get("albums").getAsJsonObject().get("items").getAsJsonArray();
    }

    public static JsonArray getFeatured() throws IOException, InterruptedException {
        String apiPath = "/v1/browse/featured-playlists";

        return sendGETRequest(apiServerPath + apiPath)
                .get("playlists").getAsJsonObject().get("items").getAsJsonArray();
    }

    public static JsonArray getCategories() throws IOException, InterruptedException {
        String apiPath = "/v1/browse/categories";
        JsonArray categories = sendGETRequest(apiServerPath + apiPath)
                .get("categories").getAsJsonObject().get("items").getAsJsonArray();

        // saving categories
        for (int i = 0; i < categories.size(); i++) {
            categoryIDs.add(categories.get(i).getAsJsonObject().get("id").toString().replace("\"", ""));
        }

        return categories;
    }

    public static JsonArray getCategoryPlaylists(String category) throws IOException, InterruptedException {
        // checking if categories list is populated, populating if not
        if(categoryIDs.isEmpty()) {
            getCategoriesIDs();
        }

        // convertging category name to category id
        String id = category.replaceAll("\\s+", "").toLowerCase();

        if(id.equals("partytime")) {
            id = "party";
        }

        if(!categoryIDs.contains(id)) {
            System.out.println("Unknown category name.");
            return null;
        }

        String apiPath = "/v1/browse/categories/" + id + "/playlists";

        JsonObject response = sendGETRequest(apiServerPath + apiPath);
        JsonArray categoryPlaylists = null;

        if(response.toString().contains("error")) {
            JsonObject error = response.get("error").getAsJsonObject();
            System.out.println(error.get("message").toString());
            return null;
        } else {
            categoryPlaylists = response.get("playlists").getAsJsonObject().get("items").getAsJsonArray();
        }

        return categoryPlaylists;
    }

    private static void getCategoriesIDs() throws IOException, InterruptedException {
        String apiPath = "/v1/browse/categories";
        JsonArray categories = sendGETRequest(apiServerPath + apiPath)
                .get("categories").getAsJsonObject().get("items").getAsJsonArray();

        for (int i = 0; i < categories.size(); i++) {
            categoryIDs.add(categories.get(i).getAsJsonObject().get("id").toString().replace("\"", ""));
        }
    }

    private static JsonObject sendGETRequest(String apiPath) throws IOException, InterruptedException {
        // create HttpClient instance
        HttpClient client = HttpClient.newBuilder().build();

        // create GET request
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + Auth.getAccessToken())
                .uri(URI.create(apiPath))
                .GET()
                .build();

        // catch response
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        // parse and return JSON object
        return JsonParser.parseString(response.body()).getAsJsonObject();
    }
}
