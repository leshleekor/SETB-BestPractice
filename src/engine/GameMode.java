package engine;

public class GameMode {
    public final static int P1 = 1;
    public final static int P2 = 2;

    private static int selectedGameMode;

    public static int getGameMode(){ return selectedGameMode; }

    public static void setGameMode(int gameMode){
        GameMode.selectedGameMode = gameMode;
        switch (GameMode.selectedGameMode){
            case P1:

                break;
            case P2:

                break;
            default:
                break;
        }
        Core.setModeSettings(gameMode);
    }
}
