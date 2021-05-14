package subtitle.users.sentance.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import subtitle.base.model.BaseEntity;
import subtitle.users.files.model.FileText;

import javax.persistence.*;

@Entity(name = SentenceEntity.ENTITY_NAME)
@Table(name = SentenceEntity.TABLE_NAME)
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties({"id","number","fileText"})
public class SentenceEntity extends BaseEntity {
    public static final String ENTITY_NAME = "Sentence";
    public static final String TABLE_NAME = "sentence";

    @Column(columnDefinition = "text")
    private String sentence;
    @Column
    private int number;
    @ManyToOne
    private FileText fileText;

    public SentenceEntity(int number, String sentence, FileText fileText){
        this.number = number;
        this.sentence = sentence;
        this.fileText = fileText;
        fileText.getSentences().add(this);
    }
}