package springaipractice.newportfolio.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springaipractice.newportfolio.Models.PageVisit;
import springaipractice.newportfolio.Repos.PageVisitRepository;

@Service
public class VisitService {


    private PageVisitRepository repo;

    public VisitService(PageVisitRepository repo) {
        this.repo = repo;
    }



    public long incrementCount(String pageName) {
        PageVisit visit = repo.findById(pageName)
                .orElseGet(() -> {
                    PageVisit pv = new PageVisit();
                    pv.setPageName(pageName);
                    pv.setVisitCount(0);
                    return pv;
                });

        visit.setVisitCount(visit.getVisitCount() + 1);
        repo.save(visit);

        return visit.getVisitCount();
    }
}