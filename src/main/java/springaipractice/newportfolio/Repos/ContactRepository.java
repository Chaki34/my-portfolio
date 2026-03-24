package springaipractice.newportfolio.Repos;

import org.springframework.data.jpa.repository.JpaRepository;
import springaipractice.newportfolio.Models.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {
}
