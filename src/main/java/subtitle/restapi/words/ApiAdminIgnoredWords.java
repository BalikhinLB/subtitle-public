package subtitle.restapi.words;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subtitle.base.restapi.AbstractApiService;
import subtitle.dictionary.service.IgnoredWordsService;

@Log
@RestController
@RequestMapping("/api/admin/words")
public class ApiAdminIgnoredWords extends AbstractApiService {


    private final IgnoredWordsService ignoredWordsService;

    @Autowired
    public ApiAdminIgnoredWords(IgnoredWordsService ignoredWordsService) {
        this.ignoredWordsService = ignoredWordsService;
    }

    @PostMapping("/toignored/{wordId}")
    public Boolean putToIgnored(@PathVariable Long wordId) {
        try {
            return ignoredWordsService.putWordToIgnored(wordId);
        } catch (Exception e) {
            throw getException(e, "Can`t set ignored");
        }
    }
}