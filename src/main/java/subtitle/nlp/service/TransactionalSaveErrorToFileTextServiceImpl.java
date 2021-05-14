package subtitle.nlp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import subtitle.users.files.model.FileText;
import subtitle.users.files.repository.FileTextRepository;

@Service
public class TransactionalSaveErrorToFileTextServiceImpl implements TransactionalSaveErrorToFileTextService {


    FileTextRepository fileTextRepository;

    @Autowired
    public TransactionalSaveErrorToFileTextServiceImpl(FileTextRepository fileTextRepository) {
        this.fileTextRepository = fileTextRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void setError(FileText fileText) {
        fileText = fileTextRepository.getOne(fileText.getId());
        fileText.setState(FileText.States.ERROR);
    }
}
