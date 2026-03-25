package springaipractice.newportfolio.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springaipractice.newportfolio.Repos.PageVisitRepository;

@Service
public class VisitService {

    @Autowired
    private PageVisitRepository repository;

    public long incrementCount(String pageName) {
        repository.incrementVisit(pageName);
        return repository.getVisitCount(pageName);
    }

    public long getCount(String pageName) {
        Long count = repository.getVisitCount(pageName);
        return count != null ? count : 0;
    }
}