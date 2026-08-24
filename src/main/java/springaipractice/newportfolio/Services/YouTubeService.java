package springaipractice.newportfolio.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class YouTubeService {

    @Value("${youtube.api.key}")
    private String apiKey;

    @Value("${youtube.channel.id}")
    private String channelId;

    @Value("${youtube.api.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<Map<String, Object>> getPlaylists() {

        String url = String.format(
                "%s/playlists?part=snippet,contentDetails&channelId=%s&maxResults=50&key=%s",
                baseUrl,
                channelId,
                apiKey
        );

        Map<String, Object> response =
                restTemplate.getForObject(url, Map.class);

        if (response == null || response.get("items") == null) {
            return new ArrayList<>();
        }

        return (List<Map<String, Object>>) response.get("items");
    }

    public List<Map<String, Object>> getPlaylistVideos(String playlistId) {

        List<Map<String, Object>> allVideos = new ArrayList<>();
        String nextPageToken = null;

        do {
            String url = String.format(
                    "%s/playlistItems?part=snippet,contentDetails&playlistId=%s&maxResults=50&key=%s",
                    baseUrl,
                    playlistId,
                    apiKey
            );

            if (nextPageToken != null && !nextPageToken.isBlank()) {
                url += "&pageToken=" + nextPageToken;
            }

            Map<String, Object> response =
                    restTemplate.getForObject(url, Map.class);

            if (response == null) {
                break;
            }

            List<Map<String, Object>> items =
                    (List<Map<String, Object>>) response.get("items");

            if (items != null) {
                allVideos.addAll(items);
            }

            nextPageToken =
                    (String) response.get("nextPageToken");

        } while (nextPageToken != null && !nextPageToken.isBlank());

        return allVideos;
    }
}