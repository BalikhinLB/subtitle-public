package subtitle.users.files.exeption;

import org.springframework.http.HttpStatus;

import subtitle.base.exception.SubtitleException;

public class NotSupportedFormatException extends SubtitleException {

	private static final long serialVersionUID = 1L;

	public NotSupportedFormatException(String error) {
		super(HttpStatus.UNSUPPORTED_MEDIA_TYPE ,error);
		
	}

}
