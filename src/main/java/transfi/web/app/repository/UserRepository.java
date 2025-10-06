package transfi.web.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import transfi.web.app.entity.User;

public interface UserRepository extends JpaRepository<User, String> {

    User findByUsernameIgnoreCase(String username);
}
