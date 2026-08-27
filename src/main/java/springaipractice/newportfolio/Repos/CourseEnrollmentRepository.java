package springaipractice.newportfolio.Repos;



import org.springframework.data.jpa.repository.JpaRepository;
import springaipractice.newportfolio.Models.CourseEnrollment;
import java.util.List;
import java.util.Optional;

public interface CourseEnrollmentRepository extends JpaRepository<CourseEnrollment, Long> {
    Optional<CourseEnrollment> findByStudentIdAndPlaylistId(Long studentId, String playlistId);
    List<CourseEnrollment> findAllByStudentId(Long studentId);
}
