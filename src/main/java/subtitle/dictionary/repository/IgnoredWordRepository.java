package subtitle.dictionary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subtitle.dictionary.model.IgnoredWord;

public interface IgnoredWordRepository extends JpaRepository<IgnoredWord, Long> {
    boolean existsByWord(String word);
}
