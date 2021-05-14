package subtitle.restapi.upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import subtitle.base.restapi.AbstractApiService;
import subtitle.users.files.model.FileText;
import subtitle.users.files.service.FileTextService;

import java.util.List;

@RestController
@RequestMapping("/api/user/files")
public class ApiUserFiles extends AbstractApiService {

	private final FileTextService fileTextService;

	@Autowired
	public ApiUserFiles(FileTextService fileTextService) {
		this.fileTextService = fileTextService;
	}

	@PostMapping("/")
	public FileText uploadFile(@RequestParam(value = "file") MultipartFile file){
		try {
			return fileTextService.store(file, getCurrentUser());
		} catch (Exception e) {
			throw getException(e, "Can`t store file");
		}
	}
	
	@GetMapping("/")
	public List<FileText> getFiles(){
		try {
			return fileTextService.getFiles(getCurrentUser());
		} catch (Exception e) {
			throw getException(e, "Can`t get files");
		}
	}

	@DeleteMapping("/{id}")
	public String deleteFile(@PathVariable Long id) {
		try {
			return fileTextService.deleteFile(id, getCurrentUser());
		} catch (Exception e) {
			throw getException(e, "Can`t delete file");
		}
	}

}
