package springaipractice.newportfolio.Models;



import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Certificate {
    private Long id;
    private String title;
    private String organization;
    private String description;
    private LocalDate issueDate;
    private String iconClass;
    private String verifyUrl;

    // Constructor
    public Certificate() {
    }

    public Certificate(Long id, String title, String organization, String description,
                       LocalDate issueDate, String iconClass, String verifyUrl) {
        this.id = id;
        this.title = title;
        this.organization = organization;
        this.description = description;
        this.issueDate = issueDate;
        this.iconClass = iconClass;
        this.verifyUrl = verifyUrl;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public String getIconClass() {
        return iconClass;
    }

    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
    }

    public String getVerifyUrl() {
        return verifyUrl;
    }

    public void setVerifyUrl(String verifyUrl) {
        this.verifyUrl = verifyUrl;
    }



    // Add this method if you want to use cert.date in template
    public String getDate() {
        if (issueDate != null) {
            return issueDate.format(DateTimeFormatter.ofPattern("MMMM yyyy"));
        }
        return "";
    }

    // Or keep both
    public String getFormattedDate() {
        return getDate();
    }
}
