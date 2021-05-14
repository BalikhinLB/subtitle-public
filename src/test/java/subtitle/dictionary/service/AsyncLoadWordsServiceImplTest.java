package subtitle.dictionary.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import subtitle.dictionary.model.StarDictWord;
import subtitle.dictionary.repository.StarDictWordRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;

class AsyncLoadWordsServiceImplTest {
    public static final String TEST_DICT_PATH = "src/test/resources/dictionary/ER-LingvoUniversal.dict";

    BootStartLoadDictionaryService loadDictionaryService;

    @Mock
    private StarDictWordRepository wordRepository;

    AsyncLoadWordsService asyncLoadWordsService;

    @BeforeEach
    void init() {

        MockitoAnnotations.initMocks(this);
        asyncLoadWordsService =  Mockito.spy(new AsyncLoadWordsServiceImpl(wordRepository));
        loadDictionaryService = Mockito.spy(new BootStartLoadDictionaryService(asyncLoadWordsService));
        loadDictionaryService.setDictionaryPath(TEST_DICT_PATH);
        Mockito.when(wordRepository.existsByWord("yes")).thenReturn(true);
        Mockito.when(wordRepository.existsByWord("not")).thenReturn(false);
    }

    @Test
    void saveWords() {
        StarDictWord expected = new StarDictWord("not", "nɔt", "нареч.\n" +
                " 1)\n" +
                "  а) не, нет, ни (n't [nt]);");
        loadDictionaryService.dbUpdate();

        //check correct BootStartLoadDictionaryService
        Mockito.verify(asyncLoadWordsService, Mockito.times(1))
                .saveWords(Mockito.any(), eq(1));
        //check not valid word
        Mockito.verify(wordRepository, Mockito.times(2)).existsByWord(Mockito.any());
        //check unique
        Mockito.verify(wordRepository, Mockito.times(1)).save(Mockito.any());
        //check brush word
        ArgumentCaptor<StarDictWord> requestCaptor = ArgumentCaptor.forClass(StarDictWord.class);
        Mockito.verify(wordRepository, Mockito.times(1)).save(requestCaptor.capture());
        StarDictWord actual = requestCaptor.getValue();
        assertEquals(expected.getWord(), actual.getWord());
        assertEquals(expected.getTranscription(), actual.getTranscription());
        assertEquals(expected.getDescription(), actual.getDescription());
    }

}