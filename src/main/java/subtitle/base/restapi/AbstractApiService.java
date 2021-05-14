package subtitle.base.restapi;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import subtitle.base.exception.SubtitleException;
import subtitle.restapi.exeption.UserCantDefineException;
import subtitle.security.model.User;
import subtitle.security.service.UserService;

public abstract class AbstractApiService {

    @Getter
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    protected ResponseStatusException getException(Exception e, String msg) {
        if (e instanceof SubtitleException) {
            SubtitleException se = (SubtitleException) e;
            return new ResponseStatusException(se.getStatusRespond(), se.getMessage());
        }
        else {
            e.printStackTrace();
            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
        }

    }

    protected User getCurrentUser() throws UserCantDefineException {
        User user = userService.getUserProfile();
        if (user == null) throw new UserCantDefineException();
        return user;
    }

}
