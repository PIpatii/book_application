package mate.academy.application.repository.role;

import mate.academy.application.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query(value = "SELECT * FROM roles WHERE name = 'USER'", nativeQuery = true)
    Role getDefaultRole();
}
