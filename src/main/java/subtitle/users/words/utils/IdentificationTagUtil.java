package subtitle.users.words.utils;

import lombok.extern.java.Log;
import subtitle.users.words.model.Tags;

@Log
public class IdentificationTagUtil {
    private IdentificationTagUtil(){
        throw new IllegalStateException("Utility class");
    }

    public static Tags getTag(String tagString) {
        if (tagString.matches("^[^A-Za-z]*$")) return Tags.SYM;
        Tags tag;
        try {
            tag = Tags.valueOf(tagString);
        } catch (IllegalArgumentException e) {
            String msg = String.format("Unknown tag:%s", tagString);
            log.warning(msg);
            tag = Tags.UNKNOWN;
        }
        return tag;
    }
}
