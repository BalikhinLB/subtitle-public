package subtitle.users.files.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import subtitle.users.files.model.FileText;
import subtitle.security.model.User;

import java.util.List;

@Repository
public interface FileTextRepository extends JpaRepository<FileText, Long>, FileTextRepositoryExt {
    List<FileText> findAllByUser(User user);
}
