package subtitle.dictionary.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import subtitle.base.model.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = StarDictWord.ENTITY_NAME)
@Table(name = StarDictWord.TABLE_NAME)
public class StarDictWord extends BaseEntity{
	public static final String ENTITY_NAME = "StardictWord";
	public static final String TABLE_NAME = "star_dict_words";
	
	@Column
	private String word;
	@Column 
	private String transcription;
	@Column(columnDefinition = "TEXT")
	private String description;
	
	
}
