package subtitle.restapi.exeption;

import org.springframework.http.HttpStatus;
import subtitle.base.exception.SubtitleException;

public class UserCantDefineException extends SubtitleException {
    private static final long serialVersionUID = 1L;
    public UserCantDefineException() {
        super(HttpStatus.UNAUTHORIZED, "Can`t define user");
    }
}
