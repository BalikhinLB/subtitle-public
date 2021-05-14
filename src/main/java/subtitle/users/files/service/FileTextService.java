package subtitle.users.files.service;

import org.springframework.web.multipart.MultipartFile;

import subtitle.base.exception.SubtitleException;
import subtitle.users.files.model.FileText;
import subtitle.security.model.User;

import java.util.List;

public interface FileTextService {
	
	FileText store(MultipartFile file, User user) throws SubtitleException;
	List<FileText> getFiles(User user) throws SubtitleException;
	String deleteFile(Long id, User user) throws SubtitleException;

	FileText getFile(Long id, User user) throws SubtitleException;
}
