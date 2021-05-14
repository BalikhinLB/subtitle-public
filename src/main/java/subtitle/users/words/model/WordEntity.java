package subtitle.users.words.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import subtitle.base.model.BaseEntity;
import subtitle.dictionary.model.StarDictWord;
import subtitle.users.dictionary.model.UserDictionaryWord;
import subtitle.users.files.model.FileText;
import subtitle.users.sentance.model.SentenceEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = WordEntity.ENTITY_NAME)
@Table(name = WordEntity.TABLE_NAME)
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class WordEntity extends BaseEntity {
    public static final String ENTITY_NAME = "Word";
    public static final String TABLE_NAME = "word";

    @Column
    private String word;
    @ElementCollection
    @Column(name = "wordforms")
    private Set<String> wordForms = new HashSet<>();
    @Column
    private int count;
    @Column
    Boolean known;
    @ElementCollection
    @Enumerated(EnumType.STRING)
    @Column(name = "tag")
    private Set<Tags> tags = new HashSet<>();
    @JsonIgnore
    @ManyToOne
    private FileText fileText;
    @ManyToMany
    @OrderBy("number")
    private List<SentenceEntity> sentences = new ArrayList<>();
    @ManyToOne
    private StarDictWord stardictWord;
    @ManyToOne
    private UserDictionaryWord userDictionaryWord;

    public WordEntity(String word, StarDictWord stardictWord, UserDictionaryWord userDictionaryWord, boolean known, FileText fileText) {
        this.word = word;
        this.stardictWord = stardictWord;
        this.userDictionaryWord = userDictionaryWord;
        this.known = known;
        this.fileText = fileText;
        fileText.getWords().add(this);
    }

    public void addNewItem(String word, Tags tag, SentenceEntity sentence){
        this.count++;
        this.wordForms.add(word);
        this.tags.add(tag);
        if (!this.sentences.contains(sentence)) {
            this.sentences.add(sentence);
        }
    }

}
