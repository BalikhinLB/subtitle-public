package subtitle.nlp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import subtitle.dictionary.repository.StarDictWordRepository;
import subtitle.dictionary.service.IgnoredWordsService;
import subtitle.users.dictionary.repository.UserDictionaryWordRepository;
import subtitle.users.dictionary.repository.UserKnownWordRepository;
import subtitle.users.files.repository.FileTextRepository;

import java.io.IOException;

class NlpParsingServiceImplTest {
    @Mock
    private FileTextRepository fileTextRepository;
    @Mock
    private StarDictWordRepository stardictWordRepository;
    @Mock
    private IgnoredWordsService ignoredWordsService;
    @Mock
    private UserKnownWordRepository userKnownWordRepository;
    @Mock
    private UserDictionaryWordRepository userDictionaryWordRepository;
    @Mock
    private TransactionalSaveErrorToFileTextService saveErrorToFileTextService;

    NlpParsingService nlpParsingService;

    @BeforeEach
    void init() throws IOException {
        MockitoAnnotations.initMocks(this);
        nlpParsingService = new NlpParsingServiceImpl(fileTextRepository, stardictWordRepository,
                                    ignoredWordsService, userKnownWordRepository,
                                    userDictionaryWordRepository, saveErrorToFileTextService);
    }

    @Test
    void parsingFile() {
        //TODO 1 read simple text - get what we wait

    }
}