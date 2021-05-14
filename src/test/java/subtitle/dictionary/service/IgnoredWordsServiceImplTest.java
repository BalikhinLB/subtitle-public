package subtitle.dictionary.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import subtitle.base.exception.CantFindObjectException;
import subtitle.dictionary.model.IgnoredWord;
import subtitle.dictionary.repository.IgnoredWordRepository;
import subtitle.users.words.model.WordEntity;
import subtitle.users.words.repository.FileTextWordsRepository;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class IgnoredWordsServiceImplTest {
    public static final String EXIST = "exist";
    public static final String NOT_EXIST = "not exist";
    @Mock
    private IgnoredWordRepository ignoredWordRepository;
    @Mock
    private FileTextWordsRepository fileTextWordsRepository;
    @Spy
    IgnoredWordsService wordsService;

    @BeforeEach
    void init(){
        MockitoAnnotations.initMocks(this);
        Mockito.when(ignoredWordRepository.existsByWord(EXIST)).thenReturn(true);
        Mockito.when(ignoredWordRepository.existsByWord(NOT_EXIST)).thenReturn(false);
        wordsService = Mockito.spy(new IgnoredWordsServiceImpl(ignoredWordRepository, fileTextWordsRepository));
    }

    @Test
    void existsByWord() {
        assertTrue(wordsService.existsByWord(EXIST));
        assertFalse(wordsService.existsByWord(NOT_EXIST));
    }

    @Test
    void putWordToIgnored_cantFind() {
        assertThrows(CantFindObjectException.class, () -> wordsService.putWordToIgnored(12L));
    }

    @Test
    void putWordToIgnored() throws CantFindObjectException {
        WordEntity wordExist = new WordEntity();
        wordExist.setWord(EXIST);

        WordEntity wordNotExist = new WordEntity();
        wordNotExist.setWord(NOT_EXIST);

        Mockito.when(fileTextWordsRepository.findById(1L)).thenReturn(Optional.of(wordExist));
        Mockito.when(fileTextWordsRepository.findById(2L)).thenReturn(Optional.of(wordNotExist));

        Mockito.when(fileTextWordsRepository.findAllByWord(NOT_EXIST)).thenReturn(Collections.singletonList(wordNotExist));

        assertTrue(wordsService.putWordToIgnored(1L));
        assertTrue(wordsService.putWordToIgnored(2L));
        Mockito.verify(ignoredWordRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(ignoredWordRepository, Mockito.times(1)).save(new IgnoredWord(NOT_EXIST));
        Mockito.verify(fileTextWordsRepository, Mockito.times(1)).findAllByWord(Mockito.any());
        Mockito.verify(fileTextWordsRepository, Mockito.times(1)).findAllByWord(NOT_EXIST);
        Mockito.verify(fileTextWordsRepository, Mockito.times(1)).delete(wordExist);
        Mockito.verify(fileTextWordsRepository, Mockito.times(1)).delete(wordNotExist);
        Mockito.verify(fileTextWordsRepository, Mockito.times(2)).delete(Mockito.any());
    }
}