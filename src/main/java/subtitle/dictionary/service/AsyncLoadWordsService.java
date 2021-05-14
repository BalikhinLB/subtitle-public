package subtitle.dictionary.service;

import java.util.List;

public interface AsyncLoadWordsService {
    void saveWords(List<String> word, int party);
}
