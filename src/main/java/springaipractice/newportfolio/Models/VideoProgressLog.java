package springaipractice.newportfolio.Models;



import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "video_progress_logs")
public class VideoProgressLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long studentId;

    @Column(nullable = false)
    private String playlistId;

    @Column(nullable = false)
    private String videoId;

    private String videoTitle;
    private double watchedSeconds = 0.0;
    private double totalDuration = 0.0;
    private boolean completed = false;

    private String actionType; // "START", "PAUSE", "SEEK", "FINISH"
    private LocalDateTime timestamp = LocalDateTime.now();

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getPlaylistId() { return playlistId; }
    public void setPlaylistId(String playlistId) { this.playlistId = playlistId; }

    public String getVideoId() { return videoId; }
    public void setVideoId(String videoId) { this.videoId = videoId; }

    public String getVideoTitle() { return videoTitle; }
    public void setVideoTitle(String videoTitle) { this.videoTitle = videoTitle; }

    public double getWatchedSeconds() { return watchedSeconds; }
    public void setWatchedSeconds(double watchedSeconds) { this.watchedSeconds = watchedSeconds; }

    public double getTotalDuration() { return totalDuration; }
    public void setTotalDuration(double totalDuration) { this.totalDuration = totalDuration; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}