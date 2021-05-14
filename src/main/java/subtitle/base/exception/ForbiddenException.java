package subtitle.base.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends SubtitleException{
    private static final long serialVersionUID = 1L;

    public ForbiddenException(String error) {
        super(HttpStatus.FORBIDDEN, error);
    }
}
