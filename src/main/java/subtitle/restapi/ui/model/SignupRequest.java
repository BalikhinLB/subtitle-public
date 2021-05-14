package subtitle.restapi.ui.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
	
	@NotBlank
	private String login;
	
	@NotBlank
	@Email
	private String email;
	
	@NotBlank
	private String password;

}
