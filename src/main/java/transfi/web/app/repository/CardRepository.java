package transfi.web.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import transfi.web.app.entity.Card;

public interface CardRepository extends JpaRepository<Card, String> {

}
