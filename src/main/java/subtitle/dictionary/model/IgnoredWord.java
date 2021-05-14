package subtitle.dictionary.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import subtitle.base.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * It`s simple words like 'be', 'not', '`s' and etc.
 * */
@Entity(name = IgnoredWord.ENTITY_NAME)
@Table(name = IgnoredWord.TABLE_NAME)
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class IgnoredWord extends BaseEntity {
    public static final String ENTITY_NAME = "IgnoredWord";
    public static final String TABLE_NAME = "ignored_words";

    @Column
    private String word;

    public IgnoredWord(String word){
        this.word = word;
    }

}
