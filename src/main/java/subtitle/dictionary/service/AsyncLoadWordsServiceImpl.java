package subtitle.dictionary.service;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import subtitle.dictionary.model.StarDictWord;
import subtitle.dictionary.repository.StarDictWordRepository;

import java.util.List;

@Log
@Service
public class AsyncLoadWordsServiceImpl implements AsyncLoadWordsService {
    private static final String END_OF_WORD = "</k>";
    private static final String START_OF_TRANSCRIPTION = "<tr>";
    private static final String END_OF_TRANSCRIPTION = "</tr>";
    private static final String END_OF_WAV = "</rref>";
    private static final String REGEX_TAG = "<[^>]+?>";

    private final StarDictWordRepository wordRepository;

    @Autowired
    public AsyncLoadWordsServiceImpl(StarDictWordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    @Async
    public void saveWords(List<String> words, int party) {
        for (String word : words) {
            StarDictWord starDictWord = getStarDictWord(word);
            if (starDictWord != null && !wordRepository.existsByWord(starDictWord.getWord())) {
                wordRepository.save(starDictWord);
            }
        }
        log.info(String.format("It saved %s words, party:%s", words.size(), party));

    }


    protected StarDictWord getStarDictWord(String word) {
        StarDictWord starDictWord = new StarDictWord(null, null, word);
        //methods ordered
        try {
            initWord(starDictWord);
        } catch (Exception e) {
            log.warning("Can`t find word in string for dictionary.");
            log.warning(word);
            return null;
        }
        initTranscription(starDictWord);
        removeWav(starDictWord);
        brushStarDictWordEntries(starDictWord);

        return starDictWord;
    }

    private void removeWav(StarDictWord starDictWord) {
        String description = starDictWord.getDescription();
        int indexOfEndWav = description.lastIndexOf(END_OF_WAV);
        indexOfEndWav = indexOfEndWav > 0 ? indexOfEndWav + END_OF_WAV.length() : 0;
        description = description.substring(indexOfEndWav);
        starDictWord.setDescription(description);
    }

    private void initWord(StarDictWord stardictWord) {
        String description = stardictWord.getDescription();
        int indexEndOfWord = description.lastIndexOf(END_OF_WORD);
        if (indexEndOfWord < 0) {
            throw new IllegalArgumentException();
        }
        indexEndOfWord = indexEndOfWord + END_OF_WORD.length();
        String word = description
                .substring(0, indexEndOfWord)
                .replaceAll(REGEX_TAG, "")
                .trim();
        stardictWord.setWord(word);
        description = description.substring(indexEndOfWord);
        stardictWord.setDescription(description);
    }

    private void initTranscription(StarDictWord stardictWord) {
        String description = stardictWord.getDescription();
        int indexOfEndTranscription = description.indexOf(END_OF_TRANSCRIPTION);
        indexOfEndTranscription = indexOfEndTranscription > 0 ?
                indexOfEndTranscription + END_OF_TRANSCRIPTION.length() : 0;
        String transcription = description
                .substring(0, indexOfEndTranscription)
                .replaceAll(REGEX_TAG, "")
                .trim();
        if (transcription.length() > 255) transcription = "";
        description = description.substring(indexOfEndTranscription);
        stardictWord.setTranscription(transcription);
        stardictWord.setDescription(description);
    }

    private void brushStarDictWordEntries(StarDictWord stardictWord) {
        stardictWord.setDescription(brushString(stardictWord.getDescription()));
        stardictWord.setWord(brushString(stardictWord.getWord()));
    }

    private String brushString(String string) {
        string = string
                .replace(START_OF_TRANSCRIPTION, "[")
                .replace(END_OF_TRANSCRIPTION, "]")
                .replace("_", "")
                .replace("=", "")
                .replace("&gt;", ".")
                .replace("&apos;", "'")
                .replaceAll(REGEX_TAG, "")
                .trim();
        return string;
    }
}
