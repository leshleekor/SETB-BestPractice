package screen;

import engine.Cooldown;
import engine.Core;
import engine.GameMode;

import java.awt.event.KeyEvent;

public class ModeScreen extends Screen{
    /** Milliseconds between changes in user selection. */
    private static final int SELECTION_TIME = 200;

    /** Time between changes in user selection. */
    private Cooldown selectionCooldown;

    private int cursorMenuItem=1;
    public static final int P1 = 1;
    public static final int P2 = 2;
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
    public ModeScreen(final int width, final int height, final int fps) {
        super(width, height, fps);

        // Defaults to play.
        this.selectionCooldown = Core.getCooldown(SELECTION_TIME);
        this.selectionCooldown.restart();

        // Default return code is Main in Mode screen
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
                        case P1:
                            GameMode.setGameMode(GameMode.P1);
                            break;
                        case P2:
                            GameMode.setGameMode(GameMode.P2);
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
        if (this.cursorMenuItem == P1)
            this.cursorMenuItem = P2;
        else if(this.cursorMenuItem == P2)
            this.cursorMenuItem = MENU_BACK;
        else if(this.cursorMenuItem == MENU_BACK)
            this.cursorMenuItem = P1;
    }

    /**
     * Shifts the focus to the previous menu item.
     */
    private void previousMenuItem() {
        if (this.cursorMenuItem == P2)
            this.cursorMenuItem = P1;
        else if(this.cursorMenuItem == P1)
            this.cursorMenuItem = MENU_BACK;
        else if(this.cursorMenuItem == MENU_BACK)
            this.cursorMenuItem = P2;
    }

    /**
     * Draws the elements associated with the screen.
     */
    private void draw() {
        drawManager.initDrawing(this);

        drawManager.drawTitle(this, "Mode Setting", "select with w+s / arrows, confirm with space");
        drawManager.drawModeMenu(this, this.cursorMenuItem);

        drawManager.completeDrawing(this);
    }
}
