package springaipractice.newportfolio.Models;



import java.util.List;

public class QuizRequest {
    private List<String> languages;

    public String getDifficulty() {
        return difficulty;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    private String difficulty;


}