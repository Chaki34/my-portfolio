package springaipractice.newportfolio.Models;



public class EnrollmentDTO {
    private String fullName;
    private String email;
    private String contactNumber;
    private String docType;
    private String docNumber;
    private String occupation;
    private String institutionName;
    private String playlistId;
    private String playlistTitle;

    // Getters and Setters
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    public String getDocType() { return docType; }
    public void setDocType(String docType) { this.docType = docType; }
    public String getDocNumber() { return docNumber; }
    public void setDocNumber(String docNumber) { this.docNumber = docNumber; }
    public String getOccupation() { return occupation; }
    public void setOccupation(String occupation) { this.occupation = occupation; }
    public String getInstitutionName() { return institutionName; }
    public void setInstitutionName(String institutionName) { this.institutionName = institutionName; }
    public String getPlaylistId() { return playlistId; }
    public void setPlaylistId(String playlistId) { this.playlistId = playlistId; }
    public String getPlaylistTitle() { return playlistTitle; }
    public void setPlaylistTitle(String playlistTitle) { this.playlistTitle = playlistTitle; }
}
