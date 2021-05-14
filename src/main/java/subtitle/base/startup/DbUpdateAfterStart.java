package subtitle.base.startup;

public interface DbUpdateAfterStart {
    void dbUpdate();
    boolean isRepeatable();
    String getName();
}
