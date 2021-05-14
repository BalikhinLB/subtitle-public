package subtitle.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subtitle.security.model.Authority;
import subtitle.security.model.Role;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findFirstByRole(Role role);
}
