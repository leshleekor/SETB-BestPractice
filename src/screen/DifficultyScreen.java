package screen;

import engine.Cooldown;
import engine.Core;
import engine.GameDifficulty;

import java.awt.event.KeyEvent;

public class DifficultyScreen extends Screen{
    /** Milliseconds between changes in user selection. */
    private static final int SELECTION_TIME = 200;

    /** Time between changes in user selection. */
    private Cooldown selectionCooldown;

    private int cursorMenuItem;
    public static final int MENU_EASY = 0;
    public static final int MENU_NORMAL = 1;
    public static final int MENU_HARD = 2;
    public static final int MENU_BACK = 3;

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
    public DifficultyScreen(final int width, final int height, final int fps) {
        super(width, height, fps);

        // Defaults to play.
        this.selectionCooldown = Core.getCooldown(SELECTION_TIME);
        this.selectionCooldown.restart();

        // Default return code is Main in difficulty screen
        this.returnCode = ScreenCode.MAIN;
    }

    /**
     * Starts the action.
     *
     * @return Next screen code.
     */
    public final int run() {
        super.run();

        return this.returnCode;
    }

    /**
     * Updates the elements on screen and checks for events.
     */
    protected final void update() {
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
                if(this.cursorMenuItem == MENU_BACK) {
                    this.isRunning = false;
                }else{
                    switch (this.cursorMenuItem){
                        case MENU_EASY:
                            GameDifficulty.setDifficulty(GameDifficulty.EASY);
                            break;
                        case MENU_NORMAL:
                            GameDifficulty.setDifficulty(GameDifficulty.NORMAL);
                            break;
                        case MENU_HARD:
                            GameDifficulty.setDifficulty(GameDifficulty.HARD);
                            break;
                        default:
                            break;
                    }
                    this.isRunning = false;
                }
            }
        }
    }

    /**
     * Shifts the focus to the next menu item.
     */
    private void nextMenuItem() {
        if (this.cursorMenuItem == MENU_EASY)
            this.cursorMenuItem = MENU_NORMAL;
        else if(this.cursorMenuItem == MENU_NORMAL)
            this.cursorMenuItem = MENU_HARD;
        else if(this.cursorMenuItem == MENU_HARD)
            this.cursorMenuItem = MENU_BACK;
        else if(this.cursorMenuItem == MENU_BACK)
            this.cursorMenuItem = MENU_EASY;
    }

    /**
     * Shifts the focus to the previous menu item.
     */
    private void previousMenuItem() {
        if (this.cursorMenuItem == MENU_NORMAL)
            this.cursorMenuItem = MENU_EASY;
        else if(this.cursorMenuItem == MENU_HARD)
            this.cursorMenuItem = MENU_NORMAL;
        else if(this.cursorMenuItem == MENU_BACK)
            this.cursorMenuItem = MENU_HARD;
        else if(this.cursorMenuItem == MENU_EASY)
            this.cursorMenuItem = MENU_BACK;
    }

    /**
     * Draws the elements associated with the screen.
     */
    private void draw() {
        drawManager.initDrawing(this);

        drawManager.drawTitle(this, "Difficulty Setting", "select with w+s / arrows, confirm with space");
        drawManager.drawDifficultyMenu(this, this.cursorMenuItem);

        drawManager.completeDrawing(this);
    }
}
