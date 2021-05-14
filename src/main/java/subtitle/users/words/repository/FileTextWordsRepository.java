package subtitle.users.words.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import subtitle.security.model.User;
import subtitle.users.dictionary.model.UserDictionaryWord;
import subtitle.users.files.model.FileText;
import subtitle.users.words.model.WordEntity;

import java.util.List;

public interface FileTextWordsRepository extends JpaRepository<WordEntity, Long> {
    Page<WordEntity> findAllByFileText(FileText fileText, Pageable pageable);
    Page<WordEntity> findAllByFileTextAndKnown(FileText fileText, boolean known, Pageable pageable);
    List<WordEntity> findAllByWord(String word);
    @Query("SELECT word FROM " + WordEntity.ENTITY_NAME + " word join word.fileText.user user " +
            "WHERE user = :user AND word.word = :word")
    List<WordEntity> findAllByUserAndWord(@Param("user") User user, @Param("word") String word);
    List<WordEntity> findAllByUserDictionaryWord(UserDictionaryWord userDictionaryWord);
}
