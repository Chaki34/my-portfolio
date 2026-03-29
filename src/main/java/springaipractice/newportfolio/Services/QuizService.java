package springaipractice.newportfolio.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;





import java.util.Map;

@Service
public class QuizService {

    private final RestClient restClient;
    private final String apiKey;
    private final ObjectMapper mapper = new ObjectMapper();

    public QuizService(RestClient.Builder builder,
                       @Value("${OPENAI_API_KEY}") String apiKey) {

        this.apiKey = apiKey;

        this.restClient = builder
                .baseUrl("https://openrouter.ai/api/v1")
                .build();
    }

    public String generateQuiz(String languages, String difficulty) {

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("OPENROUTER API KEY missing");
        }

        String prompt = """
                Generate 10 MCQ questions in STRICT JSON format only.

                Format:
                {
                  "questions": [
                    {
                      "question": "",
                      "options": ["A","B","C","D"],
                      "answer": ""
                    }
                  ]
                }

                Languages: %s
                Difficulty: %s
                """.formatted(languages, difficulty);

        Map<String, Object> body = Map.of(
                "model", "openai/gpt-4o-mini",
                "messages", new Object[]{
                        Map.of("role", "user", "content", prompt)
                }
        );

        try {
            String response = restClient.post()
                    .uri("/chat/completions")
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .header("HTTP-Referer", "http://localhost:8080")
                    .header("X-Title", "Quiz AI")
                    .body(body)
                    .retrieve()
                    .body(String.class);

            if (response == null || response.isBlank()) {
                return "";
            }

            // 🔥 STEP 1: Extract AI content
            JsonNode root = mapper.readTree(response);

            String content = root
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

            if (content == null || content.isBlank()) {
                return "";
            }

            // 🔥 STEP 2: remove markdown ```json
            content = content
                    .replace("```json", "")
                    .replace("```", "")
                    .trim();

            // 🔥 STEP 3: validate JSON
            JsonNode quizJson = mapper.readTree(content);

            // return clean JSON string
            return mapper.writeValueAsString(quizJson);

        } catch (Exception e) {
            System.err.println("❌ Quiz generation failed: " + e.getMessage());
            return "";
        }
    }
}