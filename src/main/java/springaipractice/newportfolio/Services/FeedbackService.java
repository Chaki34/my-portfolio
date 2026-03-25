package springaipractice.newportfolio.Services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springaipractice.newportfolio.Models.Feedback;
import springaipractice.newportfolio.Repos.FeedbackRepository;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    public Feedback saveFeedback(int rating) {
        Feedback feedback = new Feedback(rating);
        return feedbackRepository.save(feedback);
    }
}
