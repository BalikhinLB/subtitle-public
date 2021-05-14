package subtitle.dictionary.service;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.java.Log;
import org.springframework.util.CollectionUtils;
import subtitle.base.startup.DbUpdateAfterStart;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
@Log
public class BootStartLoadDictionaryService implements DbUpdateAfterStart {

    private static final String DB_UPDATE_NAME = "DICTIONARY_LOADING";
    private static final int COUNT_WORDS_TO_SAVE = 1000;
    private static final String START_OF_WORD = "<k>";


    private final AsyncLoadWordsService asyncLoadWordsService;

    @Getter(AccessLevel.PRIVATE)
    private String dictionaryPath;

    @Autowired
    public BootStartLoadDictionaryService(AsyncLoadWordsService asyncLoadWordsService) {
        this.asyncLoadWordsService = asyncLoadWordsService;
    }

    @Value("${lb.dict.path}")
    public void setDictionaryPath(String dictionaryPath) {
        this.dictionaryPath = dictionaryPath;
    }

    @Override
    public void dbUpdate() {
       log.info("Starting loading dictionary into the database");
       loadFromFile();
       log.info("Dictionary was loaded into the database");

    }

    @Override
    public boolean isRepeatable() {
        return false;
    }

    @Override
    public String getName() {
        return DB_UPDATE_NAME;
    }

    private void loadFromFile() {
        File file = new File(getDictionaryPath());
        try (Scanner scanner = new Scanner(file)) {
            int index = 0;
            int party = 0;
            scanner.useDelimiter(START_OF_WORD);
            List<String> words = new ArrayList<>();
            while (scanner.hasNext()) {
                words.add(scanner.next());
                if (++index % COUNT_WORDS_TO_SAVE == 0){
                    //TODO create with Future
                    asyncLoadWordsService.saveWords(words, ++party);
                    words = new ArrayList<>();
                }
            }
            if (!CollectionUtils.isEmpty(words)) {
                asyncLoadWordsService.saveWords(words, ++party);
            }
            String msg = String.format("End of reading. It was %s words",index);
            log.info(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
