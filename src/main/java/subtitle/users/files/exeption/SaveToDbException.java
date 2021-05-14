package subtitle.users.files.exeption;

import subtitle.base.exception.SubtitleException;

public class SaveToDbException extends SubtitleException {

	private static final long serialVersionUID = 1L;

	public SaveToDbException(String error) {
		super(error);
		
	}

}
