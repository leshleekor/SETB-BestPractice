package engine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {

    GameState s = new GameState(1, 0, 3, 0, 0);

    @Test
    void getLevel() {
        assertEquals(1, s.getLevel());
    }

    @Test
    void getScore() {
        assertEquals(0, s.getScore());
    }

    @Test
    void getLivesRemaining() {
        assertEquals(3, s.getLivesRemaining());
    }

    @Test
    void getBulletsShot() {
        assertEquals(0, s.getBulletsShot());
    }

    @Test
    void getShipsDestroyed() {
        assertEquals(0, s.getShipsDestroyed());
    }
}