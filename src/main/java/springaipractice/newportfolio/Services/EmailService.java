package springaipractice.newportfolio.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import springaipractice.newportfolio.Models.Contact;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendConfirmationEmail(String toEmail, String name) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("debmalyachaki5@gmail.com"); // ✅ FIX ADDED
        message.setTo(toEmail);
        message.setSubject("Thank you for contacting us");

        message.setText(
                "Dear " + name + ",\n\n" +
                        "Thank you for reaching out. We have received your message.\n\n" +
                        "Our team will contact you within 24 hours.\n\n" +
                        "Best regards,\nYour Company"
        );

        try { // ✅ SAFETY ADDED (prevents crash)
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println("EMAIL ERROR (USER): " + e.getMessage());
        }
    }

    public void notifyAdmin(Contact contact) {
        SimpleMailMessage msg = new SimpleMailMessage();

        msg.setFrom("debmalyachaki5@gmail.com"); // ✅ FIX ADDED
        msg.setTo("debmalyachaki5@gmail.com");
        msg.setSubject("New Contact Form Submission");

        msg.setText(
                "Name: " + contact.getName() + "\n" +
                        "Email: " + contact.getEmail() + "\n" +
                        "Subject: " + contact.getSubject() + "\n" +
                        "Message: " + contact.getMessage()
        );

        try { // ✅ SAFETY ADDED
            mailSender.send(msg);
        } catch (Exception e) {
            System.out.println("EMAIL ERROR (ADMIN): " + e.getMessage());
        }
    }
}