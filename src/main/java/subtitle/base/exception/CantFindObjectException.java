package subtitle.base.exception;

import org.springframework.http.HttpStatus;

public class CantFindObjectException extends SubtitleException{
	private static final long serialVersionUID = 1L;

	public CantFindObjectException(String error) {
        super(HttpStatus.NO_CONTENT, error);
    }
}
