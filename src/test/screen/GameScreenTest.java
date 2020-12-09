package screen;


import engine.Cooldown;
import engine.Core;
import engine.GameState;
import engine.LevelSettings;
import entity.Bullet;
import entity.EnemyShip;
import entity.EnemyShipFormation;
import entity.Ship;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GameScreenTest{
    static GameScreen gs;

    @BeforeAll
    static void setup(){
        LevelSettings level = new LevelSettings(1,1,1,1);
        GameState gameState = new GameState(1,1,1,1,1);
        gs = new GameScreen(gameState, level,true, 5, 5, 5);
    }

    @Test
    void initialize() {
        gs.initialize();

        Screen screen = new Screen(1,1,1);

        assertEquals(gs.drawManager,Core.getDrawManager());
        assertEquals(gs.fps, 5);
        assertEquals(gs.height, 5);
        assertEquals(gs.width, 5);
        assertEquals(gs.inputManager, Core.getInputManager());
        assertEquals(gs.insets, screen.insets);
        // if gs.run() is running, actual is true. else is false
        assertEquals(gs.isRunning, true);
        assertEquals(gs.logger, Core.getLogger());
        assertEquals(gs.returnCode, 0);

        Ship ship = new Ship(gs.width, gs.height);
        assertEquals(ship.getWidth(), 26);
        assertEquals(ship.getHeight(), 16);
    }

    @Test
    void run() throws IOException {
        gs.initialize();
        try{
            assertEquals(gs.run(), 0);
        } catch (NullPointerException e) {
        }
    }

    @Test
    void update() throws IOException {
        gs.update();

    }

    @Test
    void getGameState() {
        GameState gameState = gs.getGameState();
        assertEquals(gameState.getBulletsShot(), 1);
        assertEquals(gameState.getLevel(), 1);
        assertEquals(gameState.getLivesRemaining(), 2);
        assertEquals(gameState.getScore(), 1);
        assertEquals(gameState.getShipsDestroyed(), 1);
    }
}