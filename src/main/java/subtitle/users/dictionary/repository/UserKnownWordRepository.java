package subtitle.users.dictionary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subtitle.security.model.User;
import subtitle.users.dictionary.model.UserKnownWord;

public interface UserKnownWordRepository extends JpaRepository<UserKnownWord, Long> {
    boolean existsByWordAndUser(String word, User user);
    UserKnownWord findByWordAndUser(String word, User user);
}
