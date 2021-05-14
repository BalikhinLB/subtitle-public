package subtitle.users.files.service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.java.Log;
import subtitle.base.exception.CantFindObjectException;
import subtitle.base.exception.ForbiddenException;
import subtitle.base.exception.SubtitleException;
import subtitle.users.files.exeption.NotSupportedFormatException;
import subtitle.users.files.model.FileText;
import subtitle.users.files.repository.FileTextRepository;
import subtitle.nlp.service.NlpParsingService;
import subtitle.security.model.User;

@Log
@Service
public class FileTextServiceImpl implements FileTextService {

	private static final Set<String> AVAILABLE_TXT_TYPES = Set.of("application/x-subrip","application/octet-stream","text/plain");

	@Autowired
	private FileTextRepository fileTextRepository;
	@Autowired
	private ApplicationContext applicationContext;

	private NlpParsingService getNlpService(){
		return applicationContext.getBean(NlpParsingService.class);
	}

	@Override
	public FileText store(@NonNull MultipartFile file, User user) throws SubtitleException {

		String type = file.getContentType();
		if (!AVAILABLE_TXT_TYPES.contains(type)) {
			String error = String.format("User (id:%s) uploaded file with unsupported format (%s)", user.getId(), type);
			log.log(Level.WARNING, error);
			throw new NotSupportedFormatException(error);
		}

		String msg = String.format("File %s:%s was loaded by %s",
				file.getOriginalFilename(), type, user.getEmail());
		log.info(msg);

		FileText fileText = new FileText(file, user);
		fileTextRepository.persist(fileText);

		getNlpService().parsingFile(fileText);

		return fileText;

	}

	@Override
	public List<FileText> getFiles(User user) {
		return fileTextRepository.findAllByUser(user);
	}

	@Override
	@Transactional
	public String deleteFile(Long id, User user) throws SubtitleException {
		FileText fileText = getFile(id, user);
		if (Objects.equals(fileText.getState(), FileText.States.LOADED)){
			throw new ForbiddenException(String.format("Can`t delete FileText (id:%s) in state: LOADED", id));
		}
		fileTextRepository.delete(fileText);
		return String.format("FileText (id:%s) was deleted successfully", id);
	}

	@Override
	public FileText getFile(Long id, User user) throws SubtitleException {
		FileText fileText = fileTextRepository.findById(id)
				.orElseThrow(() -> new CantFindObjectException(String.format("Can`t find FileText (id:%s)",id)));
		if (!fileText.getUser().equals(user)) {
			throw new ForbiddenException(String.format("FileText (id:%s) is not user`s (id:%s)", id, user.getId()));
		}
		return fileText;
	}
}
