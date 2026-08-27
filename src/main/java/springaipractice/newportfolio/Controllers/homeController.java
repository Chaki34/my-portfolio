package springaipractice.newportfolio.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springaipractice.newportfolio.Models.*;
import springaipractice.newportfolio.Repos.ContactRepository;
import springaipractice.newportfolio.Repos.CourseEnrollmentRepository;
import springaipractice.newportfolio.Repos.StudentUserRepository;
import springaipractice.newportfolio.Repos.VideoProgressLogRepository;
import springaipractice.newportfolio.Services.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Controller
@RequestMapping("/v1")
public class homeController {

    private final VisitService visitService;

    private final YouTubeService  youTubeService;

    private final ContactRepository contactRepository;


    private BrevoEmailService brevoEmailService;


    private FeedbackService feedbackService;

    @Autowired
    private CourseProgressService courseProgressService;

    @Autowired
    private CourseEnrollmentRepository enrollmentRepo;

    @Autowired
    private VideoProgressLogRepository progressLogRepo;

    @Autowired
    private StudentUserRepository studentUserRepository;

    public homeController(VisitService visitService, YouTubeService youTubeService, ContactRepository contactRepository , BrevoEmailService brevoEmailService , FeedbackService feedbackService) {
        this.visitService = visitService;
        this.youTubeService = youTubeService;
        this.contactRepository = contactRepository;
        this.brevoEmailService = brevoEmailService;
        this.feedbackService = feedbackService;

    }


    @GetMapping("/index")
    public String homeview(Model model) {


        long totalPageViews = visitService.incrementCount("index");

        model.addAttribute("totalPageViews", totalPageViews);

        // other attributes
        model.addAttribute("totalHappyClients", ThreadLocalRandom.current().nextInt(100, 5000));
        model.addAttribute("totalContactApps", ThreadLocalRandom.current().nextInt(50, 1000));

        double rawRating = ThreadLocalRandom.current().nextDouble(3.5, 5.0);
        double avgRating = Math.round(rawRating * 10.0) / 10.0;

        model.addAttribute("avgRating", avgRating);

        model.addAttribute("playlists", youTubeService.getPlaylists());
        return "index";


    }

    @GetMapping("/projects")
    public String projects() {
        return "projects";
    }

