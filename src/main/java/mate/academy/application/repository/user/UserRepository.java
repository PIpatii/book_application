package mate.academy.application.repository.user;

import java.util.Optional;
import mate.academy.application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsUserByEmail(String email);

    Optional<User> findByEmail(String email);
}
