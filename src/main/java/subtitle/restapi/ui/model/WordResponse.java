package subtitle.restapi.ui.model;

import lombok.Data;
import subtitle.users.sentance.model.SentenceEntity;
import subtitle.users.words.model.Tags;
import subtitle.users.words.model.WordEntity;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class WordResponse {

    Long id;
    String word;
    Set<String> wordForms;
    Set<Tags> tags;
    int count;
    boolean known;
    Set<String> sentences;
    Translate translate;

    public WordResponse(WordEntity word) {
        this.id = word.getId();
        this.word = word.getWord();
        this.wordForms = word.getWordForms();
        this.tags = word.getTags();
        this.count = word.getCount();
        this.known = word.getKnown();
        this.sentences = word.getSentences()
                .stream()
                .map(SentenceEntity::getSentence)
                .collect(Collectors.toSet());
        if (word.getUserDictionaryWord() != null) {
            this.translate = new Translate(word.getUserDictionaryWord().getTranscription(), word.getUserDictionaryWord().getDescription());
        } else {
            if (word.getStardictWord() != null) {
                this.translate = new Translate(word.getStardictWord().getTranscription(), word.getStardictWord().getDescription());
            }
        }
    }

    @Data
    public static class Translate {
        private String transcription;
        private String description;

        public Translate(String transcription, String description) {
            this.transcription = transcription;
            this.description = description;
        }
    }
}
