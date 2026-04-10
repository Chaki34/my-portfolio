package springaipractice.newportfolio.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import springaipractice.newportfolio.Services.GitHubService;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class GitHubController {

    private final GitHubService gitHubService;

    public GitHubController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @GetMapping("/portfolio")
    public String githubPortfolio(Model model) {
        // 1. Fetch User Profile
        Map<String, Object> user = safeMap(gitHubService.getUserProfile());

        // 2. Fetch Repositories
        List<Map<String, Object>> repos = safeList(gitHubService.getRepos());

        // 3. Fetch Activity Events (Crucial: Fixes the 'null' error)
        List<Map<String, Object>> events = safeList(gitHubService.getEvents());

        long totalStars = 0;
        Map<String, Long> languageStats = new HashMap<>();

        // 4. Process Repositories (Limit details to top 6 to prevent API timeout)
        int processLimit = Math.min(repos.size(), 6);
        for (int i = 0; i < repos.size(); i++) {
            Map<String, Object> repo = repos.get(i);

            // Accumulate Stars safely
            Object starCount = repo.get("stargazers_count");
            if (starCount instanceof Number) {
                totalStars += ((Number) starCount).longValue();
            }

            // Only fetch languages and commits for the top featured repos (Optimization)
            if (i < processLimit) {
                String repoName = (String) repo.get("name");

                // Fetch Languages
                Map<String, Long> languages = gitHubService.getRepoLanguages(repoName);
                if (languages != null) {
                    languages.forEach((lang, bytes) -> languageStats.merge(lang, bytes, Long::sum));
                }

                // Fetch Commits for top 3
                if (i < 3) {
                    repo.put("commits", safeList(gitHubService.getCommits(repoName)));
                } else {
                    repo.put("commits", Collections.emptyList());
                }
            }
        }

        // 5. Calculate Productivity Percentages
        long totalBytes = languageStats.values().stream().mapToLong(Long::longValue).sum();
        List<Map<String, Object>> topLanguages = languageStats.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .map(entry -> {
                    Map<String, Object> langMap = new HashMap<>();
                    langMap.put("name", entry.getKey());
                    // Calculate percentage
                    long percentage = (totalBytes > 0) ? (entry.getValue() * 100 / totalBytes) : 0;
                    langMap.put("percentage", percentage);
                    return langMap;
                })
                .collect(Collectors.toList());

        // 6. Add all data to Model (Always ensure objects aren't null)
        model.addAttribute("user", user);
        model.addAttribute("repos", repos);
        model.addAttribute("events", events); // Fixed: Now passed to Thymeleaf
        model.addAttribute("totalStars", totalStars);
        model.addAttribute("topLanguages", topLanguages);

        // Fallback for username if profile call failed
        model.addAttribute("username", user.getOrDefault("login", "unknown"));

        return "github-portfolio";
    }

    // ✅ Helper: Ensure Map is never null
    private Map<String, Object> safeMap(Map<String, Object> data) {
        return data != null ? data : new HashMap<>();
    }

    // ✅ Helper: Ensure List is never null
    private List<Map<String, Object>> safeList(List<Map<String, Object>> data) {
        return data != null ? data : new ArrayList<>();
    }
}