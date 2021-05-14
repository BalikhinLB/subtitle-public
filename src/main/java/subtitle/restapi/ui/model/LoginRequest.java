package subtitle.restapi.ui.model;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class LoginRequest {
	
	@NotBlank(message = "Login cannot be empty")
	private String login;
	
	@NotBlank(message = "Password cannot be empty")
	private String password;
	

}
