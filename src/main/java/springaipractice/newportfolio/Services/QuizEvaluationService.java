package springaipractice.newportfolio.Services;


import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

@Service
public class QuizEvaluationService {

    private final ObjectMapper mapper = new ObjectMapper();

    /* ==========================
       SCORE CALCULATION ONLY
    ========================== */
    public int calculateScore(String quizJson, Map<Integer, String> userAnswers) {

        try {
            JsonNode root = mapper.readTree(quizJson);
            JsonNode questions = root.path("questions");

            int score = 0;

            for (int i = 0; i < questions.size(); i++) {

                JsonNode q = questions.get(i);

                String correctAnswer = q.path("answer").asText("");
                String userAnswer = userAnswers.getOrDefault(i, "");

                if (correctAnswer.equalsIgnoreCase(userAnswer)) {
                    score++;
                }
            }

            return score;

        } catch (Exception e) {
            System.err.println("❌ Score calculation failed: " + e.getMessage());
            return 0;
        }
    }

    /* ==========================
       FULL RESULT (RECOMMENDED)
       → returns correct + user answers
    ========================== */
    public Map<String, Object> evaluateQuiz(String quizJson, Map<Integer, String> userAnswers) {

        Map<String, Object> response = new HashMap<>();

        try {
            JsonNode root = mapper.readTree(quizJson);
            JsonNode questions = root.path("questions");

            int score = 0;

            for (int i = 0; i < questions.size(); i++) {

                JsonNode q = questions.get(i);

                String question = q.path("question").asText("");
                String correctAnswer = q.path("answer").asText("");
                String userAnswer = userAnswers.getOrDefault(i, "");

                boolean isCorrect = correctAnswer.equalsIgnoreCase(userAnswer);

                if (isCorrect) score++;

                response.put("q" + i, Map.of(
                        "question", question,
                        "correct", correctAnswer,
                        "selected", userAnswer,
                        "status", isCorrect ? "Correct" : "Wrong"
                ));
            }

            response.put("score", score);
            response.put("total", questions.size());

        } catch (Exception e) {
            response.put("error", "Parsing failed: " + e.getMessage());
        }

        return response;
    }
}