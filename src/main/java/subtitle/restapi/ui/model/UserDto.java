package subtitle.restapi.ui.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subtitle.security.model.Authority;
import subtitle.security.model.Role;
import subtitle.security.model.User;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class UserDto {
    Long id;
    private String login;
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<Role> authorities;

    public UserDto(User user){
        this.id = user.getId();
        this.login = user.getLogin();
        this.email = user.getEmail();
        this.authorities = user.getAuthoritiesSet().stream()
                .map(Authority::getRole).collect(Collectors.toSet());
    }

}
