package subtitle.restapi.exeption;

import org.springframework.http.HttpStatus;
import subtitle.base.exception.SubtitleException;

public class CantCreateUserException extends SubtitleException {
    private static final long serialVersionUID = 1L;
    public CantCreateUserException(String error) {
        super(HttpStatus.BAD_REQUEST, error);
    }
}
