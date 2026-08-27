package springaipractice.newportfolio.Repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import springaipractice.newportfolio.Models.VideoProgressLog;
import java.util.List;

public interface VideoProgressLogRepository extends JpaRepository<VideoProgressLog, Long> {
    List<VideoProgressLog> findTop20ByStudentIdAndPlaylistIdOrderByTimestampDesc(Long studentId, String playlistId);

    @Query("SELECT COUNT(DISTINCT v.videoId) FROM VideoProgressLog v WHERE v.studentId = :studentId AND v.playlistId = :playlistId AND v.completed = true")
    int countDistinctCompletedVideos(Long studentId, String playlistId);

    @Query("SELECT COALESCE(SUM(maxW), 0.0) FROM (SELECT MAX(v.watchedSeconds) as maxW FROM VideoProgressLog v WHERE v.studentId = :studentId AND v.playlistId = :playlistId GROUP BY v.videoId)")
    Double sumMaxWatchedSecondsPerVideo(Long studentId, String playlistId);
}