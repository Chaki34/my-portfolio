package springaipractice.newportfolio.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class PageVisit {

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public long getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(long visitCount) {
        this.visitCount = visitCount;
    }

    @Id
    private String pageName;

    private long visitCount;

    // getters & setters
}