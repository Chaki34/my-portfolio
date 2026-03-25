package springaipractice.newportfolio.Repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import springaipractice.newportfolio.Models.PageVisit;

public interface PageVisitRepository extends JpaRepository<PageVisit, String> {

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO page_visit (page_name, visit_count)
        VALUES (:pageName, 1)
        ON CONFLICT (page_name)
        DO UPDATE SET visit_count = page_visit.visit_count + 1
        """, nativeQuery = true)
    void incrementVisit(String pageName);

    @Query(value = "SELECT visit_count FROM page_visit WHERE page_name = :pageName", nativeQuery = true)
    Long getVisitCount(String pageName);
}