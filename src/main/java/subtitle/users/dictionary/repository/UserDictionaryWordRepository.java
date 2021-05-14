package subtitle.users.dictionary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subtitle.security.model.User;
import subtitle.users.dictionary.model.UserDictionaryWord;

public interface UserDictionaryWordRepository extends JpaRepository<UserDictionaryWord, Long> {
    UserDictionaryWord findByWordAndUser(String word, User user);
}
