package subtitle.restapi.words;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import subtitle.base.restapi.AbstractApiService;
import subtitle.restapi.ui.model.WordDescriptionRequest;
import subtitle.restapi.ui.model.WordsRequest;
import subtitle.users.words.model.WordEntity;
import subtitle.restapi.ui.model.WordResponse;
import subtitle.restapi.ui.model.WordsPageable;
import subtitle.users.words.service.UserWordsService;

@RestController
@RequestMapping("/api/user/files/words")
public class ApiUserFilesWords extends AbstractApiService {

    private final UserWordsService userWordsService;

    @Autowired
    public ApiUserFilesWords(UserWordsService userWordsService) {
        this.userWordsService = userWordsService;
    }

    @GetMapping("/{fileId}")
    public WordsPageable getAllWords(@PathVariable Long fileId, WordsRequest wordsRequest)
    {
        try {
            Sort.Direction direction = wordsRequest.isSortDesc()? Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(wordsRequest.getPage(), wordsRequest.getSize(), Sort.by(direction, wordsRequest.getSort()));
            return userWordsService.getAllWordsByFileText(fileId, getCurrentUser(), wordsRequest.getKnown(), pageable);
        } catch (Exception e) {
           throw getException(e, "Can`t get words by fileText");
        }
    }

    @PutMapping("/{wordId}/{known}")
    public WordEntity setWordKnown(@PathVariable Long wordId, @PathVariable boolean known) {
        try {
            return userWordsService.setWordKnown(wordId, known, getCurrentUser());
        } catch (Exception e) {
            throw getException(e, "Can`t set word known/unknown");
        }
    }

    @PutMapping("/{wordId}")
    public WordResponse setDescription(@PathVariable Long wordId,
                                       @RequestBody WordDescriptionRequest wordDescription){
        try {
            return userWordsService.setDescription(wordId, wordDescription, getCurrentUser());
        } catch(Exception e) {
            throw getException(e, "Can`t set description to word");
        }
    }
}
