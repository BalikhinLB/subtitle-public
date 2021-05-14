package subtitle.users.dictionary.service;

import subtitle.base.exception.SubtitleException;
import subtitle.restapi.ui.model.WordDescriptionRequest;
import subtitle.security.model.User;

public interface UserDictionaryWordService {
    boolean deleteUserDictionaryWord(Long wordId, User user) throws SubtitleException;
    boolean updateDescription(Long wordId, WordDescriptionRequest description, User user) throws SubtitleException;
}
