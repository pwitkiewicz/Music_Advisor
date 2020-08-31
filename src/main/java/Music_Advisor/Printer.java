package Music_Advisor;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Printer {
    public static void printAlbums(JsonArray albums, int printCounter) {
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

    public static void printPlaylists(JsonArray playlists, int printCounter) {
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

    public static void printCategories(JsonArray categories) {
        for (int i = 0; i < categories.size(); i++) {
            System.out.println(categories.get(i).getAsJsonObject().get("name").toString().replace("\"", ""));
        }
    }
}
