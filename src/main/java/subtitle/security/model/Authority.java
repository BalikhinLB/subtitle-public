package subtitle.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import subtitle.base.model.BaseEntity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;


@Entity(name = Authority.ENTITY_NAME)
@Table(name = Authority.TABLE_NAME)
@Data
@EqualsAndHashCode(callSuper = true, exclude = "users")
@NoArgsConstructor
@ToString
public class Authority extends BaseEntity implements Serializable {
  
	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY_NAME = "Authority";
	public static final String TABLE_NAME = "authority";

	public Authority(Role role){
	    this.role = role;
    }

    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;

    @JsonIgnore
    @ToString.Exclude
	@ManyToMany(mappedBy = "authoritiesSet", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<User> users;

    public GrantedAuthority grantedAuthority() {
        return new SimpleGrantedAuthority(this.role.name());
    }
}