    @GetMapping("/resume")
    public String getProfile(Model model) {

        // 1. Basic Profile Info
        model.addAttribute("profileName", "DEBMALYA CHAKI");
        model.addAttribute("jobTitle", "Senior Full-Stack Engineer");
        model.addAttribute("email", "debmalyachaki5@gmail.com");
        model.addAttribute("phone", "8597135533");
        model.addAttribute("location", "Pailan , Kolkata");
        model.addAttribute("profileImageUrl", "/images/my-photo2.png"); // Demo avatar

        model.addAttribute("aboutDescription",
                "I am a Backend Software Engineer specializing in architecting scalable, high-performance systems " +
                        "with a core focus on the Java ecosystem and Spring Boot. I excel at building robust server-side " +
                        "foundations, optimizing data persistence with MySQL and Redis, and solving complex algorithmic " +
                        "challenges. I help industries and organizations modernize their digital infrastructure by " +
                        "engineering resilient microservices and high-throughput applications that prioritize technical " +
                        "precision and clean code architecture.");

        // 2. Expertise / Skills List (Middle Grid)
        List<Skill> expertise = new ArrayList<>();
// Java & Spring Boot Core
        expertise.add(new Skill("Backend Architecture", "fa-server"));
        expertise.add(new Skill("Spring Boot & Microservices", "fa-leaf"));

// Systems & Scripting
        expertise.add(new Skill("Systems Programming (C)", "fa-microchip"));
        expertise.add(new Skill("Python & Automation", "fa-robot"));

// Database & High Performance
        expertise.add(new Skill("Database Management (MySQL)", "fa-database"));
        expertise.add(new Skill("Caching & NoSQL (Redis/Mongo)", "fa-bolt"));

// Engineering Fundamentals
        expertise.add(new Skill("Scalable System Design", "fa-project-diagram"));
        expertise.add(new Skill("Data Structures & Algorithms", "fa-code-branch"));

        model.addAttribute("expertiseList", expertise);

        // 3. Work Experience Section
        model.addAttribute("expTitle", "Senior Full-Stack Devloper");
        model.addAttribute("expCompany", "Founder Of Codi_Die_Oxcide Inc.");
        model.addAttribute("expDuration", "2023 - Present");

        // 4. Tools List (Right Sidebar - Developer Toolchain)
        List<Tool> tools = new ArrayList<>();

// Core Development & IDEs
        tools.add(new Tool("IntelliJ IDEA", "95", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRDWkXVSznHFcv4RO7BaYKnk8CzQ41ElYbXJw&s"));
        tools.add(new Tool("VS Code", "90", "https://cdn.worldvectorlogo.com/logos/visual-studio-code-1.svg"));

// Backend Ecosystem & Build Tools
        tools.add(new Tool("Spring Boot", "92", "https://www.vectorlogo.zone/logos/springio/springio-icon.svg"));
        tools.add(new Tool("Maven/Gradle", "85", "https://www.vectorlogo.zone/logos/apache_maven/apache_maven-icon.svg"));

// Version Control & API Testing
        tools.add(new Tool("Git & GitHub", "95", "https://www.vectorlogo.zone/logos/git-scm/git-scm-icon.svg"));
        tools.add(new Tool("Postman", "90", "https://www.vectorlogo.zone/logos/getpostman/getpostman-icon.svg"));

// Deployment & Databases
        tools.add(new Tool("Docker", "80", "https://www.vectorlogo.zone/logos/docker/docker-icon.svg"));
        tools.add(new Tool("MySQL Workbench", "85", "https://www.vectorlogo.zone/logos/mysql/mysql-icon.svg"));
        tools.add(new Tool("RedisInsight", "75", "https://www.vectorlogo.zone/logos/redis/redis-icon.svg"));

        model.addAttribute("toolsList", tools);


        return "resume";
    }


    @GetMapping("/certificates")
    public String showCertificates(Model model) {

        return "certificates";
    }


    @PostMapping("/contact")
    @ResponseBody
    public Map<String, String> handleContact(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam(required = false) String subject,
            @RequestParam String message) {

        Map<String, String> response = new HashMap<>();

        try {
            // ✅ Handle null/empty subject
            if (subject == null || subject.trim().isEmpty()) {
                subject = "General Inquiry";
            }

            // ✅ Create and save contact
            Contact contact = new Contact();
            contact.setName(name);
            contact.setEmail(email);
            contact.setSubject(subject);
            contact.setMessage(message);

            contactRepository.save(contact);


            // Send email to user
            try {
                brevoEmailService.sendConfirmationEmail(email, name);
            } catch (Exception e) {
                System.out.println("User email failed");
                e.printStackTrace();
            }

// Send email to admin
            try {
                brevoEmailService.notifyAdmin(name, email, subject, message);
            } catch (Exception e) {
                System.out.println("Admin email failed");
                e.printStackTrace();
            }

            // ✅ Success response
            response.put("status", "success");
            response.put("message", "Message sent successfully");

        } catch (Exception e) {
            // ❌ Handle any unexpected error
            e.printStackTrace();

            response.put("status", "error");
            response.put("message", "Something went wrong. Please try again.");
        }

        return response;
    }

    @PostMapping("/feedback")
    @ResponseBody
    public String submitFeedback(@RequestBody FeedbackRequest request) {
        feedbackService.saveFeedback(request.getRating());
        return "Feedback submitted successfully!";
    }

    @GetMapping("/quiz")
    public String quizPage() {
        return "Quiz"; // refers to quiz.html
    }

    @GetMapping("/quiz-ground")
    public String quizPlayGround() {
        return "quiz-ground";
    }

    @GetMapping("/courses")
    public String showCourses(Model model) {
        model.addAttribute("playlists", youTubeService.getPlaylists());
        return "courses";
    }

    // API for the modal to fetch videos dynamically
    @GetMapping("/playlist-videos/{id}")
    @ResponseBody
    public List<Map<String, Object>> getVideos(@PathVariable String id) {
        return youTubeService.getPlaylistVideos(id);
    }

    @PostMapping("/enroll-course")
    @ResponseBody
    public Map<String, Object> handleEnrollment(@RequestBody EnrollmentDTO dto, jakarta.servlet.http.HttpServletRequest request) {
        Map<String, Object> res = new HashMap<>();
        try {
            CourseEnrollment enrollment = courseProgressService.enrollStudent(dto, request);
            res.put("status", "success");
            res.put("studentId", enrollment.getStudent().getId());
            res.put("playlistId", enrollment.getPlaylistId());
            res.put("redirectUrl", "/v1/course-dashboard?studentId=" + enrollment.getStudent().getId() + "&playlistId=" + enrollment.getPlaylistId());
        } catch (Exception e) {
            e.printStackTrace();
            res.put("status", "error");
            res.put("message", e.getMessage());
        }
        return res;
    }

    @GetMapping("/course-dashboard")
    public String showDashboard(@RequestParam Long studentId, @RequestParam String playlistId, Model model) {
        StudentUser student = studentUserRepository.findById(studentId).orElse(null);
        CourseEnrollment enrollment = enrollmentRepo.findByStudentIdAndPlaylistId(studentId, playlistId).orElse(null);

        if (student == null || enrollment == null) {
            return "redirect:/v1/courses";
        }

        List<Map<String, Object>> videos = youTubeService.getPlaylistVideos(playlistId);
        List<VideoProgressLog> logs = progressLogRepo.findTop20ByStudentIdAndPlaylistIdOrderByTimestampDesc(studentId, playlistId);

        model.addAttribute("student", student);
        model.addAttribute("enrollment", enrollment);
        model.addAttribute("videos", videos);
        model.addAttribute("logs", logs);

        return "course-dashboard";
    }

    @PostMapping("/video-heartbeat")
    @ResponseBody
    public Map<String, Object> videoHeartbeat(@RequestBody Map<String, Object> payload) {
        Long studentId = Long.valueOf(payload.get("studentId").toString());
        String playlistId = (String) payload.get("playlistId");
        String videoId = (String) payload.get("videoId");
        String videoTitle = (String) payload.get("videoTitle");
        double currentTime = Double.parseDouble(payload.get("currentTime").toString());
        double duration = Double.parseDouble(payload.get("duration").toString());
        String actionType = (String) payload.get("actionType");

        courseProgressService.recordVideoHeartbeat(studentId, playlistId, videoId, videoTitle, currentTime, duration, actionType);

        CourseEnrollment enrollment = enrollmentRepo.findByStudentIdAndPlaylistId(studentId, playlistId).orElse(null);
        Map<String, Object> res = new HashMap<>();
        res.put("status", "ok");
        if (enrollment != null) {
            res.put("progress", enrollment.getProgressPercentage());
            res.put("certificateUnlocked", enrollment.isCertificateUnlocked());
        }
        return res;
    }

    @GetMapping("/student-certificate")
    public String showStudentCertificate(@RequestParam Long studentId, @RequestParam String playlistId, Model model) {
        StudentUser student = studentUserRepository.findById(studentId).orElse(null);
        CourseEnrollment enrollment = enrollmentRepo.findByStudentIdAndPlaylistId(studentId, playlistId).orElse(null);

        // Security check: Must have completed at least 80% total watch hours
        if (student == null || enrollment == null || !enrollment.isCertificateUnlocked()) {
            return "redirect:/v1/course-dashboard?studentId=" + studentId + "&playlistId=" + playlistId;
        }

        // If certificate was never generated, generate once and persist to database
        if (enrollment.getCertificateId() == null || enrollment.getCertificateId().trim().isEmpty()) {
            String generatedCertId = "CERT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            enrollment.setCertificateId(generatedCertId);
            enrollment.setCertificateIssuedAt(LocalDateTime.now());
            enrollmentRepo.save(enrollment);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        String formattedDate = enrollment.getCertificateIssuedAt().format(formatter);

        model.addAttribute("student", student);
        model.addAttribute("enrollment", enrollment);
        model.addAttribute("completionDate", formattedDate);
        model.addAttribute("certificateId", enrollment.getCertificateId());

        return "student-certificate";
    }

    @GetMapping("/check-enrollment")
    @ResponseBody
    public Map<String, Object> checkExistingEnrollment(@RequestParam String email) {
        Map<String, Object> response = new HashMap<>();

        Optional<StudentUser> studentOpt = studentUserRepository.findByEmail(email.trim().toLowerCase());

        if (studentOpt.isEmpty()) {
            response.put("status", "not_found");
            response.put("message", "No student enrollment found for this email address.");
            return response;
        }

        StudentUser student = studentOpt.get();
        List<CourseEnrollment> enrollments = enrollmentRepo.findAllByStudentId(student.getId());

        if (enrollments.isEmpty()) {
            response.put("status", "not_found");
            response.put("message", "No enrolled courses found for this user.");
            return response;
        }

        response.put("status", "success");
        response.put("studentId", student.getId());
        response.put("studentName", student.getFullName());

        List<Map<String, Object>> courseList = new ArrayList<>();
        for (CourseEnrollment ce : enrollments) {
            Map<String, Object> c = new HashMap<>();
            c.put("playlistId", ce.getPlaylistId());
            c.put("playlistTitle", ce.getPlaylistTitle());
            c.put("progress", ce.getProgressPercentage());
            c.put("dashboardUrl", "/v1/course-dashboard?studentId=" + student.getId() + "&playlistId=" + ce.getPlaylistId());
            courseList.add(c);
        }

        response.put("courses", courseList);
        return response;
    }
}









