package springaipractice.newportfolio.Controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springaipractice.newportfolio.Models.QuizRequest;
import springaipractice.newportfolio.Services.QuizEvaluationService;
import springaipractice.newportfolio.Services.QuizService;




import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
public class QuizController {

    private final QuizService quizService;

    private final QuizEvaluationService quizEvaluationService;

    public QuizController(QuizService quizService, QuizEvaluationService quizEvaluationService) {
        this.quizService = quizService;
        this.quizEvaluationService = quizEvaluationService;
    }

    @PostMapping("/generate-quiz")
    public String generateQuiz(@RequestBody QuizRequest request,
                               HttpSession session) {

        String quizJson = quizService.generateQuiz(
                String.join(", ", request.getLanguages()),
                request.getDifficulty()
        );

        if (quizJson == null || quizJson.isBlank()) {
            session.setAttribute("quizData", "");
            return "FAILED";
        }

        session.setAttribute("quizData", quizJson);

        return "OK";
    }

    @GetMapping("/quiz-data")
    public String getQuiz(HttpSession session) {

        Object data = session.getAttribute("quizData");

        if (data == null || data.toString().isBlank()) {
            return "";
        }

        return data.toString();
    }

    /* =====================
     SUBMIT ANSWERS + SCORE
  ===================== */
    @PostMapping("/submit-quiz")
    public Map<String, Object> submitQuiz(
            @RequestBody Map<Integer, String> answers,
            HttpSession session) {

        String quiz = (String) session.getAttribute("quizData");

        if (quiz == null) {
            return Map.of("error", "No quiz found");
        }

        int score = quizEvaluationService.calculateScore(quiz, answers);

        return Map.of(
                "score", score,
                "status", "completed"
        );
    }
}