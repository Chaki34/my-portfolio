package springaipractice.newportfolio.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.*;

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

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
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

            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response == null) {
                break;
            }

            List<Map<String, Object>> items = (List<Map<String, Object>>) response.get("items");
            if (items != null) {
                allVideos.addAll(items);
            }

            nextPageToken = (String) response.get("nextPageToken");
        } while (nextPageToken != null && !nextPageToken.isBlank());

        return allVideos;
    }

    // Calculates total playlist duration in seconds
    public double getTotalPlaylistDurationInSeconds(List<Map<String, Object>> videos) {
        if (videos == null || videos.isEmpty()) return 0.0;

        List<String> videoIds = new ArrayList<>();
        for (Map<String, Object> v : videos) {
            Map<String, Object> snippet = (Map<String, Object>) v.get("snippet");
            if (snippet != null) {
                Map<String, Object> resId = (Map<String, Object>) snippet.get("resourceId");
                if (resId != null && resId.get("videoId") != null) {
                    videoIds.add((String) resId.get("videoId"));
                }
            }
        }

        double totalSeconds = 0;
        // Batch in groups of 50
        for (int i = 0; i < videoIds.size(); i += 50) {
            List<String> batch = videoIds.subList(i, Math.min(i + 50, videoIds.size()));
            String idsParam = String.join(",", batch);
            String url = String.format("%s/videos?part=contentDetails&id=%s&key=%s", baseUrl, idsParam, apiKey);

            try {
                Map<String, Object> response = restTemplate.getForObject(url, Map.class);
                if (response != null && response.get("items") != null) {
                    List<Map<String, Object>> items = (List<Map<String, Object>>) response.get("items");
                    for (Map<String, Object> item : items) {
                        Map<String, Object> cd = (Map<String, Object>) item.get("contentDetails");
                        if (cd != null && cd.get("duration") != null) {
                            String isoDuration = (String) cd.get("duration");
                            totalSeconds += Duration.parse(isoDuration).toSeconds();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return totalSeconds;
    }
}