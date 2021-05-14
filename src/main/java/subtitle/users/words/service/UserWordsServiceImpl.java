package subtitle.users.words.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subtitle.base.exception.CantFindObjectException;
import subtitle.base.exception.ForbiddenException;
import subtitle.base.exception.SubtitleException;
import subtitle.restapi.ui.model.WordDescriptionRequest;
import subtitle.security.model.User;
import subtitle.users.dictionary.model.UserDictionaryWord;
import subtitle.users.dictionary.model.UserKnownWord;
import subtitle.users.dictionary.repository.UserDictionaryWordRepository;
import subtitle.users.dictionary.repository.UserKnownWordRepository;
import subtitle.users.files.model.FileText;
import subtitle.users.files.service.FileTextService;
import subtitle.users.words.model.WordEntity;
import subtitle.users.words.repository.FileTextWordsRepository;
import subtitle.restapi.ui.model.WordResponse;
import subtitle.restapi.ui.model.WordsPageable;

import java.util.List;
import java.util.Objects;

@Service
public class UserWordsServiceImpl implements UserWordsService {
    FileTextService fileTextService;
    FileTextWordsRepository wordsRepository;
    UserKnownWordRepository knownWordRepository;

    UserDictionaryWordRepository userDictionaryWordRepository;
    @Autowired
    public void setFileTextService(FileTextService fileTextService) {
        this.fileTextService = fileTextService;
    }
    @Autowired
    public void setWordsRepository(FileTextWordsRepository wordsRepository) {
        this.wordsRepository = wordsRepository;
    }
    @Autowired
    public void setKnownWordRepository(UserKnownWordRepository knownWordRepository) {
        this.knownWordRepository = knownWordRepository;
    }
    @Autowired
    public void setUserDictionaryWordRepository(UserDictionaryWordRepository userDictionaryWordRepository) {
        this.userDictionaryWordRepository = userDictionaryWordRepository;
    }

    @Override
    @Transactional
    public WordsPageable getAllWordsByFileText(Long fileTextId, User user,
                                               Boolean known, Pageable pageable) throws SubtitleException {
        FileText fileText = fileTextService.getFile(fileTextId, user);
        if (!Objects.equals(FileText.States.COMPLETED, fileText.getState())){
            throw new ForbiddenException(String.format("Can`t get words from FileText (id:%s) in state: %s", fileTextId, fileText.getState()));
        }
        Page<WordEntity> wordsPage;
        if (known == null)
            wordsPage = wordsRepository.findAllByFileText(fileText, pageable);
        else
            wordsPage = wordsRepository.findAllByFileTextAndKnown(fileText, known, pageable);
        WordsPageable wordsPageable = new WordsPageable();
        wordsPageable.setCurrentPage(wordsPage.getNumber());
        wordsPageable.setTotalItems(wordsPage.getTotalElements());
        wordsPageable.setWords(wordsPage.getContent());
        return wordsPageable;
    }

    @Override
    @Transactional
    public WordEntity setWordKnown(Long wordId, boolean known, User user) throws SubtitleException {
        WordEntity word = findWordByIdAndUser(wordId, user);
        if (known) {
            if (!knownWordRepository.existsByWordAndUser(word.getWord(), user)) {
                UserKnownWord knownWord = new UserKnownWord(word, user);
                knownWordRepository.save(knownWord);
            }
        } else {
            UserKnownWord knownWord = knownWordRepository.findByWordAndUser(word.getWord(), user);
            if (knownWord != null) knownWordRepository.delete(knownWord);
        }
        wordsRepository.findAllByUserAndWord(user, word.getWord()).forEach(w -> w.setKnown(known));
        return word;
    }

    @Override
    @Transactional
    public WordResponse setDescription(Long wordId, WordDescriptionRequest wordDescriptionRequest,
                                       User user) throws SubtitleException {
        WordEntity word = findWordByIdAndUser(wordId, user);
        UserDictionaryWord dictionaryWord = word.getUserDictionaryWord();
        if (dictionaryWord == null) {
            dictionaryWord = new UserDictionaryWord(word, user);
            userDictionaryWordRepository.save(dictionaryWord);

        }
        dictionaryWord.setTranscription(wordDescriptionRequest.getTranscription());
        dictionaryWord.setDescription(wordDescriptionRequest.getDescription());
        return new WordResponse(word);
    }

    @Override
    public List<WordEntity> findWordsByUserDictionaryWord(UserDictionaryWord userDictionaryWord) {
        return wordsRepository.findAllByUserDictionaryWord(userDictionaryWord);
    }

    @Override
    public List<WordEntity> findAllWordsByUserAndWord(User user, String word) {
        return wordsRepository.findAllByUserAndWord(user, word);
    }

    public WordEntity findWordByIdAndUser(Long id, User user) throws SubtitleException {
        WordEntity word = wordsRepository.findById(id)
                .orElseThrow(() -> new CantFindObjectException(String.format("Can`t find word (id:%s)",id)));
        if (!word.getFileText().getUser().equals(user)) {
            throw new ForbiddenException(String.format("Word (id:%s) is not user`s (id:%s)", id, user.getId()));
        }
        return word;
    }
}
