package springaipractice.newportfolio.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BrevoEmailService {

    @Value("${BREVO_API_KEY}")
    private String API_KEY;

    private final String URL = "https://api.brevo.com/v3/smtp/email";

    private final String SENDER_EMAIL = "debmalyachaki5@gmail.com"; // must be verified in Brevo

    private RestTemplate restTemplate = new RestTemplate();

    // ✅ 1. Send confirmation email to USER
    public void sendConfirmationEmail(String toEmail, String name) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", API_KEY);

        String body = """
        {
          "sender": {"name": "Portfolio", "email": "%s"},
          "to": [{"email": "%s"}],
          "subject": "Thank you for contacting us",
          "htmlContent": "<p>Dear %s,<br><br>Thanks for reaching out! I will get back to you soon.<br><br>Best Regards,<br>Portfolio Team</p>"
        }
        """.formatted(SENDER_EMAIL, toEmail, name);

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        try {
            restTemplate.postForEntity(URL, request, String.class);
        } catch (Exception e) {
            System.out.println("USER EMAIL ERROR:");
            e.printStackTrace();
        }
    }

    // ✅ 2. Send notification email to ADMIN
    public void notifyAdmin(String name, String email, String subject, String message) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", API_KEY);

        String body = """
        {
          "sender": {"name": "Portfolio", "email": "%s"},
          "to": [{"email": "%s"}],
          "subject": "New Contact Form Submission",
          "htmlContent": "<h3>New Message Received</h3>
                          <p><b>Name:</b> %s</p>
                          <p><b>Email:</b> %s</p>
                          <p><b>Subject:</b> %s</p>
                          <p><b>Message:</b><br>%s</p>"
        }
        """.formatted(SENDER_EMAIL, SENDER_EMAIL, name, email, subject, message);

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        try {
            restTemplate.postForEntity(URL, request, String.class);
        } catch (Exception e) {
            System.out.println("ADMIN EMAIL ERROR:");
            e.printStackTrace();
        }
    }
}