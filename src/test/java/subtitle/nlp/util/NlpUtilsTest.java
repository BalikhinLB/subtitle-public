package subtitle.nlp.util;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import subtitle.users.files.model.FileData;
import subtitle.users.files.model.FileText;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Blob;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class NlpUtilsTest {
    public static final String SIMPLE_TXT_PATH = "src/test/resources/nlp/example/txt/simple.txt";
    public static final String SUBTITLE_PATH = "src/test/resources/nlp/example/txt/subtitle.srt";

    @Test
    void getStringFromBlobTxt() throws IOException, SQLException {
        Path simpleTxtPath = Path.of(SIMPLE_TXT_PATH);
        String actual = Files.readString(simpleTxtPath);
        Blob textBlob = Mockito.mock(Blob.class);
        Mockito.when(textBlob.length()).thenReturn(10L);
        Mockito.when(textBlob.getBytes(1, 10)).thenReturn(actual.getBytes(StandardCharsets.UTF_8));
        assertEquals(actual, NlpUtils.getStringFromBlob("", textBlob));
    }
    @Test
    void getStringFromSrtTxt() throws IOException, SQLException {
        //TODO fix this test
        Path simpleTxtPath = Path.of(SUBTITLE_PATH);
        String actual = Files.readString(simpleTxtPath);
        Blob textBlob = Mockito.mock(Blob.class);
        Mockito.when(textBlob.length()).thenReturn(10L);
        Mockito.when(textBlob.getBytes(1, 10)).thenReturn(actual.getBytes(StandardCharsets.UTF_8));
        assertEquals(actual, NlpUtils.getStringFromBlob("application/x-subrip", textBlob));
    }
}