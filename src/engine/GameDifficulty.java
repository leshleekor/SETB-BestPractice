package engine;

import java.util.ArrayList;
import java.util.List;

public class GameDifficulty {
    public final static int EASY = 0;
    public final static int NORMAL = 1;
    public final static int HARD = 2;

    private static int selectedGameDifficulty;

    public static int getDiffculty(){
        return selectedGameDifficulty;
    }

    public static void setDifficulty(int difficulty){
        GameDifficulty.selectedGameDifficulty = difficulty;
        ArrayList<LevelSettings> levelSettingsList = new ArrayList<LevelSettings>();
        switch (GameDifficulty.selectedGameDifficulty) {
            case EASY:
                /** Difficulty settings for level 1. */
                levelSettingsList.add(new LevelSettings(5, 4, 60, 2000));
                /** Difficulty settings for level 2. */
                levelSettingsList.add(new LevelSettings(5, 5, 50, 2500));
                /** Difficulty settings for level 3. */
                levelSettingsList.add(new LevelSettings(6, 5, 40, 1500));
                /** Difficulty settings for level 4. */
                levelSettingsList.add(new LevelSettings(6, 6, 30, 1500));
                /** Difficulty settings for level 5. */
                levelSettingsList.add(new LevelSettings(7, 6, 20, 1000));
                /** Difficulty settings for level 6. */
                levelSettingsList.add(new LevelSettings(7, 7, 10, 1000));
                /** Difficulty settings for level 7. */
                levelSettingsList.add(new LevelSettings(8, 7, 2, 500));
                break;
            case NORMAL:
                /** Fill level 1 ~ 7 */
                /** Example) Difficulty settings for level 1. */
                levelSettingsList.add(new LevelSettings(5, 4, 60, 2000));
                break;
            case HARD:
                /** Fill level 1 ~ 7 */
                /** Example) Difficulty settings for level 1. */
                levelSettingsList.add(new LevelSettings(6, 4, 60, 2000));
                break;
            default:
                break;
        }
        Core.setLevelSettings(levelSettingsList);
    }
}
