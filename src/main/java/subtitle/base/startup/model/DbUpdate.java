package subtitle.base.startup.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import subtitle.base.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity(name = DbUpdate.ENTITY_NAME)
@Table(name = DbUpdate.TABLE_NAME)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DbUpdate extends BaseEntity {

    public static final String ENTITY_NAME = "DbUpdate";
    public static final String TABLE_NAME = "db_update";

    @Column(unique = true)
    private String name;
    @Column
    private LocalDateTime createDate;
}
