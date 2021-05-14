package subtitle.nlp.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javax.persistence.EntityNotFoundException;

import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import lombok.extern.java.Log;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import subtitle.dictionary.model.StarDictWord;
import subtitle.dictionary.repository.StarDictWordRepository;
import subtitle.dictionary.service.IgnoredWordsService;
import subtitle.nlp.util.NlpUtils;
import subtitle.security.model.User;
import subtitle.users.dictionary.model.UserDictionaryWord;
import subtitle.users.dictionary.repository.UserDictionaryWordRepository;
import subtitle.users.dictionary.repository.UserKnownWordRepository;
import subtitle.users.files.model.FileText;
import subtitle.users.files.model.FileText.States;
import subtitle.users.files.repository.FileTextRepository;
import subtitle.users.sentance.model.SentenceEntity;
import subtitle.users.words.model.Tags;
import subtitle.users.words.model.WordEntity;
import subtitle.users.words.utils.IdentificationTagUtil;

@Log
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NlpParsingServiceImpl implements NlpParsingService {

    private static final String SENT_MODEL = "nlp/en-sent.bin";
    private static final String TOKEN_MODEL = "nlp/en-token.bin";
    private static final String POS_MODEL = "nlp/en-pos-maxent.bin";
    private static final String LEMMATIZER_MODEL = "nlp/en-lemmatizer.dict";


    private final FileTextRepository fileTextRepository;
    private final StarDictWordRepository stardictWordRepository;
    private final IgnoredWordsService ignoredWordsService;
    private final UserKnownWordRepository userKnownWordRepository;
    private final UserDictionaryWordRepository userDictionaryWordRepository;
    private final TransactionalSaveErrorToFileTextService saveErrorToFileTextService;

    public NlpParsingServiceImpl(FileTextRepository fileTextRepository, StarDictWordRepository stardictWordRepository,
                                 IgnoredWordsService ignoredWordsService, UserKnownWordRepository userKnownWordRepository,
                                 UserDictionaryWordRepository userDictionaryWordRepository,
                                 TransactionalSaveErrorToFileTextService saveErrorToFileTextService) throws IOException {
        this.fileTextRepository = fileTextRepository;
        this.stardictWordRepository = stardictWordRepository;
        this.ignoredWordsService = ignoredWordsService;
        this.userKnownWordRepository = userKnownWordRepository;
        this.userDictionaryWordRepository = userDictionaryWordRepository;
        this.saveErrorToFileTextService = saveErrorToFileTextService;
        initModels();
    }

    private SentenceDetectorME sentenceDetector;
    private TokenizerME tokenizer;
    private POSTaggerME posTagger;
    private DictionaryLemmatizer lemmatizer;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void parsingFile(FileText fileText) {
        log.info("Start parsing file:" + fileText.getFilename()+" object " + this);
        try {
            String textString = NlpUtils.getStringFromBlob(fileText.getType(), fileText.getFileData().getData());
            fileText = fileTextRepository.getOne(fileText.getId());
            String[] sentences = sentenceDetector.sentDetect(textString);
            log.info(String.format("It found %s sentences in fileText (id:%s)", sentences.length, fileText.getId()));
            Map<String, WordEntity> mapWords = new HashMap<>();
            int sentenceNumber = 0;
            for (String sentenceString : sentences) {
                if (StringUtils.isEmpty(sentenceString)) continue;
                sentenceNumber++;
                SentenceEntity sentence = new SentenceEntity(sentenceNumber, sentenceString, fileText);
                String[] tokens = tokenizer.tokenize(sentenceString);
                String[] tags = posTagger.tag(tokens);
                String[] lemmas = lemmatizer.lemmatize(tokens, tags);

                checkAndGetWord(tokens, tags, lemmas, sentence, fileText, mapWords);
            }
            log.info("End parsing file:" + fileText.getFilename());
            fileTextRepository.save(fileText);
            fileText.setState(States.COMPLETED);
            fileTextRepository.flush();
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            saveErrorToFileTextService.setError(fileText);
        }

    }


    private void checkAndGetWord(String[] tokens, String[] tags,
                                 String[] lemmas, SentenceEntity sentence, FileText fileText,
                                 Map<String, WordEntity> mapWords) {
        for (int i = 0; i < tokens.length; i++) {
            String tokenString = tokens[i];

            String tagString = tags[i];
            String lemmaString = lemmas[i];
            Tags tag = IdentificationTagUtil.getTag(tagString);
            if (!tag.isImpotent()) continue;
            if (lemmaString.equals("O")) {
                lemmaString = getLemmaFromString(tokenString);
            }
            if (isValidLemma(lemmaString) && !ignoredWordsService.existsByWord(lemmaString)) {
                WordEntity word = mapWords.computeIfAbsent(lemmaString, ls -> {
                    StarDictWord stardictWord = stardictWordRepository.findByWord(ls);
                    UserDictionaryWord userDictionaryWord = userDictionaryWordRepository.findByWordAndUser(ls, fileText.getUser());
                    boolean known = isKnownWord(ls, fileText.getUser());
                    return new WordEntity(ls, stardictWord, userDictionaryWord, known, fileText);
                });
                word.addNewItem(tokenString, tag, sentence);
            }
        }
    }

    private boolean isKnownWord(String lemma, User user) {
        return userKnownWordRepository.existsByWordAndUser(lemma, user);
    }

    private boolean isValidLemma(String lemmaString) {
        if (StringUtils.isEmpty(lemmaString)) return false;
        if (lemmaString.length() <= 1) return false;
        return lemmaString.matches("^[a-z]*$");
    }

    private String getLemmaFromString(String tokenString) {
        tokenString = tokenString.toLowerCase(Locale.ROOT).replaceAll("[^a-z]", "");
        return tokenString;
    }

    private void initModels() throws IOException {
        initSentModel();
        initTokenModel();
        initPosModel();
        initLemmatizerModel();
    }

    private void initSentModel() throws IOException {
        try (InputStream is = new ClassPathResource(SENT_MODEL).getInputStream()) {
            SentenceModel model = new SentenceModel(is);
            sentenceDetector = new SentenceDetectorME(model);
        }
    }

    private void initTokenModel() throws IOException {
        try (InputStream is = new ClassPathResource(TOKEN_MODEL).getInputStream()) {
            TokenizerModel model = new TokenizerModel(is);
            tokenizer = new TokenizerME(model);
        }
    }

    private void initPosModel() throws IOException {
        try (InputStream is = new ClassPathResource(POS_MODEL).getInputStream()) {
            POSModel posModel = new POSModel(is);
            posTagger = new POSTaggerME(posModel);
        }
    }

    private void initLemmatizerModel() throws IOException {
        try (InputStream is = new ClassPathResource(LEMMATIZER_MODEL).getInputStream()) {
            lemmatizer = new DictionaryLemmatizer(is);
        }
    }
}
