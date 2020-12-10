package engine;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class DrawManagerTest {
    Frame frame;
    Frame b;

    @Test
    void getInstance() {
        DrawManager instance = new DrawManager();
        instance.getInstance();
        assertNotNull(instance);

    }

    @Test
    void setFrame() {
        DrawManager sf = new DrawManager();
        sf.setFrame(b);
        assertEquals(frame, b);

    }}
