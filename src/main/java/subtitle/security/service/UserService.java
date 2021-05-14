package subtitle.security.service;

import org.springframework.http.ResponseEntity;

import subtitle.base.exception.SubtitleException;
import subtitle.restapi.ui.model.LoginRequest;
import subtitle.restapi.ui.model.SignupRequest;
import subtitle.security.model.User;


public interface UserService {
    ResponseEntity<String> login(LoginRequest loginRequest, String accessToken, String refreshToken) throws SubtitleException;

    ResponseEntity<String> refresh(String accessToken, String refreshToken);
    
    ResponseEntity<String> removeToken();

    User signup(SignupRequest request) throws SubtitleException;

    User getUserProfile();

}
