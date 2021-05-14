package subtitle.restapi.auth;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.server.ResponseStatusException;
import subtitle.base.restapi.AbstractApiService;
import subtitle.restapi.ui.model.SignupRequest;
import subtitle.restapi.ui.model.UserDto;
import subtitle.security.utils.SecurityCipher;
import subtitle.restapi.ui.model.LoginRequest;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController extends AbstractApiService {

	private final AuthenticationManager authenticationManager;

	@Autowired
	public ApiAuthController(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@CookieValue(name = "accessToken", required = false) String accessToken,
            						  @CookieValue(name = "refreshToken", required = false) String refreshToken,
            						  @Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication;
		try{
			authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getPassword()));
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Login or password is not correct");
		}
		try {
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String decryptedAccessToken = SecurityCipher.decrypt(accessToken);
			String decryptedRefreshToken = SecurityCipher.decrypt(refreshToken);
			return getUserService().login(loginRequest, decryptedAccessToken, decryptedRefreshToken);
		} catch (Exception e) {
			throw getException(e, "Can`t login");
		}
	}

    @PostMapping(value = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> refreshToken(@CookieValue(name = "accessToken", required = false) String accessToken,
                                                      @CookieValue(name = "refreshToken", required = false) String refreshToken) {
        String decryptedAccessToken = SecurityCipher.decrypt(accessToken);
        String decryptedRefreshToken = SecurityCipher.decrypt(refreshToken);
        try {
        	return getUserService().refresh(decryptedAccessToken, decryptedRefreshToken);
		} catch (Exception e) {
        	throw getException(e, "Can`t refresh token");
		}
        
    }
    
    @PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> removeToken() {
        return getUserService().removeToken();
    }
    
    @PostMapping("/signup")
	public UserDto registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		try {
			return new UserDto(getUserService().signup(signUpRequest));
		} catch (Exception e) {
			throw getException(e, "Can`t create user");
		}
	}

}
