package subtitle.base.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.Getter;

public class SubtitleException extends Exception{
	private static final long serialVersionUID = 1L;
	
	@Getter
	private final HttpStatus statusRespond;
	
	public SubtitleException(String error) {
		super(error);
		this.statusRespond = HttpStatus.INTERNAL_SERVER_ERROR;
	}
	
	public SubtitleException(HttpStatus statusRespond, String error) {
		super(error);
		this.statusRespond = statusRespond;
	}
	
	public ResponseEntity<String> getResponse() {
		return ResponseEntity.status(getStatusRespond()).body(getMessage());
	}
}
