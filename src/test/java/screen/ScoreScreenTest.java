package screen;

import engine.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;


import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static screen.ScreenCode.*;

class ScoreScreenTest {
    /** Width of current screen. */
    private static final int WIDTH = 500;
    /** Height of current screen. */
    private static final int HEIGHT = 680;
    /** Max fps of current screen. */
    private static final int FPS = 60;

    /** Max lives. */
    private static final int MAX_P1_LIVES = 3;
    private static final int MAX_P2_LIVES = 3;

    /** Levels between extra life. */
    private static final int EXTRA_LIFE_FRECUENCY = 3;
    /** Total number of levels. */
    private static final int NUM_LEVELS = 7;
    /** Total number of Difficulties. */
    private static final int NUM_DIFFICULTIES = 3;



    /** Frame to draw the screen on. */
    private static Frame frame;
    /** Screen currently shown. */
    private static Screen currentScreen;
    /** Difficulty settings list. */
    private static List<LevelSettings> levelSettings;
    /** Mode settings (1p/2p)*/
    private static int ModeSettings;
    /** Application logger. */
    private static final Logger LOGGER = Logger.getLogger(Core.class
            .getSimpleName());
    /** Logger handler for printing to disk. */
    private static Handler fileHandler;
    /** Logger handler for printing to console. */
    private static ConsoleHandler consoleHandler;
    private DrawManager drawManager;


    @Test
    void scoreScreen_test() throws IOException {
        try {
            LOGGER.setUseParentHandlers(false);

            fileHandler = new FileHandler("log");
            fileHandler.setFormatter(new MinimalFormatter());

            consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new MinimalFormatter());

            LOGGER.addHandler(fileHandler);
            LOGGER.addHandler(consoleHandler);
            LOGGER.setLevel(Level.ALL);

        } catch (Exception e) {
            // TODO handle exception
            e.printStackTrace();
        }
        drawManager = Core.getDrawManager();
        frame = new Frame(WIDTH, HEIGHT);
        int width = frame.getWidth();
        int height = frame.getHeight();
        drawManager.setFrame(frame);

        GameDifficulty.setDifficulty(GameDifficulty.EASY);
        GameMode.setGameMode(GameMode.P1);

        GameState gameState=new GameState(1, 0, MAX_P1_LIVES, 0, 0);;
        GameState gameState2;
        gameState = new GameState(gameState.getLevel() + 1,
                30,
                0,
                3,
                4);

        currentScreen = new ScoreScreen(width, height, FPS, gameState);

        int returnCode = frame.setScreen(currentScreen);
        assertEquals(returnCode,MAIN);
    }

}


