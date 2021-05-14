package subtitle.users.files.model;

import javax.persistence.*;

import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import subtitle.base.model.BaseEntity;
import subtitle.security.model.User;
import subtitle.users.sentance.model.SentenceEntity;
import subtitle.users.words.model.WordEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = FileText.ENTITY_NAME)
@Table(name = FileText.TABLE_NAME)
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FileText extends BaseEntity {
	public static final String ENTITY_NAME = "FileText";
	public static final String TABLE_NAME = "file_text";
	
	public FileText(MultipartFile file, User user) {
		this.user = user;
		String originalFilename = file.getOriginalFilename();
		if (originalFilename!= null ) {
			this.filename = StringUtils.cleanPath(originalFilename);
		}
		this.state = States.LOADED;
		this.type = file.getContentType();
		this.fileData = new FileData(file);
		this.creationDate = LocalDateTime.now();
	}
	
	@Column
	private String filename;

	@Column
	private LocalDateTime creationDate;
	
	@Column
	private String type;
	
	@Column
	@Enumerated(EnumType.STRING)
	private States state;
	
	@Column(columnDefinition = "text")
	private String description;

	@Column
	private String size;
	
	@JsonIgnore
	@OneToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.LAZY)
	private FileData fileData;

	@JsonIgnore
	@OneToMany(mappedBy = "fileText", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<SentenceEntity> sentences = new ArrayList<>();

	@JsonIgnore
	@OneToMany(mappedBy = "fileText", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<WordEntity> words = new ArrayList<>();

	@ManyToOne
	@JoinColumn(nullable = false)
	private User user;

	public enum States {
		LOADED,
		ERROR,
		COMPLETED	
	} 
	
}
