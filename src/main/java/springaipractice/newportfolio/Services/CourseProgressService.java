package springaipractice.newportfolio.Services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springaipractice.newportfolio.Models.*;
import springaipractice.newportfolio.Repos.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class CourseProgressService {

    private final StudentUserRepository studentRepo;
    private final CourseEnrollmentRepository enrollmentRepo;
    private final VideoProgressLogRepository progressLogRepo;
    private final YouTubeService youTubeService;

    public CourseProgressService(StudentUserRepository studentRepo,
                                 CourseEnrollmentRepository enrollmentRepo,
                                 VideoProgressLogRepository progressLogRepo,
                                 YouTubeService youTubeService) {
        this.studentRepo = studentRepo;
        this.enrollmentRepo = enrollmentRepo;
        this.progressLogRepo = progressLogRepo;
        this.youTubeService = youTubeService;
    }

    @Transactional
    public CourseEnrollment enrollStudent(EnrollmentDTO dto, HttpServletRequest request) {
        StudentUser student = studentRepo.findByEmail(dto.getEmail()).orElseGet(StudentUser::new);

        student.setFullName(dto.getFullName());
        student.setEmail(dto.getEmail());
        student.setContactNumber(dto.getContactNumber());
        student.setDocType(dto.getDocType());
        student.setDocNumber(dto.getDocNumber());
        student.setOccupation(dto.getOccupation());
        student.setInstitutionName(dto.getInstitutionName());

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        student.setIpAddress(ip);

        String userAgent = request.getHeader("User-Agent");
        student.setUserAgent(userAgent);

        String ua = (userAgent != null) ? userAgent.toLowerCase() : "";
        if (ua.contains("mobile") || ua.contains("android") || ua.contains("iphone")) {
            student.setDeviceType("Mobile");
        } else if (ua.contains("tablet") || ua.contains("ipad")) {
            student.setDeviceType("Tablet");
        } else {
            student.setDeviceType("Desktop / Laptop");
        }

        student = studentRepo.save(student);

        final StudentUser finalStudent = student;
        CourseEnrollment enrollment = enrollmentRepo.findByStudentIdAndPlaylistId(student.getId(), dto.getPlaylistId())
                .orElseGet(() -> {
                    CourseEnrollment ne = new CourseEnrollment();
                    ne.setStudent(finalStudent);
                    ne.setPlaylistId(dto.getPlaylistId());
                    ne.setPlaylistTitle(dto.getPlaylistTitle());
                    return ne;
                });

        if (enrollment.getTotalCourseDurationSeconds() == 0.0) {
            List<Map<String, Object>> videos = youTubeService.getPlaylistVideos(dto.getPlaylistId());
            enrollment.setTotalVideos(videos.size());
            double totalSeconds = youTubeService.getTotalPlaylistDurationInSeconds(videos);
            enrollment.setTotalCourseDurationSeconds(totalSeconds > 0 ? totalSeconds : (videos.size() * 600)); // Fallback: 10m each if quota exhausted
        }

        enrollment.setLastAccessedAt(LocalDateTime.now());
        return enrollmentRepo.save(enrollment);
    }

    @Transactional
    public void recordVideoHeartbeat(Long studentId, String playlistId, String videoId,
                                     String videoTitle, double currentTime, double duration,
                                     String actionType) {
        VideoProgressLog log = new VideoProgressLog();
        log.setStudentId(studentId);
        log.setPlaylistId(playlistId);
        log.setVideoId(videoId);
        log.setVideoTitle(videoTitle);
        log.setWatchedSeconds(currentTime);
        log.setTotalDuration(duration);
        log.setActionType(actionType);

        boolean isCompleted = (duration > 0 && (currentTime / duration) >= 0.85) || "FINISH".equalsIgnoreCase(actionType);
        log.setCompleted(isCompleted);
        progressLogRepo.save(log);

        CourseEnrollment enrollment = enrollmentRepo.findByStudentIdAndPlaylistId(studentId, playlistId).orElse(null);
        if (enrollment != null && enrollment.getTotalCourseDurationSeconds() > 0) {
            int completedCount = progressLogRepo.countDistinctCompletedVideos(studentId, playlistId);
            enrollment.setCompletedVideos(completedCount);

            // Compute total watched duration in seconds across unique videos
            Double watchedSecondsSum = progressLogRepo.sumMaxWatchedSecondsPerVideo(studentId, playlistId);
            if (watchedSecondsSum == null) watchedSecondsSum = 0.0;
            enrollment.setTotalWatchedSeconds(watchedSecondsSum);

            // 80% calculation based strictly on total duration
            double percentage = (watchedSecondsSum / enrollment.getTotalCourseDurationSeconds()) * 100.0;
            percentage = Math.min(100.0, Math.round(percentage * 10.0) / 10.0);
            enrollment.setProgressPercentage(percentage);

            if (percentage >= 80.0) {
                enrollment.setCertificateUnlocked(true);
            }
            enrollment.setLastAccessedAt(LocalDateTime.now());
            enrollmentRepo.save(enrollment);
        }
    }
}