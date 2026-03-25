package springaipractice.newportfolio.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springaipractice.newportfolio.Models.Certificate;
import springaipractice.newportfolio.Models.Contact;
import springaipractice.newportfolio.Models.Skill;
import springaipractice.newportfolio.Models.Tool;
import springaipractice.newportfolio.Repos.ContactRepository;
import springaipractice.newportfolio.Services.EmailService;
import springaipractice.newportfolio.Services.RESENDAPI_EmailService;
import springaipractice.newportfolio.Services.VisitService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Controller
@RequestMapping("/v1")
public class homeController {

    private final VisitService visitService;

    private final ContactRepository contactRepository;

    private final RESENDAPI_EmailService resendapiemailService;

    public homeController(VisitService visitService, ContactRepository contactRepository, RESENDAPI_EmailService resendapiemailService) {
        this.visitService = visitService;
        this.contactRepository = contactRepository;
        this.resendapiemailService = resendapiemailService;

    }


    @GetMapping("/index")
    public String homeview(Model model) {



        long totalPageViews = visitService.incrementCount("index");

        // 2. Generate Random Happy Clients (between 100 and 5,000)
        int totalHappyClients = ThreadLocalRandom.current().nextInt(100, 5000);

        // 3. Generate Random Contact Applications (between 50 and 1,000)
        int totalContactApps = ThreadLocalRandom.current().nextInt(50, 1000);

        // 4. Generate Random Avg Rating (between 3.5 and 5.0)
        // We calculate it then round to 1 decimal place
        double rawRating = ThreadLocalRandom.current().nextDouble(3.5, 5.0);
        double avgRating = Math.round(rawRating * 10.0) / 10.0;

        // Pass variables to Thymeleaf Model
        model.addAttribute("totalPageViews", totalPageViews);
        model.addAttribute("totalHappyClients", totalHappyClients);
        model.addAttribute("totalContactApps", totalContactApps);
        model.addAttribute("avgRating", avgRating);

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

            // ✅ Send email to user
            try {
                resendapiemailService.sendConfirmationEmail(email, name);
            } catch (Exception e) {
                System.out.println("User email failed");
                e.printStackTrace();
            }

            // ✅ Send email to admin
            try {
                resendapiemailService.notifyAdmin(name, email, subject, message);
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
}
