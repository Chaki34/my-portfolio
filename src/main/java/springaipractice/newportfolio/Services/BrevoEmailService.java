package springaipractice.newportfolio.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BrevoEmailService {

    // =========================================================
    // CONFIGURATION VALUES FROM application.properties
    // =========================================================

    @Value("${ADMIN_EMAIL}")
    private String senderEmail;

    @Value("${BREVO_API_KEY}")
    private String brevoApiKey;


    // =========================================================
    // BREVO API
    // =========================================================

    private static final String URL =
            "https://api.brevo.com/v3/smtp/email";

    private final RestTemplate restTemplate = new RestTemplate();


    // =========================================================
    // GET BREVO API KEY SAFELY
    // =========================================================

    private String getApiKey() {

        if (brevoApiKey == null || brevoApiKey.isBlank()) {
            throw new RuntimeException(
                    "BREVO_API_KEY is missing in environment variables"
            );
        }

        return brevoApiKey;
    }


    // =========================================================
    // COMMON HEADERS
    // =========================================================

    private HttpHeaders buildHeaders() {

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", getApiKey());

        return headers;
    }


    // =========================================================
    // USER EMAIL
    // =========================================================

    public void sendConfirmationEmail(String toEmail, String name) {

        try {

            HttpHeaders headers = buildHeaders();

            Map<String, Object> requestBody = new HashMap<>();


            // -------------------------------------------------
            // SENDER
            // -------------------------------------------------

            Map<String, String> sender = new HashMap<>();

            sender.put("name", "Debmalya Chaki | Portfolio");
            sender.put("email", senderEmail);


            // -------------------------------------------------
            // RECIPIENT
            // -------------------------------------------------

            Map<String, String> to = new HashMap<>();

            to.put("email", toEmail);

            List<Map<String, String>> toList = new ArrayList<>();

            toList.add(to);


            requestBody.put("sender", sender);
            requestBody.put("to", toList);

            requestBody.put(
                    "subject",
                    "Thank you for getting in touch! | Debmalya Chaki"
            );


            // =================================================
            // YOUR EXISTING EMAIL DESIGN
            // =================================================

            String htmlContent =
                    "<!DOCTYPE html>"
                            + "<html>"
                            + "<head>"
                            + "<meta charset='UTF-8'>"
                            + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                            + "</head>"

                            + "<body style='margin:0; padding:0; background-color:#f4f6f9; font-family: -apple-system, BlinkMacSystemFont, \"Segoe UI\", Roboto, Helvetica, Arial, sans-serif;'>"

                            + "  <table width='100%' border='0' cellspacing='0' cellpadding='0' style='background-color:#f4f6f9; padding:40px 10px;'>"
                            + "    <tr>"
                            + "      <td align='center'>"

                            + "        <table width='600' border='0' cellspacing='0' cellpadding='0' style='max-width:600px; background-color:#ffffff; border-radius:12px; overflow:hidden; box-shadow:0 4px 15px rgba(0,0,0,0.08);'>"


                            // Banner Image
                            + "          <tr>"
                            + "            <td>"
                            + "              <img src='https://images.unsplash.com/photo-1517694712202-14dd9538aa97?q=80&w=1200&auto=format&fit=crop' alt='Portfolio Banner' style='width:100%; max-height:220px; object-fit:cover; display:block;'>"
                            + "            </td>"
                            + "          </tr>"


                            // Email Header
                            + "          <tr>"
                            + "            <td style='padding:30px 40px 10px 40px;'>"
                            + "              <h1 style='margin:0; color:#1a1f36; font-size:24px; font-weight:700;'>Thank you for reaching out!</h1>"
                            + "              <p style='color:#697386; font-size:15px; margin-top:8px;'>We have received your message and will be in touch shortly.</p>"
                            + "            </td>"
                            + "          </tr>"


                            // Email Body
                            + "          <tr>"
                            + "            <td style='padding:10px 40px 30px 40px; color:#4f566b; font-size:15px; line-height:1.6;'>"

                            + "              <p>Hi <strong>" + name + "</strong>,</p>"

                            + "              <p>Thank you for visiting my portfolio and taking the time to send a message. Whether it is regarding a project collaboration, freelance inquiry, or just a quick hello, I truly appreciate your interest.</p>"

                            + "              <div style='background-color:#f8fafc; border-left:4px solid #4f46e5; padding:15px 20px; border-radius:4px; margin:20px 0;'>"

                            + "                <p style='margin:0; font-size:14px; color:#334155;'>"
                            + "<strong>Quick Note:</strong> I usually respond within "
                            + "<strong>24 to 48 business hours</strong>. "
                            + "If your request is urgent, feel free to connect via LinkedIn."
                            + "</p>"

                            + "              </div>"

                            + "              <p style='margin-bottom:25px;'>Looking forward to connecting with you soon!</p>"

                            + "              <p style='margin:0;'>Warm regards,"
                            + "<br><strong style='color:#1a1f36;'>Debmalya Chaki</strong>"
                            + "<br><span style='color:#8792a2; font-size:13px;'>Software Engineer & Developer</span>"
                            + "</p>"

                            + "            </td>"
                            + "          </tr>"


                            // Footer
                            + "          <tr>"
                            + "            <td style='background-color:#0f172a; padding:20px; text-align:center; color:#94a3b8; font-size:12px;'>"

                            + "              <p style='margin:0;'>&copy; "
                            + Year.now().getValue()
                            + " Debmalya Chaki. All rights reserved.</p>"

                            + "              <p style='margin:6px 0 0 0;'>This is an automated confirmation of your contact form submission.</p>"

                            + "            </td>"
                            + "          </tr>"


                            + "        </table>"
                            + "      </td>"
                            + "    </tr>"
                            + "  </table>"

                            + "</body>"
                            + "</html>";


            requestBody.put("htmlContent", htmlContent);


            HttpEntity<Map<String, Object>> request =
                    new HttpEntity<>(requestBody, headers);


            restTemplate.postForEntity(
                    URL,
                    request,
                    String.class
            );


            System.out.println("✅ User email sent successfully");


        } catch (Exception e) {

            System.out.println("❌ Failed to send user email");

            e.printStackTrace();
        }
    }


    // =========================================================
    // ADMIN EMAIL
    // =========================================================

    public void notifyAdmin(
            String name,
            String email,
            String subject,
            String message
    ) {

        try {

            HttpHeaders headers = buildHeaders();

            Map<String, Object> requestBody = new HashMap<>();


            // -------------------------------------------------
            // SENDER
            // -------------------------------------------------

            Map<String, String> sender = new HashMap<>();

            sender.put(
                    "name",
                    "Portfolio Notification System"
            );

            sender.put(
                    "email",
                    senderEmail
            );


            // -------------------------------------------------
            // ADMIN RECIPIENT
            // -------------------------------------------------

            Map<String, String> to = new HashMap<>();

            to.put(
                    "email",
                    senderEmail
            );

            List<Map<String, String>> toList =
                    new ArrayList<>();

            toList.add(to);


            requestBody.put("sender", sender);
            requestBody.put("to", toList);

            requestBody.put(
                    "subject",
                    "🚀 New Contact Request: " + subject
            );


            // =================================================
            // YOUR EXISTING ADMIN EMAIL DESIGN
            // =================================================

            String htmlContent =
                    "<!DOCTYPE html>"
                            + "<html>"
                            + "<head><meta charset='UTF-8'></head>"

                            + "<body style='margin:0; padding:0; background-color:#f4f6f9; font-family: -apple-system, BlinkMacSystemFont, \"Segoe UI\", Roboto, Arial, sans-serif;'>"

                            + "  <table width='100%' border='0' cellspacing='0' cellpadding='0' style='padding:30px 10px;'>"

                            + "    <tr>"
                            + "      <td align='center'>"

                            + "        <table width='600' border='0' cellspacing='0' cellpadding='0' style='max-width:600px; background-color:#ffffff; border-radius:10px; border:1px solid #e2e8f0; overflow:hidden;'>"


                            // Admin Header
                            + "          <tr>"
                            + "            <td style='background:linear-gradient(135deg, #1e293b, #0f172a); padding:20px 30px;'>"

                            + "              <h2 style='color:#ffffff; margin:0; font-size:18px;'>📬 New Contact Form Submission</h2>"

                            + "            </td>"
                            + "          </tr>"


                            // Details Card
                            + "          <tr>"
                            + "            <td style='padding:30px;'>"

                            + "              <table width='100%' border='0' cellspacing='0' cellpadding='10' style='border-collapse:collapse;'>"


                            + "                <tr style='border-bottom:1px solid #f1f5f9;'>"

                            + "                  <td style='width:120px; font-weight:600; color:#64748b; font-size:14px;'>Name:</td>"

                            + "                  <td style='color:#1e293b; font-size:14px; font-weight:500;'>"
                            + name
                            + "</td>"

                            + "                </tr>"


                            + "                <tr style='border-bottom:1px solid #f1f5f9;'>"

                            + "                  <td style='font-weight:600; color:#64748b; font-size:14px;'>Email:</td>"

                            + "                  <td style='color:#2563eb; font-size:14px;'>"

                            + "                    <a href='mailto:"
                            + email
                            + "' style='color:#2563eb; text-decoration:none;'>"
                            + email
                            + "</a>"

                            + "                  </td>"

                            + "                </tr>"


                            + "                <tr style='border-bottom:1px solid #f1f5f9;'>"

                            + "                  <td style='font-weight:600; color:#64748b; font-size:14px;'>Subject:</td>"

                            + "                  <td style='color:#1e293b; font-size:14px;'>"
                            + subject
                            + "</td>"

                            + "                </tr>"


                            + "                <tr>"

                            + "                  <td colspan='2' style='padding-top:15px; font-weight:600; color:#64748b; font-size:14px;'>Message:</td>"

                            + "                </tr>"


                            + "              </table>"


                            // Message Box
                            + "              <div style='margin-top:10px; background-color:#f8fafc; border:1px solid #e2e8f0; border-radius:6px; padding:15px; color:#334155; font-size:14px; line-height:1.6; white-space:pre-wrap;'>"

                            + message

                            + "              </div>"


                            // Quick Reply
                            + "              <div style='margin-top:25px; text-align:center;'>"

                            + "                <a href='mailto:"
                            + email
                            + "?subject=Re: "
                            + subject
                            + "' style='background-color:#2563eb; color:#ffffff; padding:10px 24px; text-decoration:none; border-radius:6px; font-size:14px; font-weight:500; display:inline-block;'>Reply directly to "
                            + name
                            + "</a>"

                            + "              </div>"


                            + "            </td>"
                            + "          </tr>"

                            + "        </table>"

                            + "      </td>"
                            + "    </tr>"

                            + "  </table>"

                            + "</body>"
                            + "</html>";


            requestBody.put(
                    "htmlContent",
                    htmlContent
            );


            HttpEntity<Map<String, Object>> request =
                    new HttpEntity<>(requestBody, headers);


            restTemplate.postForEntity(
                    URL,
                    request,
                    String.class
            );


            System.out.println("✅ Admin email sent successfully");


        } catch (Exception e) {

            System.out.println("❌ Failed to send admin email");

            e.printStackTrace();
        }
    }
}