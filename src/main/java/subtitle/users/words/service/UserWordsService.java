package subtitle.users.words.service;

import org.springframework.data.domain.Pageable;
import subtitle.base.exception.SubtitleException;
import subtitle.restapi.ui.model.WordDescriptionRequest;
import subtitle.security.model.User;
import subtitle.users.dictionary.model.UserDictionaryWord;
import subtitle.users.words.model.WordEntity;
import subtitle.restapi.ui.model.WordResponse;
import subtitle.restapi.ui.model.WordsPageable;

import java.util.List;


public interface UserWordsService {
    WordsPageable getAllWordsByFileText(Long fileTextId, User user,
                                        Boolean known, Pageable pageable)
                                        throws SubtitleException;
    WordEntity findWordByIdAndUser(Long id, User user) throws SubtitleException;
    List<WordEntity> findWordsByUserDictionaryWord(UserDictionaryWord userDictionaryWord);
    List<WordEntity> findAllWordsByUserAndWord(User user, String word);
    WordEntity setWordKnown(Long wordId, boolean known, User user) throws SubtitleException;
    WordResponse setDescription(Long wordId, WordDescriptionRequest wordDescriptionRequest,
                                User user) throws SubtitleException;
}
