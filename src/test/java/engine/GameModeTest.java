package engine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameModeTest {

    GameMode m = new GameMode();

    @Test
    public void getGameMode() {
        m.setGameMode(1);
        assertEquals(1, m.getGameMode());
    }

    @Test
    public void setGameMode() {
        m.setGameMode(2);
        assertEquals(2, m.getGameMode());
    }
}
