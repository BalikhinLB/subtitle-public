package subtitle.users.files.repository;

import subtitle.base.exception.SubtitleException;
import subtitle.users.files.model.FileText;

public interface FileTextRepositoryExt {
	
	FileText persist(FileText fileText) throws SubtitleException;
	void refresh(FileText fileText);

}
