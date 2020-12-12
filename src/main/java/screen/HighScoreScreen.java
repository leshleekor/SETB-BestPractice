package screen;

import engine.Core;
import engine.Score;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;

/**
 * Implements the high scores screen, it shows player records.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public class HighScoreScreen extends Screen {

	/** List of past high scores. */
	private List<Score> highScores;

	/**
	 * Constructor, establishes the properties of the screen.
	 *
	 * @param width
	 *            Screen width.
	 * @param height
	 *            Screen height.
	 * @param fps
	 *            Frames per second, frame rate at which the game is run.
	 */
	public HighScoreScreen(final int width, final int height, final int fps) {
		super(width, height, fps);

		this.returnCode = ScreenCode.MAIN;
		try {
			this.highScores = Core.getFileManager().loadHighScores();
		} catch (NumberFormatException | IOException e) {
			logger.warning("Couldn't load high scores!");
		}
	}

	/**
	 * Starts the action.
	 *
	 * @return Next screen code.
	 */
	public final int run() throws IOException {
		super.run();

		return this.returnCode;
	}

	/**
	 * Updates the elements on screen and checks for events.
	 */
	protected final void update() throws IOException {
		super.update();

		draw();
		if (inputManager.isKeyDown(KeyEvent.VK_SPACE)
				&& this.inputDelay.checkFinished()){
			this.isRunning = false;
		}
		else if (inputManager.isKeyDown(KeyEvent.VK_SHIFT)
		&& this.inputDelay.checkFinished()){
			Core.getFileManager().deleteScores();
			this.isRunning = false;
		}
	}

	/**
	 * Draws the elements associated with the screen.
	 */
	private void draw() {
		drawManager.initDrawing(this);

//		drawManager.drawHighScoreMenu(this);
		drawManager.drawTitle(this, "High Scores", "Press Space to return");
		drawManager.drawHighScores(this, this.highScores);

		drawManager.completeDrawing(this);
	}



}
