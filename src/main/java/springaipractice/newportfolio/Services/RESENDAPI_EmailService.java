package springaipractice.newportfolio.Services;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class RESENDAPI_EmailService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${RESEND_API_KEY}")
    private String apiKey;

    private final String RESEND_URL = "https://api.resend.com/emails";

    public void sendConfirmationEmail(String toEmail, String name) {

        Map<String, Object> body = new HashMap<>();
        body.put("from", "onboarding@resend.dev"); // later change to your domain
        body.put("to", toEmail);
        body.put("subject", "Thank you for contacting us");
        body.put("text",
                "Dear " + name + ",\n\n" +
                        "We received your message.\n\n" +
                        "Thanks!");

        sendEmail(body);
    }

    public void notifyAdmin(String name, String email, String subject, String messageText) {

        Map<String, Object> body = new HashMap<>();
        body.put("from", "onboarding@resend.dev");
        body.put("to", "debmalyachaki5@gmail.com");
        body.put("subject", "New Contact Form Submission");
        body.put("text",
                "Name: " + name + "\n" +
                        "Email: " + email + "\n" +
                        "Subject: " + subject + "\n" +
                        "Message: " + messageText);

        sendEmail(body);
    }

    private void sendEmail(Map<String, Object> body) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            HttpEntity<Map<String, Object>> request =
                    new HttpEntity<>(body, headers);

            restTemplate.postForEntity(RESEND_URL, request, String.class);

        } catch (Exception e) {
            System.out.println("EMAIL ERROR:");
            e.printStackTrace();
        }
    }
}