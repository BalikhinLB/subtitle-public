package subtitle.nlp.service;

import subtitle.users.files.model.FileText;

public interface TransactionalSaveErrorToFileTextService {
    void setError(FileText fileText);
}
