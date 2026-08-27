package springaipractice.newportfolio.Repos;



import org.springframework.data.jpa.repository.JpaRepository;
import springaipractice.newportfolio.Models.StudentUser;
import java.util.Optional;

public interface StudentUserRepository extends JpaRepository<StudentUser, Long> {
    Optional<StudentUser> findByEmail(String email);
}
