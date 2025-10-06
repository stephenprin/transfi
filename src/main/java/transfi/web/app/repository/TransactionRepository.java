package transfi.web.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import transfi.web.app.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

}
