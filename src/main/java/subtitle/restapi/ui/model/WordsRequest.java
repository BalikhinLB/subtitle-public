package subtitle.restapi.ui.model;

import lombok.Data;

@Data
public class WordsRequest {
    private int page;
    private int size;
    private Boolean known;
    private String sort;
    boolean sortDesc;
}
