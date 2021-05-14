package subtitle.base.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;

import lombok.Data;
/**
 * Base Entity class with id
 *
 */
@MappedSuperclass
@Data
public class BaseEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "ID_GENERATOR")
	@SequenceGenerator(name="ID_GENERATOR", sequenceName="JPWH_SEQUENCE", initialValue = 1000, allocationSize = 1)
	private Long id;

}
