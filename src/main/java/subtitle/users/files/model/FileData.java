package subtitle.users.files.model;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import subtitle.base.model.BaseEntity;

@Entity(name = FileData.ENTITY_NAME)
@Table(name = FileData.TABLE_NAME)
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FileData extends BaseEntity {
	public static final String ENTITY_NAME = "FileData";
	public static final String TABLE_NAME = "file_data";
	
	public FileData(MultipartFile file) {
		this.file = file;
	}
	
	@Lob
	private java.sql.Blob data;
	
	@Transient
	@JsonIgnore
	private MultipartFile file;

}
