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

    private static void printAlbums(JsonArray albums, int printCounter) {
        if(albums.size() < printCounter) {
            printCounter = albums.size();
        }

        for (int i = 0; i < printCounter; i++) {
            JsonObject album = albums.get(i).getAsJsonObject();
            // album title
            System.out.println(album.get("name").getAsString());

            // artists
            StringBuilder artists = new StringBuilder("[");
            JsonArray artistsArr = album.get("artists").getAsJsonArray();
            for (int j = 0; j < artistsArr.size(); j++) {
                artists.append(artistsArr.get(j)
                        .getAsJsonObject().get("name").toString().replace("\"", ""));
                if(j < artistsArr.size() - 1) {
                    artists.append(", ");
                }
            }
            artists.append("]");
            System.out.println(artists.toString());

            // spotify link
            System.out.println(album.get("external_urls")
                    .getAsJsonObject().get("spotify").toString().replace("\"", "") + "\n");
        }
    }

    private static void printPlaylists(JsonArray playlists, int printCounter) {
        if(playlists.size() < printCounter) {
            printCounter = playlists.size();
        }

        for (int i = 0; i < printCounter; i++) {
            System.out.println(playlists.get(i)
                    .getAsJsonObject().get("name").toString().replace("\"", ""));
            System.out.println(playlists.get(i).getAsJsonObject().get("external_urls").
                    getAsJsonObject().get("spotify").toString().replace("\"", "") + "\n");
        }
    }

    public static void getNewReleases() throws IOException, InterruptedException {
        String apiPath = "/v1/browse/new-releases";
        JsonArray newReleases = sendGETRequest(apiServerPath + apiPath)
                .get("albums").getAsJsonObject().get("items").getAsJsonArray();

        // printing 7 new releases
        printAlbums(newReleases, 7);
    }

    public static void getFeatured() throws IOException, InterruptedException {
        String apiPath = "/v1/browse/featured-playlists";
        JsonArray featuredPlaylists = sendGETRequest(apiServerPath + apiPath)
                .get("playlists").getAsJsonObject().get("items").getAsJsonArray();

        // printing 7 featured playlists
        printPlaylists(featuredPlaylists, 7);
    }

    public static void getCategories() throws IOException, InterruptedException {
        String apiPath = "/v1/browse/categories";
        JsonArray categories = sendGETRequest(apiServerPath + apiPath)
                .get("categories").getAsJsonObject().get("items").getAsJsonArray();

        // printing categories
        for (int i = 0; i < categories.size(); i++) {
            categoryIDs.add(categories.get(i).getAsJsonObject().get("id").toString().replace("\"", ""));
            System.out.println(categories.get(i).getAsJsonObject().get("name").toString().replace("\"", ""));
        }
    }

    public static void getCategoryPlaylists(String category) throws IOException, InterruptedException {
        // checking if categories list is populated, populating if not
        if(categoryIDs.isEmpty()) {
            getCategoriesIDs();
        }

        // convertging category name to category id
        String id = category.replaceAll("\\s+", "").toLowerCase();

        if(!categoryIDs.contains(id)) {
            System.out.println("Unknown category name.");
            return;
        }

        String apiPath = "/v1/browse/categories/" + id + "/playlists";

        JsonObject response = sendGETRequest(apiServerPath + apiPath);
        JsonArray categoryPlaylists = null;

        if(response.toString().contains("error")) {
            JsonObject error = response.get("error").getAsJsonObject();
            System.out.println(error.get("message").toString());
            return;
        } else {
            categoryPlaylists = response.get("playlists").getAsJsonObject().get("items").getAsJsonArray();
        }

        // print 5 category playlists
        printPlaylists(categoryPlaylists, 5);
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
        JsonObject responseParsed = JsonParser.parseString(response.body()).getAsJsonObject();
        return responseParsed;
    }
}
