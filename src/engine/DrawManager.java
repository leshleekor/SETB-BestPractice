package engine;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import screen.GameScreen;
import screen.DifficultyScreen;
import screen.ModeScreen;
import screen.ScreenCode;
import screen.Screen;
import entity.Entity;
import entity.Ship;

/**
 * Manages screen drawing.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public final class DrawManager {

	/** Singleton instance of the class. */
	private static DrawManager instance;
	/** Current frame. */
	private static Frame frame;
	/** FileManager instance. */
	private static FileManager fileManager;
	/** Application logger. */
	private static Logger logger;
	/** Graphics context. */
	private static Graphics graphics;
	/** Buffer Graphics. */
	private static Graphics backBufferGraphics;
	/** Buffer image. */
	private static BufferedImage backBuffer;
	/** Normal sized font. */
	private static Font fontRegular;
	/** Normal sized font properties. */
	private static FontMetrics fontRegularMetrics;
	/** Big sized font. */
	private static Font fontBig;
	/** Big sized font properties. */
	private static FontMetrics fontBigMetrics;

	/** Sprite types mapped to their images. */
	private static Map<SpriteType, boolean[][]> spriteMap;

	/** Sprite types. */
	public static enum SpriteType {
		/** Player ship. */
		Ship,
		/** Destroyed player ship. */
		ShipDestroyed,
		/** Player bullet. */
		Bullet,
		/** Enemy bullet. */
		EnemyBullet,
		/** First enemy ship - first form. */
		EnemyShipA1,
		/** First enemy ship - second form. */
		EnemyShipA2,
		/** Second enemy ship - first form. */
		EnemyShipB1,
		/** Second enemy ship - second form. */
		EnemyShipB2,
		/** Third enemy ship - first form. */
		EnemyShipC1,
		/** Third enemy ship - second form. */
		EnemyShipC2,
		/** Bonus ship. */
		EnemyShipSpecial,
		/** Destroyed enemy ship. */
		Explosion
	};

	/**
	 * Private constructor.
	 */
	private DrawManager() {
		fileManager = Core.getFileManager();
		logger = Core.getLogger();
		logger.info("Started loading resources.");

		try {
			spriteMap = new LinkedHashMap<SpriteType, boolean[][]>();

			spriteMap.put(SpriteType.Ship, new boolean[13][8]);
			spriteMap.put(SpriteType.ShipDestroyed, new boolean[13][8]);
			spriteMap.put(SpriteType.Bullet, new boolean[3][5]);
			spriteMap.put(SpriteType.EnemyBullet, new boolean[3][5]);
			spriteMap.put(SpriteType.EnemyShipA1, new boolean[12][8]);
			spriteMap.put(SpriteType.EnemyShipA2, new boolean[12][8]);
			spriteMap.put(SpriteType.EnemyShipB1, new boolean[12][8]);
			spriteMap.put(SpriteType.EnemyShipB2, new boolean[12][8]);
			spriteMap.put(SpriteType.EnemyShipC1, new boolean[12][8]);
			spriteMap.put(SpriteType.EnemyShipC2, new boolean[12][8]);
			spriteMap.put(SpriteType.EnemyShipSpecial, new boolean[16][7]);
			spriteMap.put(SpriteType.Explosion, new boolean[13][7]);

			fileManager.loadSprite(spriteMap);
			logger.info("Finished loading the sprites.");

			// Font loading.
			fontRegular = fileManager.loadFont(14f);
			fontBig = fileManager.loadFont(24f);
			logger.info("Finished loading the fonts.");

		} catch (IOException e) {
			logger.warning("Loading failed.");
		} catch (FontFormatException e) {
			logger.warning("Font formating failed.");
		}
	}

	/**
	 * Returns shared instance of DrawManager.
	 *
	 * @return Shared instance of DrawManager.
	 */
	protected static DrawManager getInstance() {
		if (instance == null)
			instance = new DrawManager();
		return instance;
	}

	/**
	 * Sets the frame to draw the image on.
	 *
	 * @param currentFrame
	 *            Frame to draw on.
	 */
	public void setFrame(final Frame currentFrame) {
		frame = currentFrame;
	}

	/**
	 * First part of the drawing process. Initialices buffers, draws the
	 * background and prepares the images.
	 *
	 * @param screen
	 *            Screen to draw in.
	 */
	public void initDrawing(final Screen screen) {
		backBuffer = new BufferedImage(screen.getWidth(), screen.getHeight(),
				BufferedImage.TYPE_INT_RGB);

		graphics = frame.getGraphics();
		backBufferGraphics = backBuffer.getGraphics();

		backBufferGraphics.setColor(Color.BLACK);
		backBufferGraphics
				.fillRect(0, 0, screen.getWidth(), screen.getHeight());

		fontRegularMetrics = backBufferGraphics.getFontMetrics(fontRegular);
		fontBigMetrics = backBufferGraphics.getFontMetrics(fontBig);

		// drawBorders(screen);
		// drawGrid(screen);
	}

	/**
	 * Draws the completed drawing on screen.
	 *
	 * @param screen
	 *            Screen to draw on.
	 */
	public void completeDrawing(final Screen screen) {
		graphics.drawImage(backBuffer, frame.getInsets().left,
				frame.getInsets().top, frame);
	}

	/**
	 * Draws an entity, using the apropiate image.
	 *
	 * @param entity
	 *            Entity to be drawn.
	 * @param positionX
	 *            Coordinates for the left side of the image.
	 * @param positionY
	 *            Coordinates for the upper side of the image.
	 */
	public void drawEntity(final Entity entity, final int positionX,
			final int positionY) {
		boolean[][] image = spriteMap.get(entity.getSpriteType());

		backBufferGraphics.setColor(entity.getColor());
		for (int i = 0; i < image.length; i++)
			for (int j = 0; j < image[i].length; j++)
				if (image[i][j])
					backBufferGraphics.drawRect(positionX + i * 2, positionY
							+ j * 2, 1, 1);
	}

	/**
	 * For debugging purpouses, draws the canvas borders.
	 *
	 * @param screen
	 *            Screen to draw in.
	 */
	@SuppressWarnings("unused")
	private void drawBorders(final Screen screen) {
		backBufferGraphics.setColor(Color.GREEN);
		backBufferGraphics.drawLine(0, 0, screen.getWidth() - 1, 0);
		backBufferGraphics.drawLine(0, 0, 0, screen.getHeight() - 1);
		backBufferGraphics.drawLine(screen.getWidth() - 1, 0,
				screen.getWidth() - 1, screen.getHeight() - 1);
		backBufferGraphics.drawLine(0, screen.getHeight() - 1,
				screen.getWidth() - 1, screen.getHeight() - 1);
	}

	/**
	 * For debugging purpouses, draws a grid over the canvas.
	 *
	 * @param screen
	 *            Screen to draw in.
	 */
	@SuppressWarnings("unused")
	private void drawGrid(final Screen screen) {
		backBufferGraphics.setColor(Color.DARK_GRAY);
		for (int i = 0; i < screen.getHeight() - 1; i += 2)
			backBufferGraphics.drawLine(0, i, screen.getWidth() - 1, i);
		for (int j = 0; j < screen.getWidth() - 1; j += 2)
			backBufferGraphics.drawLine(j, 0, j, screen.getHeight() - 1);
	}

	/**
	 * Draws current score on screen.
	 *
	 * @param screen
	 *            Screen to draw on.
	 * @param score
	 *            Current score.
	 * @param ppos
	 *            player position. ex. player1, player2
	 */
	public void drawScore(final Screen screen, final int score, int ppos) {
		backBufferGraphics.setFont(fontRegular);
		backBufferGraphics.setColor(Color.WHITE);
		String scoreString = String.format("%04d", score);
//		backBufferGraphics.drawString(scoreString, screen.getWidth() - 60, 25);
		backBufferGraphics.drawString(scoreString, screen.getWidth()/2*(ppos-1) + 20, 60);
	}

	/**
	 * Draws number of remaining lives on screen.
	 *
	 * @param screen
	 *            Screen to draw on.
	 * @param lives
	 *            Current lives.
	 * @param color
	 *            Ship's Color
	 * @param ppos
	 *            player position. ex. player1, player2
	 */
	public void drawLives(final Screen screen, final int lives, Color color, int ppos) {
		backBufferGraphics.setFont(fontRegular);
		backBufferGraphics.setColor(Color.WHITE);
		backBufferGraphics.drawString(Integer.toString(lives), screen.getWidth()/2*(ppos-1) + 20, 25);

		Ship dummyShip = new Ship(0, 0, color, 0);
		for (int i = 0; i < lives; i++)
			drawEntity(dummyShip, screen.getWidth()/2*(ppos-1) + 40 + 35 * i, 10);
	}

	/**
	 * Draws a thick line from side to side of the screen.
	 *
	 * @param screen
	 *            Screen to draw on.
	 * @param positionY
	 *            Y coordinate of the line.
	 */
	public void drawHorizontalLine(final Screen screen, final int positionY) {
		backBufferGraphics.setColor(Color.GREEN);
		backBufferGraphics.drawLine(0, positionY, screen.getWidth(), positionY);
		backBufferGraphics.drawLine(0, positionY + 1, screen.getWidth(),
				positionY + 1);
	}

	/**
	 * Draws game title.
	 *
	 * @param screen
	 *            Screen to draw on.
	 * @param title
	 * 			  Title to draw on.
	 */
	public void drawTitle(final Screen screen, String title, String instructions) {
		String titleString = title;
		String instructionsString = instructions;
		String difficultyString = "Difficulty: ";
		String modeString = "Mode: ";

		switch (GameDifficulty.getDiffculty()) {
			case GameDifficulty.EASY:
				difficultyString = difficultyString + "Easy";
				break;
			case GameDifficulty.NORMAL:
				difficultyString = difficultyString + "Normal";
				break;
			case GameDifficulty.HARD:
				difficultyString = difficultyString + "Hard";
				break;
			default:
				break;
		}
		switch (GameMode.getGameMode()){
			case GameMode.P1:
				modeString += "1 Player";
				break;
			case GameMode.P2:
				modeString += "2 player";
				break;
			default:
				break;
		}
		backBufferGraphics.setColor(Color.GRAY);
		if(instructions=="Press Space to return"){
			drawCenteredRegularString(screen, instructionsString,
					screen.getHeight() / 5 );
		}
		else{
			drawCenteredRegularString(screen, instructionsString,
					screen.getHeight() / 2 );
		}
		backBufferGraphics.setColor(Color.YELLOW);
		drawCenteredRegularString(screen, difficultyString,
				screen.getHeight() / 3 );
		backBufferGraphics.setColor(Color.GREEN);
		drawCenteredBigString(screen, titleString, screen.getHeight() / 6);
		if(titleString.equals("High Scores")){
			backBufferGraphics.setColor(Color.GRAY);
			drawCenteredRegularString(screen, "Press Shift to Delete all recode",
					screen.getHeight()*3 / 4 );
		}
		backBufferGraphics.setColor(Color.ORANGE);
		drawCenteredRegularString(screen, modeString,
				screen.getHeight() / 4 );

	}



	/**
	 * Draws main menu.
	 *
	 * @param screen
	 *            Screen to draw on.
	 * @param option
	 *            Option selected.
	 */
	public void drawMenu(final Screen screen, final int option) {
		String playString = "Play";
		String modeString = "Player Mode";
		String difficultyString = "Difficulty";
		String highScoresString = "High scores";
		String exitString = "Exit";

		if (option == ScreenCode.PLAY)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, playString,
				screen.getHeight() / 3 * 2);
		if (option == ScreenCode.PLAYERMODE)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, modeString, screen.getHeight()
				/ 3 * 2 + fontRegularMetrics.getHeight() * 2);
		if (option == ScreenCode.DIFFICULTY)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, difficultyString, screen.getHeight()
				/ 3 * 2 + fontRegularMetrics.getHeight() * 4);
		if (option == ScreenCode.HIGH_SCORES)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, highScoresString, screen.getHeight()
				/ 3 * 2 + fontRegularMetrics.getHeight() * 6);
		if (option == ScreenCode.EXIT)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, exitString, screen.getHeight() / 3
				* 2 + fontRegularMetrics.getHeight() * 8);
	}

	/**
	 * Draws main menu.
	 *
	 * @param screen
	 *            Screen to draw on.
	 * @param option
	 *            Option selected.
	 */
	public void drawDifficultyMenu(final Screen screen, final int option) {
		String easyString = "Easy";
		String normalString = "Normal";
		String hardString = "Hard";
		String backString = "Back to Main";

		if (option == DifficultyScreen.MENU_EASY)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, easyString,
				screen.getHeight() / 3 * 2);
		if (option == DifficultyScreen.MENU_NORMAL)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, normalString, screen.getHeight()
				/ 3 * 2 + fontRegularMetrics.getHeight() * 2);
		if (option == DifficultyScreen.MENU_HARD)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, hardString, screen.getHeight() / 3
				* 2 + fontRegularMetrics.getHeight() * 4);
		if (option == DifficultyScreen.MENU_BACK)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, backString, screen.getHeight() / 3
				* 2 + fontRegularMetrics.getHeight() * 6);
	}

	public void drawModeMenu(final Screen screen, final int option) {
		String easyString = "1 Player";
		String normalString = "2 Player";
		String backString = "Back to Main";

		if (option == ModeScreen.P1)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, easyString,
				screen.getHeight() / 3 * 2);
		if (option == ModeScreen.P2)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, normalString, screen.getHeight()
				/ 3 * 2 + fontRegularMetrics.getHeight() * 2);
		if (option == ModeScreen.MENU_BACK)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, backString, screen.getHeight() / 3
				* 2 + fontRegularMetrics.getHeight() * 4);
	}

	/**
	 * Draws game results.
	 *
	 * @param screen
	 *            Screen to draw on.
	 * @param score
	 *            Score obtained.
	 * @param livesRemaining
	 *            Lives remaining when finished.
	 * @param shipsDestroyed
	 *            Total ships destroyed.
	 * @param accuracy
	 *            Total accuracy.
	 * @param isNewRecord
	 *            If the score is a new high score.
	 */
	public void drawResults(final Screen screen, final int score,
			final int livesRemaining, final int shipsDestroyed,
			final float accuracy, final boolean isNewRecord) {
		if(GameMode.getGameMode() == GameMode.P1) {
			String scoreString = String.format("score %04d", score);
			String livesRemainingString = "lives remaining " + livesRemaining;
			String shipsDestroyedString = "enemies destroyed " + shipsDestroyed;
			String accuracyString = String
					.format("accuracy %.2f%%", accuracy * 100);

			int height = isNewRecord ? 4 : 2;

			backBufferGraphics.setColor(Color.WHITE);
			drawCenteredRegularString(screen, scoreString, screen.getHeight()
					/ height);
			drawCenteredRegularString(screen, livesRemainingString,
					screen.getHeight() / height + fontRegularMetrics.getHeight()
							* 2);
			drawCenteredRegularString(screen, shipsDestroyedString,
					screen.getHeight() / height + fontRegularMetrics.getHeight()
							* 4);
			drawCenteredRegularString(screen, accuracyString, screen.getHeight()
					/ height + fontRegularMetrics.getHeight() * 6);
		}
		else if(GameMode.getGameMode() == GameMode.P2) {
			String scoreString = String.format("1P score %04d", score);
			String livesRemainingString = "1P lives remaining " + livesRemaining;
			String shipsDestroyedString = "1P enemies destroyed " + shipsDestroyed;
			String accuracyString = String
					.format("1P accuracy %.2f%%", accuracy * 100);

			int height = isNewRecord ? 4 : 2;

			backBufferGraphics.setColor(Color.WHITE);
			drawCenteredRegularString(screen, scoreString, screen.getHeight()
					/ height - fontBigMetrics.getHeight() * 2);
			drawCenteredRegularString(screen, livesRemainingString,
					screen.getHeight() / height);
			drawCenteredRegularString(screen, shipsDestroyedString,
					screen.getHeight() / height + fontRegularMetrics.getHeight()
							* 4);
			drawCenteredRegularString(screen, accuracyString, screen.getHeight()
					/ height + fontRegularMetrics.getHeight() * 8);
		}

	}
	public void drawResults2(final Screen screen, final int score,final int score2,
			final int livesRemaining,final int livesRemaining2, final int shipsDestroyed,final int shipsDestroyed2,
			final float accuracy,final float accuracy2, final boolean isNewRecord,final boolean isNewRecord2) {
		String scoreString = String.format("1P score %04d", score);
		String livesRemainingString = "1P lives remaining " + livesRemaining;
		String shipsDestroyedString = "1P enemies destroyed " + shipsDestroyed;
		String accuracyString = String
				.format("1P accuracy %.2f%%", accuracy * 100);
		String scoreString2 = String.format("2P score %04d", score2);
		String livesRemainingString2 = "2P lives remaining " + livesRemaining2;
		String shipsDestroyedString2 = "2P enemies destroyed " + shipsDestroyed2;
		String accuracyString2 = String
				.format("2P accuracy %.2f%%", accuracy2 * 100);

		if(isNewRecord2==true) {
			int height = isNewRecord2 ? 4 : 2;
			backBufferGraphics.setColor(Color.WHITE);
			drawCenteredRegularString(screen, scoreString, screen.getHeight()
					/ height - fontBigMetrics.getHeight() * 2);
			drawCenteredRegularString(screen, scoreString2, screen.getHeight() / height - fontBigMetrics.getHeight());
			drawCenteredRegularString(screen, livesRemainingString,
					screen.getHeight() / height);
			drawCenteredRegularString(screen, livesRemainingString2,
					screen.getHeight() / height + fontRegularMetrics.getHeight()*2);
			drawCenteredRegularString(screen, shipsDestroyedString,
					screen.getHeight() / height + fontRegularMetrics.getHeight()
							* 4);
			drawCenteredRegularString(screen, shipsDestroyedString2,
					screen.getHeight() / height + fontRegularMetrics.getHeight()
							* 6);
			drawCenteredRegularString(screen, accuracyString, screen.getHeight()
					/ height + fontRegularMetrics.getHeight() * 8);
			drawCenteredRegularString(screen, accuracyString2, screen.getHeight()
					/ height + fontRegularMetrics.getHeight() * 10);
		}
		else if(isNewRecord==true && isNewRecord2==true) {
		int height = isNewRecord ? 4 : 2;
			backBufferGraphics.setColor(Color.WHITE);
			drawCenteredRegularString(screen, scoreString, screen.getHeight()
					/ height - fontBigMetrics.getHeight() * 3);
			drawCenteredRegularString(screen, scoreString2, screen.getHeight() / height - fontBigMetrics.getHeight() * 2);
			drawCenteredRegularString(screen, livesRemainingString,
					screen.getHeight() / height- fontBigMetrics.getHeight());
			drawCenteredRegularString(screen, livesRemainingString2,
					screen.getHeight() / height);
			drawCenteredRegularString(screen, shipsDestroyedString,
					screen.getHeight() / height + fontRegularMetrics.getHeight()
							* 2);
			drawCenteredRegularString(screen, shipsDestroyedString2,
					screen.getHeight() / height + fontRegularMetrics.getHeight()
							* 4);
			drawCenteredRegularString(screen, accuracyString, screen.getHeight()
					/ height + fontRegularMetrics.getHeight() * 6);
			drawCenteredRegularString(screen, accuracyString2, screen.getHeight()
					/ height + fontRegularMetrics.getHeight() * 8);
		}
		else {
			int height = isNewRecord ? 4 : 2;
		 backBufferGraphics.setColor(Color.WHITE);
			drawCenteredRegularString(screen, scoreString, screen.getHeight()
					/ height - fontBigMetrics.getHeight() * 2);
			drawCenteredRegularString(screen, scoreString2, screen.getHeight() / height - fontBigMetrics.getHeight());
			drawCenteredRegularString(screen, livesRemainingString,
					screen.getHeight() / height);
			drawCenteredRegularString(screen, livesRemainingString2,
					screen.getHeight() / height + fontRegularMetrics.getHeight()*2);
			drawCenteredRegularString(screen, shipsDestroyedString,
					screen.getHeight() / height + fontRegularMetrics.getHeight()
							* 4);
			drawCenteredRegularString(screen, shipsDestroyedString2,
					screen.getHeight() / height + fontRegularMetrics.getHeight()
							* 6);
			drawCenteredRegularString(screen, accuracyString, screen.getHeight()
					/ height + fontRegularMetrics.getHeight() * 8);
			drawCenteredRegularString(screen, accuracyString2, screen.getHeight()
					/ height + fontRegularMetrics.getHeight() * 10);
		}
	}
	/**
	 * Draws interactive characters for name input.
	 *
	 * @param screen
	 *            Screen to draw on.
	 * @param name
	 *            Current name selected.
	 * @param nameCharSelected
	 *            Current character selected for modification.
	 */
	public void drawNameInput2(final Screen screen, final char[] name, final int nameCharSelected,
			final char[] name2, final int nameCharSelected2) {
			String newRecordString = "New Record!";
			String introduceNameString = "Introduce green player name:";
			String introduceName2String = "Introduce blue player name:";

			backBufferGraphics.setColor(Color.GREEN);
			drawCenteredRegularString(screen, newRecordString, screen.getHeight()
					/ 4 + fontRegularMetrics.getHeight() * 12);
			backBufferGraphics.setColor(Color.WHITE);
			drawCenteredRegularString(screen, introduceNameString,
					screen.getHeight() / 4 + fontRegularMetrics.getHeight() * 14);
			drawCenteredRegularString(screen, introduceName2String,
					screen.getHeight() / 4 + fontRegularMetrics.getHeight() * 18);

			// 3 letters name.
			int positionX = screen.getWidth()
					/ 2
					- (fontRegularMetrics.getWidths()[name[0]]
							+ fontRegularMetrics.getWidths()[name[1]]
							+ fontRegularMetrics.getWidths()[name[2]]
									+ fontRegularMetrics.getWidths()[' ']) / 2;

			for (int i = 0; i < 3; i++) {
				if (i == nameCharSelected)
					backBufferGraphics.setColor(Color.GREEN);
				else
					backBufferGraphics.setColor(Color.WHITE);

				positionX += fontRegularMetrics.getWidths()[name[i]] / 2;
				positionX = i == 0 ? positionX
						: positionX
								+ (fontRegularMetrics.getWidths()[name[i - 1]]
										+ fontRegularMetrics.getWidths()[' ']) / 2;

				backBufferGraphics.drawString(Character.toString(name[i]),
						positionX,
						screen.getHeight() / 4 + fontRegularMetrics.getHeight()
								* 16);
			}
			int positionY = screen.getWidth()
					/ 2
					- (fontRegularMetrics.getWidths()[name2[0]]
							+ fontRegularMetrics.getWidths()[name2[1]]
							+ fontRegularMetrics.getWidths()[name2[2]]
									+ fontRegularMetrics.getWidths()[' ']) / 2;

			for (int i = 0; i < 3; i++) {
				if (i == nameCharSelected2)
					backBufferGraphics.setColor(Color.GREEN);
				else
					backBufferGraphics.setColor(Color.WHITE);

				positionY += fontRegularMetrics.getWidths()[name2[i]] / 2;
				positionY = i == 0 ? positionY
						: positionY
								+ (fontRegularMetrics.getWidths()[name2[i - 1]]
										+ fontRegularMetrics.getWidths()[' ']) / 2;

				backBufferGraphics.drawString(Character.toString(name2[i]),
						positionY,
						screen.getHeight() / 4 + fontRegularMetrics.getHeight()
								* 20);
			}

	}
	public void drawNameInput(final Screen screen, final char[] name,
			final int nameCharSelected) {
			String newRecordString = "New Record!";
			String introduceNameString = "Introduce name:";

			backBufferGraphics.setColor(Color.GREEN);
			drawCenteredRegularString(screen, newRecordString, screen.getHeight()
					/ 4 + fontRegularMetrics.getHeight() * 10);
			backBufferGraphics.setColor(Color.WHITE);
			drawCenteredRegularString(screen, introduceNameString,
					screen.getHeight() / 4 + fontRegularMetrics.getHeight() * 12);

			// 3 letters name.
			int positionX = screen.getWidth()
					/ 2
					- (fontRegularMetrics.getWidths()[name[0]]
							+ fontRegularMetrics.getWidths()[name[1]]
							+ fontRegularMetrics.getWidths()[name[2]]
									+ fontRegularMetrics.getWidths()[' ']) / 2;

			for (int i = 0; i < 3; i++) {
				if (i == nameCharSelected)
					backBufferGraphics.setColor(Color.GREEN);
				else
					backBufferGraphics.setColor(Color.WHITE);

				positionX += fontRegularMetrics.getWidths()[name[i]] / 2;
				positionX = i == 0 ? positionX
						: positionX
								+ (fontRegularMetrics.getWidths()[name[i - 1]]
										+ fontRegularMetrics.getWidths()[' ']) / 2;

				backBufferGraphics.drawString(Character.toString(name[i]),
						positionX,
						screen.getHeight() / 4 + fontRegularMetrics.getHeight()
								* 14);
			}
	}
	public void drawNameInputgreen(final Screen screen, final char[] name,
			final int nameCharSelected) {
			String newRecordString = "New Record from Green Player!";
			String introduceNameString = "Introduce Green name:";

			backBufferGraphics.setColor(Color.GREEN);
			drawCenteredRegularString(screen, newRecordString, screen.getHeight()
					/ 4 + fontRegularMetrics.getHeight() * 14);
			backBufferGraphics.setColor(Color.WHITE);
			drawCenteredRegularString(screen, introduceNameString,
					screen.getHeight() / 4 + fontRegularMetrics.getHeight() * 16);

			// 3 letters name.
			int positionX = screen.getWidth()
					/ 2
					- (fontRegularMetrics.getWidths()[name[0]]
							+ fontRegularMetrics.getWidths()[name[1]]
							+ fontRegularMetrics.getWidths()[name[2]]
									+ fontRegularMetrics.getWidths()[' ']) / 2;

			for (int i = 0; i < 3; i++) {
				if (i == nameCharSelected)
					backBufferGraphics.setColor(Color.GREEN);
				else
					backBufferGraphics.setColor(Color.WHITE);

				positionX += fontRegularMetrics.getWidths()[name[i]] / 2;
				positionX = i == 0 ? positionX
						: positionX
								+ (fontRegularMetrics.getWidths()[name[i - 1]]
										+ fontRegularMetrics.getWidths()[' ']) / 2;

				backBufferGraphics.drawString(Character.toString(name[i]),
						positionX,
						screen.getHeight() / 4 + fontRegularMetrics.getHeight()
								* 18);
			}
	}
	public void drawNameInputblue(final Screen screen, final char[] name,
			final int nameCharSelected) {
			String newRecordString = "New Record from Blue Player!";
			String introduceNameString = "Introduce Blue name:";

			backBufferGraphics.setColor(Color.GREEN);
			drawCenteredRegularString(screen, newRecordString, screen.getHeight()
					/ 4 + fontRegularMetrics.getHeight() * 14);
			backBufferGraphics.setColor(Color.WHITE);
			drawCenteredRegularString(screen, introduceNameString,
					screen.getHeight() / 4 + fontRegularMetrics.getHeight() * 16);

			// 3 letters name.
			int positionX = screen.getWidth()
					/ 2
					- (fontRegularMetrics.getWidths()[name[0]]
							+ fontRegularMetrics.getWidths()[name[1]]
							+ fontRegularMetrics.getWidths()[name[2]]
									+ fontRegularMetrics.getWidths()[' ']) / 2;

			for (int i = 0; i < 3; i++) {
				if (i == nameCharSelected)
					backBufferGraphics.setColor(Color.GREEN);
				else
					backBufferGraphics.setColor(Color.WHITE);

				positionX += fontRegularMetrics.getWidths()[name[i]] / 2;
				positionX = i == 0 ? positionX
						: positionX
								+ (fontRegularMetrics.getWidths()[name[i - 1]]
										+ fontRegularMetrics.getWidths()[' ']) / 2;

				backBufferGraphics.drawString(Character.toString(name[i]),
						positionX,
						screen.getHeight() / 4 + fontRegularMetrics.getHeight()
								* 18);
			}
	}

	/**
	 * Draws basic content of game over screen.
	 *
	 * @param screen
	 *            Screen to draw on.
	 * @param acceptsInput
	 *            If the screen accepts input.
	 * @param isNewRecord
	 *            If the score is a new high score.
	 */
	public void drawGameOver(final Screen screen, final boolean acceptsInput,
			final boolean isNewRecord) {
		if(GameMode.getGameMode() == GameMode.P1) {
			String gameOverString = "Game Over";
			String continueOrExitString =
					"Press Space to play again, Escape to exit";

			int height = isNewRecord ? 4 : 2;

			backBufferGraphics.setColor(Color.GREEN);
			drawCenteredBigString(screen, gameOverString, screen.getHeight()
					/ height - fontBigMetrics.getHeight() * 2);

			if (acceptsInput)
				backBufferGraphics.setColor(Color.GREEN);
			else
				backBufferGraphics.setColor(Color.GRAY);
			drawCenteredRegularString(screen, continueOrExitString,
					screen.getHeight() / 2 + fontRegularMetrics.getHeight() * 10);
		}
		else if (GameMode.getGameMode() == GameMode.P2) {
			String gameOverString = "Game Over";
			String continueOrExitString =
					"Press Space to play again, Escape to exit";

			int height = isNewRecord ? 4 : 2;

			backBufferGraphics.setColor(Color.GREEN);
			drawCenteredBigString(screen, gameOverString, screen.getHeight()
					/ height - fontBigMetrics.getHeight() * 4);

			if (acceptsInput)
				backBufferGraphics.setColor(Color.GREEN);
			else
				backBufferGraphics.setColor(Color.GRAY);
			drawCenteredRegularString(screen, continueOrExitString,
					screen.getHeight() / 2 + fontRegularMetrics.getHeight() * 14);
			}
	}

	/**
	 * Draws high score screen title and instructions.
	 *
	 * @param screen
	 *            Screen to draw on.
	 */
	public void drawHighScoreMenu(final Screen screen) {
		String highScoreString = "High Scores";
		String instructionsString = "Press Space to return";

		backBufferGraphics.setColor(Color.GREEN);
		drawCenteredBigString(screen, highScoreString, screen.getHeight() / 8);

		backBufferGraphics.setColor(Color.GRAY);
		drawCenteredRegularString(screen, instructionsString,
				screen.getHeight() / 5);
	}

	/**
	 * Draws high scores.
	 *
	 * @param screen
	 *            Screen to draw on.
	 * @param highScores
	 *            List of high scores.
	 */
	public void drawHighScores(final Screen screen,
							   final List<Score> highScores) {
		backBufferGraphics.setColor(Color.WHITE);
		int i = 0;
		String scoreString = "";

		for (Score score : highScores) {
			scoreString = String.format("%s        %04d", score.getName(),
					score.getScore());
			drawCenteredRegularString(screen, scoreString, screen.getHeight()
					/ 3 + fontRegularMetrics.getHeight() * (i + 1) * 2);
			i++;
		}
	}

	/**
	 * Draws a centered string on regular font.
	 *
	 * @param screen
	 *            Screen to draw on.
	 * @param string
	 *            String to draw.
	 * @param height
	 *            Height of the drawing.
	 */
	public void drawCenteredRegularString(final Screen screen,
			final String string, final int height) {
		backBufferGraphics.setFont(fontRegular);
		backBufferGraphics.drawString(string, screen.getWidth() / 2
				- fontRegularMetrics.stringWidth(string) / 2, height);
	}

	/**
	 * Draws a centered string on big font.
	 *
	 * @param screen
	 *            Screen to draw on.
	 * @param string
	 *            String to draw.
	 * @param height
	 *            Height of the drawing.
	 */
	public void drawCenteredBigString(final Screen screen, final String string,
			final int height) {
		backBufferGraphics.setFont(fontBig);
		backBufferGraphics.drawString(string, screen.getWidth() / 2
				- fontBigMetrics.stringWidth(string) / 2, height);
	}

	/**
	 * Countdown to game start.
	 *
	 * @param screen
	 *            Screen to draw on.
	 * @param level
	 *            Game difficulty level.
	 * @param number
	 *            Countdown number.
	 * @param bonusLife
	 *            Checks if a bonus life is received.
	 */
	public void drawCountDown(final Screen screen, final int level,
			final int number, final boolean bonusLife) {
		int rectWidth = screen.getWidth();
		int rectHeight = screen.getHeight() / 6;
		backBufferGraphics.setColor(Color.BLACK);
		backBufferGraphics.fillRect(0, screen.getHeight() / 2 - rectHeight / 2,
				rectWidth, rectHeight);
		backBufferGraphics.setColor(Color.GREEN);
		if (number >= 4)
			if (!bonusLife) {
				drawCenteredBigString(screen, "Level " + level,
						screen.getHeight() / 2
						+ fontBigMetrics.getHeight() / 3);
			} else {
				drawCenteredBigString(screen, "Level " + level
						+ " - Bonus life!",
						screen.getHeight() / 2
						+ fontBigMetrics.getHeight() / 3);
			}
		else if (number != 0)
			drawCenteredBigString(screen, Integer.toString(number),
					screen.getHeight() / 2 + fontBigMetrics.getHeight() / 3);
		else
			drawCenteredBigString(screen, "GO!", screen.getHeight() / 2
					+ fontBigMetrics.getHeight() / 3);
	}
}