package subtitle.security.service;

import subtitle.restapi.ui.model.LoginRequest;
import subtitle.restapi.ui.model.SignupRequest;
import subtitle.restapi.exeption.BadDataException;
import subtitle.restapi.exeption.CantCreateUserException;
import subtitle.security.jwt.TokenProvider;
import subtitle.security.jwt.Token;
import subtitle.security.model.Role;
import subtitle.security.model.User;
import subtitle.security.repository.AuthorityRepository;
import subtitle.security.repository.UserRepository;
import subtitle.security.utils.CookieUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final CookieUtil cookieUtil;
    private final PasswordEncoder encoder;
    private final AuthorityRepository authorityRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, TokenProvider tokenProvider, CookieUtil cookieUtil, PasswordEncoder encoder, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.cookieUtil = cookieUtil;
        this.encoder = encoder;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public ResponseEntity<String> login(LoginRequest loginRequest, String accessToken, String refreshToken) throws BadDataException {
        String login = loginRequest.getLogin();
        User user = userRepository.findByLogin(login).orElseThrow(() -> new BadDataException("User not found with email " + login));

        boolean accessTokenValid = !StringUtils.isEmpty(accessToken) && tokenProvider.validateToken(accessToken);
        boolean refreshTokenValid = !StringUtils.isEmpty(refreshToken) && tokenProvider.validateToken(refreshToken);

        HttpHeaders responseHeaders = new HttpHeaders();
        Token newAccessToken;
        Token newRefreshToken;
        if (!accessTokenValid && !refreshTokenValid) {
            newAccessToken = tokenProvider.generateAccessToken(user.getLogin());
            newRefreshToken = tokenProvider.generateRefreshToken(user.getLogin());
            addAccessTokenCookie(responseHeaders, newAccessToken);
            addRefreshTokenCookie(responseHeaders, newRefreshToken);
        }

        if (!accessTokenValid && refreshTokenValid) {
            newAccessToken = tokenProvider.generateAccessToken(user.getLogin());
            addAccessTokenCookie(responseHeaders, newAccessToken);
        }

        if (accessTokenValid && refreshTokenValid) {
            newAccessToken = tokenProvider.generateAccessToken(user.getLogin());
            newRefreshToken = tokenProvider.generateRefreshToken(user.getLogin());
            addAccessTokenCookie(responseHeaders, newAccessToken);
            addRefreshTokenCookie(responseHeaders, newRefreshToken);
        }

        return ResponseEntity.ok().headers(responseHeaders).body("Auth successful. Tokens are created in cookie.");
    }

    @Override
    public ResponseEntity<String> refresh(String accessToken, String refreshToken) {
        if (StringUtils.isEmpty(refreshToken) || !tokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Refresh Token is invalid!");
        }

        String currentUserLogin = tokenProvider.getUsernameFromToken(refreshToken);

        Token newAccessToken = tokenProvider.generateAccessToken(currentUserLogin);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.SET_COOKIE, cookieUtil.createAccessTokenCookie(newAccessToken.getTokenValue(), newAccessToken.getDuration()).toString());

        String info = "Auth successful. Tokens are created in cookie.";
        return ResponseEntity.ok().headers(responseHeaders).body(info);
    }
    
    

    @Override
	public ResponseEntity<String> removeToken() {
    	HttpHeaders responseHeaders = new HttpHeaders();
    	responseHeaders.add(HttpHeaders.SET_COOKIE, cookieUtil.deleteAccessTokenCookie().toString());
    	responseHeaders.add(HttpHeaders.SET_COOKIE, cookieUtil.deleteRefreshTokenCookie().toString());
		return ResponseEntity.ok().headers(responseHeaders).body("Logout success");
	}

	@Override
    public User getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User customUserDetails = (User) authentication.getPrincipal();

        return userRepository.findByLogin(customUserDetails.getLogin()).orElseThrow(() -> new IllegalArgumentException("User not found with email " + customUserDetails.getLogin()));
    }

    private void addAccessTokenCookie(HttpHeaders httpHeaders, Token token) {
        httpHeaders.add(HttpHeaders.SET_COOKIE, cookieUtil.createAccessTokenCookie(token.getTokenValue(), token.getDuration()).toString());
    }

    private void addRefreshTokenCookie(HttpHeaders httpHeaders, Token token) {
        httpHeaders.add(HttpHeaders.SET_COOKIE, cookieUtil.createRefreshTokenCookie(token.getTokenValue(), token.getDuration()).toString());
    }

	@Override
	public User signup(SignupRequest request) throws CantCreateUserException {
		if (userRepository.existsByLogin(request.getLogin())) {
			throw new CantCreateUserException("Error: Login is already taken!");
		}

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CantCreateUserException("Error: Email is already taken!");
        }
        User newUser = new User(request.getLogin(), request.getEmail(), encoder.encode(request.getPassword()));
        newUser.setEnabled(true);
        newUser.getAuthoritiesSet().add(authorityRepository.findFirstByRole(Role.ROLE_USER));
        newUser = userRepository.save(newUser);
		return newUser;
	}

}
