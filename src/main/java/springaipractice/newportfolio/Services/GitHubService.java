package springaipractice.newportfolio.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class GitHubService {

    private final WebClient webClient;
    private final String username;

    // ✅ Constructor Injection (clean)
    public GitHubService(
            @Value("${github.api.base-url}") String baseUrl,
            @Value("${github.token:}") String token,
            @Value("${github.username}") String username) {

        this.username = username;

        WebClient.Builder builder = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Accept", "application/vnd.github+json");

        // ✅ Add Authorization header ONLY if token exists
        if (token != null && !token.isBlank()) {
            builder.defaultHeader("Authorization", "Bearer " + token);
        }

        this.webClient = builder.build();
    }

    // 🔹 Get Profile
    public Map<String, Object> getUserProfile() {
        try {
            return webClient.get()
                    .uri("/users/{username}", username)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    // 🔹 Get Repositories
    public List<Map<String, Object>> getRepos() {
        try {
            return webClient.get()
                    .uri("/users/{username}/repos?sort=updated", username)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                    .block();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    // 🔹 Get Commits (per repo)
    public List<Map<String, Object>> getCommits(String repoName) {
        try {
            return webClient.get()
                    .uri("/repos/{owner}/{repo}/commits?per_page=5", username, repoName)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                    .block();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    // 🔹 Get Contribution-like Data (events)
    public List<Map<String, Object>> getEvents() {
        try {
            return webClient.get()
                    .uri("/users/{username}/events", username)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                    .block();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }



    public Map<String, Long> getRepoLanguages(String repoName) {
        try {
            return webClient.get()
                    .uri("/repos/{owner}/{repo}/languages", username, repoName)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Long>>() {})
                    .block();
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }
}