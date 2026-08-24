package springaipractice.newportfolio.Services;


import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class YouTubeService {
    private final String API_KEY = "AIzaSyC3QCNQJIh_2dn26g1xuvK9YEVIUqyy8B8"; // Replace this
    private final String CHANNEL_ID = "UCtnZGQV0SRJm7ONgFqz5CMA"; // Replace this
    private final String BASE_URL = "https://www.googleapis.com/youtube/v3";

    public List<Map<String, Object>> getPlaylists() {
        String url = String.format("%s/playlists?part=snippet,contentDetails&channelId=%s&maxResults=50&key=%s",
                BASE_URL, CHANNEL_ID, API_KEY);
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        return (List<Map<String, Object>>) response.get("items");
    }

    public List<Map<String, Object>> getPlaylistVideos(String playlistId) {
        List<Map<String, Object>> allVideos = new ArrayList<>();
        String nextPageToken = null;
        RestTemplate restTemplate = new RestTemplate();

        do {
            // Construct the URL. If nextPageToken exists, append it to the URL
            String url = String.format("%s/playlistItems?part=snippet&playlistId=%s&maxResults=50&key=%s",
                    BASE_URL, playlistId, API_KEY);

            if (nextPageToken != null) {
                url += "&pageToken=" + nextPageToken;
            }

            // Fetch data from YouTube
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null) {
                // 1. Extract the videos (items) from the current page
                List<Map<String, Object>> items = (List<Map<String, Object>>) response.get("items");
                if (items != null) {
                    allVideos.addAll(items);
                }

                // 2. Check if there is another page
                nextPageToken = (String) response.get("nextPageToken");
            } else {
                nextPageToken = null; // Stop if response is null
            }

        } while (nextPageToken != null); // Keep looping as long as there is a next page

        return allVideos;
    }
}
