package springaipractice.newportfolio.Models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "course_enrollments", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"student_id", "playlist_id"})
})
public class CourseEnrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private StudentUser student;

    @Column(name = "playlist_id", nullable = false)
    private String playlistId;

    private String playlistTitle;

    @Column(name = "total_videos", columnDefinition = "integer default 0")
    private int totalVideos = 0;

    @Column(name = "completed_videos", columnDefinition = "integer default 0")
    private int completedVideos = 0;

    @Column(name = "total_course_duration_seconds", columnDefinition = "float8 default 0.0")
    private Double totalCourseDurationSeconds = 0.0;

    @Column(name = "total_watched_seconds", columnDefinition = "float8 default 0.0")
    private Double totalWatchedSeconds = 0.0;

    @Column(name = "progress_percentage", columnDefinition = "float8 default 0.0")
    private double progressPercentage = 0.0;

    @Column(name = "certificate_unlocked", columnDefinition = "boolean default false")
    private boolean certificateUnlocked = false;

    // Persistent Certificate identifier and issuance timestamp
    @Column(name = "certificate_id")
    private String certificateId;

    @Column(name = "certificate_issued_at")
    private LocalDateTime certificateIssuedAt;

    private LocalDateTime enrolledAt = LocalDateTime.now();
    private LocalDateTime lastAccessedAt = LocalDateTime.now();

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public StudentUser getStudent() { return student; }
    public void setStudent(StudentUser student) { this.student = student; }

    public String getPlaylistId() { return playlistId; }
    public void setPlaylistId(String playlistId) { this.playlistId = playlistId; }

    public String getPlaylistTitle() { return playlistTitle; }
    public void setPlaylistTitle(String playlistTitle) { this.playlistTitle = playlistTitle; }

    public int getTotalVideos() { return totalVideos; }
    public void setTotalVideos(int totalVideos) { this.totalVideos = totalVideos; }

    public int getCompletedVideos() { return completedVideos; }
    public void setCompletedVideos(int completedVideos) { this.completedVideos = completedVideos; }

    public Double getTotalCourseDurationSeconds() {
        return totalCourseDurationSeconds != null ? totalCourseDurationSeconds : 0.0;
    }
    public void setTotalCourseDurationSeconds(Double totalCourseDurationSeconds) {
        this.totalCourseDurationSeconds = totalCourseDurationSeconds;
    }

    public Double getTotalWatchedSeconds() {
        return totalWatchedSeconds != null ? totalWatchedSeconds : 0.0;
    }
    public void setTotalWatchedSeconds(Double totalWatchedSeconds) {
        this.totalWatchedSeconds = totalWatchedSeconds;
    }

    public double getProgressPercentage() { return progressPercentage; }
    public void setProgressPercentage(double progressPercentage) { this.progressPercentage = progressPercentage; }

    public boolean isCertificateUnlocked() { return certificateUnlocked; }
    public void setCertificateUnlocked(boolean certificateUnlocked) { this.certificateUnlocked = certificateUnlocked; }

    public String getCertificateId() { return certificateId; }
    public void setCertificateId(String certificateId) { this.certificateId = certificateId; }

    public LocalDateTime getCertificateIssuedAt() { return certificateIssuedAt; }
    public void setCertificateIssuedAt(LocalDateTime certificateIssuedAt) { this.certificateIssuedAt = certificateIssuedAt; }

    public LocalDateTime getEnrolledAt() { return enrolledAt; }
    public void setEnrolledAt(LocalDateTime enrolledAt) { this.enrolledAt = enrolledAt; }

    public LocalDateTime getLastAccessedAt() { return lastAccessedAt; }
    public void setLastAccessedAt(LocalDateTime lastAccessedAt) { this.lastAccessedAt = lastAccessedAt; }
}