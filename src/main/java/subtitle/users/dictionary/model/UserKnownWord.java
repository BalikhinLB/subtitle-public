package subtitle.users.dictionary.model;

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

@Entity(name = UserKnownWord.ENTITY_NAME)
@Table(name = UserKnownWord.TABLE_NAME)
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class UserKnownWord extends BaseEntity {
    public static final String ENTITY_NAME = "UserKnownWord";
    public static final String TABLE_NAME = "user_known_word";


    public UserKnownWord(WordEntity word, User user) {
        this.word = word.getWord();
        this.user = user;
    }
    @Column
    String word;
    @ManyToOne
    User user;
}
