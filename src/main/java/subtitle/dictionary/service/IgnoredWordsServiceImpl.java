package subtitle.dictionary.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subtitle.base.exception.CantFindObjectException;
import subtitle.dictionary.model.IgnoredWord;
import subtitle.dictionary.repository.IgnoredWordRepository;
import subtitle.users.words.model.WordEntity;
import subtitle.users.words.repository.FileTextWordsRepository;

import java.util.List;

@Service
public class IgnoredWordsServiceImpl implements IgnoredWordsService {

    private final IgnoredWordRepository ignoredWordRepository;
    private final FileTextWordsRepository fileTextWordsRepository;
    @Autowired
    public IgnoredWordsServiceImpl(IgnoredWordRepository ignoredWordRepository, FileTextWordsRepository fileTextWordsRepository) {
        this.ignoredWordRepository = ignoredWordRepository;
        this.fileTextWordsRepository = fileTextWordsRepository;
    }

    @Override
    public boolean existsByWord(String word) {
        return ignoredWordRepository.existsByWord(word);
    }

    @Override
    @Transactional
    public boolean putWordToIgnored(Long fileTextWordId) throws CantFindObjectException {
        WordEntity word = fileTextWordsRepository.findById(fileTextWordId)
                .orElseThrow(() -> new CantFindObjectException(String.format("Can`t find FileTextWord (id:%s)", fileTextWordId)));
        if (!ignoredWordRepository.existsByWord(word.getWord())) {
            IgnoredWord ignoredWord = new IgnoredWord(word.getWord());
            ignoredWordRepository.save(ignoredWord);
            List<WordEntity> ignoredWords = fileTextWordsRepository.findAllByWord(word.getWord());
            ignoredWords.forEach(fileTextWordsRepository::delete);
        } else {
            fileTextWordsRepository.delete(word);
        }

        return true;
    }
}
