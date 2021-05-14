package subtitle.dictionary.service;

import subtitle.base.exception.CantFindObjectException;

public interface IgnoredWordsService {
    boolean existsByWord(String word);
    boolean putWordToIgnored(Long fileTextWordId) throws CantFindObjectException;
}
