package springaipractice.newportfolio.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class BrevoEmailService {

    @Autowired
    private Environment env;

    private final String URL = "https://api.brevo.com/v3/smtp/email";
    private final String SENDER_EMAIL = "debmalyachaki5@gmail.com";

    private final RestTemplate restTemplate = new RestTemplate();

    // ✅ Get API key safely
    private String getApiKey() {
        String key = env.getProperty("BREVO_API_KEY");

        if (key == null || key.isBlank()) {
            throw new RuntimeException("BREVO_API_KEY is missing in environment variables");
        }

        return key;
    }

    // ✅ Common headers builder
    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", getApiKey());
        return headers;
    }

    // ✅ USER EMAIL
    public void sendConfirmationEmail(String toEmail, String name) {
        try {
            HttpHeaders headers = buildHeaders();

            String body = """
            {
              "sender": {"name": "Portfolio", "email": "%s"},
              "to": [{"email": "%s"}],
              "subject": "Thank you for contacting us",
              "htmlContent": "<p>Dear %s,<br><br>Thanks for reaching out! I will get back to you soon.<br><br>Best Regards,<br>Portfolio Team</p>"
            }
            """.formatted(SENDER_EMAIL, toEmail, name);

            HttpEntity<String> request = new HttpEntity<>(body, headers);

            restTemplate.postForEntity(URL, request, String.class);

            System.out.println("✅ User email sent successfully");

        } catch (Exception e) {
            System.out.println("❌ Failed to send user email");
            e.printStackTrace();
        }
    }

    public void notifyAdmin(String name, String email, String subject, String message) {

        try {
            HttpHeaders headers = buildHeaders();

            // Build request payload as a Map (safe JSON)
            Map<String, Object> requestBody = new HashMap<>();

            Map<String, String> sender = new HashMap<>();
            sender.put("name", "Portfolio");
            sender.put("email", SENDER_EMAIL);

            Map<String, String> toEmail = new HashMap<>();
            toEmail.put("email", SENDER_EMAIL);

            List<Map<String, String>> toList = new ArrayList<>();
            toList.add(toEmail);

            requestBody.put("sender", sender);
            requestBody.put("to", toList);
            requestBody.put("subject", "New Contact Form Submission");

            String htmlContent = "<h3>New Message Received</h3>"
                    + "<p><b>Name:</b> " + name + "</p>"
                    + "<p><b>Email:</b> " + email + "</p>"
                    + "<p><b>Subject:</b> " + subject + "</p>"
                    + "<p><b>Message:</b><br>" + message + "</p>";

            requestBody.put("htmlContent", htmlContent);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            restTemplate.postForEntity(URL, request, String.class);

            System.out.println("✅ Admin email sent successfully");

        } catch (Exception e) {
            System.out.println("❌ Failed to send admin email");
            e.printStackTrace();
        }
    }
}