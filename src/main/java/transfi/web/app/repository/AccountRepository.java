package transfi.web.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import transfi.web.app.entity.Account;

public interface AccountRepository extends JpaRepository<Account, String> {

}
