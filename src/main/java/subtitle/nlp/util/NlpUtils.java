package subtitle.nlp.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NlpUtils {
    private static final Set<String> SUBTITLES_TYPE = Stream.of("application/x-subrip", "application/octet-stream")
            .collect(Collectors.toCollection(HashSet::new));

    private NlpUtils() {
    }

    public static String getStringFromBlob(String type, Blob file) throws SQLException, IOException {
        String textString;
        if (SUBTITLES_TYPE.contains(type)) {
            textString = getStringFromSbr(file);
        } else {
            textString = new String(file.getBytes(1, (int) file.length()));
        }
        return textString;
    }

    private static String getStringFromSbr(Blob file) throws SQLException, IOException {
        String textString;
        String s;
        InputStream inStream = file.getBinaryStream();
        InputStreamReader inStreamReader = new InputStreamReader(inStream);
        BufferedReader reader = new BufferedReader(inStreamReader);
        StringBuilder buf = new StringBuilder();
        String bufPre = "";
        while ((s = reader.readLine()) != null) {
            if (org.springframework.util.StringUtils.isEmpty(s) || s.matches("^([^a-zA-Z]*)$"))
                continue;
            if (s.matches("^.{3}(.)*") && bufPre.matches("(.)*[.]{3}$")) {
                buf.delete(buf.length() - 3, buf.length()).append(" ");
                s = s.substring(3);
            }
            buf.append(" ").append(s);
            bufPre = s;
        }
        textString = buf.toString();
        return textString;
    }
}
