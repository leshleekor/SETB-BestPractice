package engine;

import screen.*;
import screen.ScreenCode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

/**
 * Implements core game logic.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public final class Core {

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

	/**
	 * Test implementation.
	 *
	 * @param args
	 *            Program args, ignored.
	 */
	public static void main(final String[] args) throws IOException {
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

		frame = new Frame(WIDTH, HEIGHT);
		DrawManager.getInstance().setFrame(frame);
		int width = frame.getWidth();
		int height = frame.getHeight();

		GameDifficulty.setDifficulty(GameDifficulty.EASY);
		GameMode.setGameMode(GameMode.P1);

		GameState gameState;
		GameState gameState2;
		int returnCode = ScreenCode.MAIN;
		do {
			gameState2 = new GameState(1, 0, MAX_P2_LIVES, 0, 0);
			gameState = new GameState(1, 0, MAX_P1_LIVES, 0, 0);

			switch (returnCode) {
			case ScreenCode.MAIN:
				// Main menu.
				currentScreen = new TitleScreen(width, height, FPS);
				LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
						+ " title screen at " + FPS + " fps.");
				returnCode = frame.setScreen(currentScreen);
				LOGGER.info("Closing title screen.");
				break;
			case ScreenCode.PLAY:
				do {
					// One extra live every few levels.
					boolean bonusLife = gameState.getLevel()
							% EXTRA_LIFE_FRECUENCY == 0
							&& gameState.getLivesRemaining() < MAX_P1_LIVES;

					boolean bonusLife2 = gameState2.getLevel()
							% EXTRA_LIFE_FRECUENCY == 0
							&& gameState2.getLivesRemaining() < MAX_P2_LIVES;

					currentScreen = new GameScreen(gameState, gameState2,
							levelSettings.get(gameState.getLevel() - 1),
							bonusLife, width, height, FPS);
					LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
							+ " game screen at " + FPS + " fps.");
					frame.setScreen(currentScreen);
					LOGGER.info("Closing game screen.");

					gameState = ((GameScreen) currentScreen).getGameState(1);
					gameState2 = ((GameScreen) currentScreen).getGameState(2);

					gameState = new GameState(gameState.getLevel() + 1,
							gameState.getScore(),
							gameState.getLivesRemaining(),
							gameState.getBulletsShot(),
							gameState.getShipsDestroyed());

					gameState2 = new GameState(gameState2.getLevel() + 1,
							gameState2.getScore(),
							gameState2.getLivesRemaining(),
							gameState2.getBulletsShot(),
							gameState2.getShipsDestroyed());

				}while ((gameState.getLivesRemaining() > 0) || ((GameMode.getGameMode() == GameMode.P2) && gameState2.getLivesRemaining() > 0)
						&& gameState.getLevel() <= NUM_LEVELS);
				if(GameMode.getGameMode() == GameMode.P1) {
					LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
							+ " score screen at " + FPS + " fps, with a score of "
							+ gameState.getScore() + ", "
							+ gameState.getLivesRemaining() + " lives remaining, "
							+ gameState.getBulletsShot() + " bullets shot and "
							+ gameState.getShipsDestroyed() + " ships destroyed.");
					currentScreen = new ScoreScreen(width, height, FPS, gameState);
					returnCode = frame.setScreen(currentScreen);
					LOGGER.info("Closing score screen.");
				}
				else if(GameMode.getGameMode() == GameMode.P2) {
					LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
							+ " score screen at " + FPS + " fps, with a score of "
							+ gameState.getScore() + ", "
							+ gameState.getLivesRemaining() + " lives remaining, "
							+ gameState.getBulletsShot() + " bullets shot and "
							+ gameState.getShipsDestroyed() + " ships destroyed."
							+ gameState2.getScore() + ", "
							+ gameState2.getLivesRemaining() + " lives remaining, "
							+ gameState2.getBulletsShot() + " bullets shot and "
							+ gameState2.getShipsDestroyed() + " ships destroyed.");
					currentScreen = new ScoreScreen2(width, height, FPS, gameState, gameState2);
					returnCode = frame.setScreen(currentScreen);
					LOGGER.info("Closing score screen.");

				}


				break;
			case ScreenCode.HIGH_SCORES:
				// High scores.
				currentScreen = new HighScoreScreen(width, height, FPS);
				LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
						+ " high score screen at " + FPS + " fps.");
				returnCode = frame.setScreen(currentScreen);
				LOGGER.info("Closing high score screen.");
				break;
			case ScreenCode.DIFFICULTY:
				// Difficulty Setting Screen.
				currentScreen = new DifficultyScreen(width, height, FPS);
				LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
						+ " difficulty setting screen at " + FPS + " fps.");
				returnCode = frame.setScreen(currentScreen);
				LOGGER.info("Closing difficulty setting screen.");
				break;
			case ScreenCode.PLAYERMODE:
				currentScreen = new ModeScreen(width, height, FPS);
				LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
						+ " mode setting screen at " + FPS + " fps.");
				returnCode = frame.setScreen(currentScreen);
				LOGGER.info("Closing Mode setting screen.");
				break;
			default:
				break;
			}

		} while (returnCode != ScreenCode.EXIT);

		fileHandler.flush();
		fileHandler.close();
		System.exit(0);
	}

	/**
	 * Constructor, not called.
	 */
	private Core() {

	}

	/**
	 * Controls access to the logger.
	 *
	 * @return Application logger.
	 */
	public static Logger getLogger() {
		return LOGGER;
	}

	/**
	 * Controls access to the drawing manager.
	 *
	 * @return Application draw manager.
	 */
	public static DrawManager getDrawManager() {
		return DrawManager.getInstance();
	}

	/**
	 * Controls access to the input manager.
	 *
	 * @return Application input manager.
	 */
	public static InputManager getInputManager() {
		return InputManager.getInstance();
	}

	/**
	 * Controls access to the file manager.
	 *
	 * @return Application file manager.
	 */
	public static FileManager getFileManager() {
		return FileManager.getInstance();
	}

	/**
	 * Controls creation of new cooldowns.
	 *
	 * @param milliseconds
	 *            Duration of the cooldown.
	 * @return A new cooldown.
	 */
	public static Cooldown getCooldown(final int milliseconds) {
		return new Cooldown(milliseconds);
	}

	/**
	 * Controls creation of new cooldowns with variance.
	 *
	 * @param milliseconds
	 *            Duration of the cooldown.
	 * @param variance
	 *            Variation in the cooldown duration.
	 * @return A new cooldown with variance.
	 */
	public static Cooldown getVariableCooldown(final int milliseconds,
                                               final int variance) {
		return new Cooldown(milliseconds, variance);
	}
	public static void setLevelSettings(ArrayList<LevelSettings> levelSettingsArrayList){
		levelSettings = levelSettingsArrayList;
	}
	public static void setModeSettings(int modeSettings){
		ModeSettings = modeSettings;
	}
}
