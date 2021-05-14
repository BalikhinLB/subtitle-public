package subtitle.users.files.repository;

import java.io.IOException;
import java.sql.Blob;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.java.Log;
import subtitle.users.files.exeption.SaveToDbException;
import subtitle.users.files.model.FileText;

@Log
public class FileTextRepositoryExtImpl implements FileTextRepositoryExt {
	
	@PersistenceContext
	 private EntityManager em;

	@Override
	@Transactional
	public FileText persist(FileText fileText) throws SaveToDbException {
		Session session = em.unwrap(Session.class);
		Blob blob;
		try {
			blob = session.getLobHelper().createBlob(fileText.getFileData().getFile().getBytes());
		} catch (IOException e) {
			log.warning(e.getMessage());
			throw new SaveToDbException("Can`t get Blob from byte[]");
		}
		
		fileText.getFileData().setData(blob);
		em.persist(fileText);
		em.flush();
		fileText.getFileData().setFile(null);
		return fileText;
	}
	
	  @Override
	  @Transactional
	  public void refresh(FileText fileText) {
	    em.refresh(fileText);
	  }



}
