package subtitle.restapi.exeption;

import org.springframework.http.HttpStatus;
import subtitle.base.exception.SubtitleException;

public class BadDataException extends SubtitleException {
    private static final long serialVersionUID = 1L;
    public BadDataException(String error) {
        super(HttpStatus.BAD_REQUEST, error);
    }
}
