package springaipractice.newportfolio.Models;

public class Tool {
    private String name;
    private String proficiency; // e.g. "85"
    private String iconPath;    // URL to image or icon class

    public Tool(String name, String proficiency, String iconPath) {
        this.name = name;
        this.proficiency = proficiency;
        this.iconPath = iconPath;
    }
    // Getters and Setters
    public String getName() { return name; }
    public String getProficiency() { return proficiency; }
    public String getIconPath() { return iconPath; }
}