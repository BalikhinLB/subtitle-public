package subtitle.users.dictionary.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import subtitle.base.model.BaseEntity;
import subtitle.security.model.User;
import subtitle.users.words.model.WordEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = UserDictionaryWord.ENTITY_NAME)
@Table(name = UserDictionaryWord.TABLE_NAME)
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class UserDictionaryWord extends BaseEntity {
    public static final String ENTITY_NAME = "UserDictionaryWord";
    public static final String TABLE_NAME = "user_dict_word";

    @Column
    private String word;
    @Column
    private String transcription;
    @Column(columnDefinition = "text")
    private String description;

    @ManyToOne
    private User user;

    public UserDictionaryWord(WordEntity word, User user) {
        this.word = word.getWord();
        word.setUserDictionaryWord(this);
        this.user = user;
    }

}
