package subtitle.restapi.words;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import subtitle.base.exception.SubtitleException;
import subtitle.base.restapi.AbstractApiService;
import subtitle.restapi.ui.model.WordDescriptionRequest;
import subtitle.users.dictionary.service.UserDictionaryWordService;

@RestController
@RequestMapping("/api/user/dictionary")
public class ApiUserDictionary extends AbstractApiService {

    private final UserDictionaryWordService userDictionaryWordService;

    @Autowired
    public ApiUserDictionary(UserDictionaryWordService userDictionaryWordService) {
        this.userDictionaryWordService = userDictionaryWordService;
    }

    @PutMapping("/{wordId}")
    public Boolean updateDescription(@PathVariable Long wordId, @RequestBody WordDescriptionRequest description){
        try {
            return userDictionaryWordService.updateDescription(wordId, description, getCurrentUser());
        } catch (SubtitleException e) {
            throw getException(e, "Can`t update description");
        }
    }
    @DeleteMapping("/{wordId}")
    public Boolean deleteDescription(@PathVariable Long wordId){
        try {
            return userDictionaryWordService.deleteUserDictionaryWord(wordId, getCurrentUser());
        } catch (SubtitleException e) {
            throw getException(e, "Can`t delete description");
        }
    }
}
