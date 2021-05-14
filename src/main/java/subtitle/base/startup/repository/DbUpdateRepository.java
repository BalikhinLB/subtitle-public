package subtitle.base.startup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subtitle.base.startup.model.DbUpdate;

public interface DbUpdateRepository extends JpaRepository<DbUpdate, Long> {
    boolean existsByName(String name);
    DbUpdate findFirstByName(String name);
}
