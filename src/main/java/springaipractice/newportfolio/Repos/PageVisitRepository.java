package springaipractice.newportfolio.Repos;

import org.springframework.data.jpa.repository.JpaRepository;
import springaipractice.newportfolio.Models.PageVisit;

public interface PageVisitRepository extends JpaRepository<PageVisit, String> {
}
