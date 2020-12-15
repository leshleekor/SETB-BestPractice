package engine;

public class GameMode {
    public final static int P1 = 1;
    public final static int P2 = 2;

    private static int selectedGameMode;

    public static int getGameMode(){ return selectedGameMode; }

    public static void setGameMode(int gameMode){
        GameMode.selectedGameMode = gameMode;
        Core.setModeSettings(gameMode);
    }
}
