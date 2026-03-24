package springaipractice.newportfolio.Models;

public class Skill {
    private String name;
    private String icon; // FontAwesome class like "fa-search"

    public Skill(String name, String icon) {
        this.name = name;
        this.icon = icon;
    }
    // Getters
    public String getName() { return name; }
    public String getIcon() { return icon; }
}
