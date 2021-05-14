package subtitle.base.startup;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import subtitle.base.startup.repository.DbUpdateRepository;

import java.util.List;

class ApplicationStartupTest {

    private final String REPEATABLE_NAME = "REPEATABLE_NAME";
    private final String NOT_REPEATABLE_NAME = "NOT_REPEATABLE_NAME";

    private ApplicationStartup startup;

    @Mock
    DbUpdateRepository dbUpdateRepository;
    @Mock
    DbUpdateAfterStart repeatable;
    @Mock
    DbUpdateAfterStart notRepeatable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        List<DbUpdateAfterStart> listDbUpdates = initDbUpdateAfterStarts();
        startup = new ApplicationStartup(listDbUpdates, dbUpdateRepository);
    }


    private List<DbUpdateAfterStart> initDbUpdateAfterStarts() {
        Mockito.when(repeatable.isRepeatable()).thenReturn(true);
        Mockito.when(repeatable.getName()).thenReturn(REPEATABLE_NAME);
        Mockito.when(notRepeatable.isRepeatable()).thenReturn(false);
        Mockito.when(notRepeatable.getName()).thenReturn(NOT_REPEATABLE_NAME);
        return List.of(repeatable, notRepeatable);
    }

    @Test
    void onApplicationEvent_1repeatable() {
        Mockito.when(dbUpdateRepository.existsByName(REPEATABLE_NAME)).thenReturn(true);
        Mockito.when(dbUpdateRepository.existsByName(NOT_REPEATABLE_NAME)).thenReturn(true);

        startup.onApplicationEvent(Mockito.mock(ApplicationReadyEvent.class));

        Mockito.verify(repeatable, Mockito.times(1)).dbUpdate();
        Mockito.verify(notRepeatable, Mockito.times(0)).dbUpdate();
    }
    @Test
    void onApplicationEvent_1repeatable_1notRepeatable() {
        Mockito.when(dbUpdateRepository.existsByName(REPEATABLE_NAME)).thenReturn(false);
        Mockito.when(dbUpdateRepository.existsByName(NOT_REPEATABLE_NAME)).thenReturn(false);

        startup.onApplicationEvent(Mockito.mock(ApplicationReadyEvent.class));

        Mockito.verify(repeatable, Mockito.times(1)).dbUpdate();
        Mockito.verify(notRepeatable, Mockito.times(1)).dbUpdate();
    }
}