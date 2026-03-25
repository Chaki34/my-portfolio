package springaipractice.newportfolio.Repos;




import org.springframework.data.jpa.repository.JpaRepository;
import springaipractice.newportfolio.Models.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}