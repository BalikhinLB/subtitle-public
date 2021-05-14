package subtitle.restapi.ui.model;

import lombok.Data;
import subtitle.users.words.model.WordEntity;

import java.util.ArrayList;
import java.util.List;

@Data
public class WordsPageable {
    private int currentPage;
    private long totalItems;
    private List<WordEntity> words;

    public List<WordEntity> getWords() {
        if (words == null) {
            words = new ArrayList<>();
        }
        return words;
    }
}
