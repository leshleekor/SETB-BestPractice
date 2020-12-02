package screen;

import java.awt.event.KeyEvent;
import java.io.IOException;

import engine.Cooldown;
import engine.Core;
import engine.GameDifficulty;

/**
 * Implements the title screen.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class TitleScreen extends Screen {

	/** Milliseconds between changes in user selection. */
	private static final int SELECTION_TIME = 200;
	
	/** Time between changes in user selection. */
	private Cooldown selectionCooldown;

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
	public TitleScreen(final int width, final int height, final int fps) {
		super(width, height, fps);

		// Defaults to play.
		this.returnCode = ScreenCode.PLAY;
		this.selectionCooldown = Core.getCooldown(SELECTION_TIME);
		this.selectionCooldown.restart();
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
		if (this.selectionCooldown.checkFinished()
				&& this.inputDelay.checkFinished()) {
			if (inputManager.isKeyDown(KeyEvent.VK_UP)
					|| inputManager.isKeyDown(KeyEvent.VK_W)) {
				previousMenuItem();
				this.selectionCooldown.restart();
			}
			if (inputManager.isKeyDown(KeyEvent.VK_DOWN)
					|| inputManager.isKeyDown(KeyEvent.VK_S)) {
				nextMenuItem();
				this.selectionCooldown.restart();
			}
			if (inputManager.isKeyDown(KeyEvent.VK_SPACE)) {
				this.isRunning = false;
			}
		}
	}

	/**
	 * Shifts the focus to the next menu item.
	 */
	private void nextMenuItem() {
		if (this.returnCode == ScreenCode.EXIT)
			this.returnCode = ScreenCode.PLAY;
		else if (this.returnCode == ScreenCode.PLAY)
			this.returnCode = ScreenCode.DIFFICULTY;
		else if(this.returnCode == ScreenCode.DIFFICULTY)
			this.returnCode = ScreenCode.HIGH_SCORES;
		else if(this.returnCode == ScreenCode.HIGH_SCORES)
			this.returnCode = ScreenCode.EXIT;
	}

	/**
	 * Shifts the focus to the previous menu item.
	 */
	private void previousMenuItem() {
		if (this.returnCode == ScreenCode.PLAY)
			this.returnCode = ScreenCode.EXIT;
		else if(this.returnCode == ScreenCode.HIGH_SCORES)
			this.returnCode = ScreenCode.DIFFICULTY;
		else if(this.returnCode == ScreenCode.DIFFICULTY)
			this.returnCode = ScreenCode.PLAY;
		else if(this.returnCode == ScreenCode.EXIT)
			this.returnCode = ScreenCode.HIGH_SCORES;
	}

	/**
	 * Draws the elements associated with the screen.
	 */
	private void draw() {
		drawManager.initDrawing(this);

		drawManager.drawTitle(this, "Invaders", "select with w+s / arrows, confirm with space");
		drawManager.drawMenu(this, this.returnCode);

		drawManager.completeDrawing(this);
	}
}
