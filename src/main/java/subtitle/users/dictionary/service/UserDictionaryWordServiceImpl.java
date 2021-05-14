package subtitle.users.dictionary.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subtitle.base.exception.CantFindObjectException;
import subtitle.base.exception.SubtitleException;
import subtitle.dictionary.model.StarDictWord;
import subtitle.restapi.ui.model.WordDescriptionRequest;
import subtitle.security.model.User;
import subtitle.users.dictionary.model.UserDictionaryWord;
import subtitle.users.dictionary.repository.UserDictionaryWordRepository;
import subtitle.users.words.model.WordEntity;
import subtitle.users.words.service.UserWordsService;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Service
public class UserDictionaryWordServiceImpl implements UserDictionaryWordService {
    @Autowired
    private UserWordsService userWordsService;
    @Autowired
    private UserDictionaryWordRepository userDictionaryWordRepository;

    @Override
    @Transactional
    public boolean deleteUserDictionaryWord(Long wordId, User user) throws SubtitleException {
        WordEntity word = userWordsService.findWordByIdAndUser(wordId, user);
        if (word.getUserDictionaryWord() == null) {
            String error = String.format("Word %s does not contain user description", wordId);
            throw new CantFindObjectException(error);
        }
        return removeDictWord(word.getUserDictionaryWord());
    }

    private boolean removeDictWord(UserDictionaryWord userDictionaryWord) {
        List<WordEntity> words = userWordsService.findWordsByUserDictionaryWord(userDictionaryWord);
        words.forEach(w -> w.setUserDictionaryWord(null));
        userDictionaryWordRepository.delete(userDictionaryWord);
        return true;
    }

    @Override
    @Transactional
    public boolean updateDescription(Long wordId, WordDescriptionRequest description, User user)
                                                                        throws SubtitleException {
        description = trimDescription(description);
        if (isDescriptionEmpty(description)) return deleteUserDictionaryWord(wordId, user);
        WordEntity word = userWordsService.findWordByIdAndUser(wordId, user);
        UserDictionaryWord oldDictWord = word.getUserDictionaryWord();
        UserDictionaryWord newDictWord;
        StarDictWord starDictWord = word.getStardictWord();

        if (oldDictWord != null) {
            if (!Objects.equals(oldDictWord.getTranscription(), description.getTranscription())) {
                oldDictWord.setTranscription(description.getTranscription());
            }
            if (!Objects.equals(oldDictWord.getDescription(), description.getDescription())){
                oldDictWord.setDescription(description.getDescription());
            }
            if (isUserDictWordEqualSarDictWord(oldDictWord, starDictWord)) {
                removeDictWord(oldDictWord);
            }
        } else {
            newDictWord = new UserDictionaryWord(word.getWord(), description.getTranscription(),
                    description.getDescription(), user);
            if (!isUserDictWordEqualSarDictWord(newDictWord, starDictWord)) {
                addNewDictWord(newDictWord);
            }
        }
        return true;
    }

    private WordDescriptionRequest trimDescription(WordDescriptionRequest description) {
        String transcription = StringUtils.isBlank(description.getTranscription()) ?
                null : description.getTranscription().trim();
        String definition = StringUtils.isBlank(description.getDescription()) ? null : description.getDescription().trim();
        return new WordDescriptionRequest(transcription, definition);
    }

    private boolean isDescriptionEmpty(WordDescriptionRequest description) {
        return (description.getTranscription() == null) && (description.getDescription() == null);

    }

    private void addNewDictWord(UserDictionaryWord newDictWord) {
        newDictWord = userDictionaryWordRepository.save(newDictWord);
        //not empty, because we have 1 word from request
        List<WordEntity> words = userWordsService.findAllWordsByUserAndWord(newDictWord.getUser(), newDictWord.getWord());
        for (WordEntity w : words) {
            w.setUserDictionaryWord(newDictWord);
        }
    }

    private boolean isUserDictWordEqualSarDictWord(@NotNull UserDictionaryWord userDictWord,
                                                   @NotNull StarDictWord starDictWord){
        return Objects.equals(userDictWord.getDescription(), starDictWord.getDescription()) &&
                Objects.equals(userDictWord.getTranscription(), starDictWord.getTranscription());
    }
}
