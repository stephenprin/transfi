package transfi.web.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import transfi.web.app.entity.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {

    boolean existsByAccountNumber(Long accountNumber);

    boolean existsByCodeAndOwner_Uid(String code, String uid);


    List<Account> findAllByOwnerUid(String uid);

    Optional<Account> findByCodeAndOwner_Uid(String code, String uid);

    Optional<Account> findByAccountNumber(long receiverAccount);
}
