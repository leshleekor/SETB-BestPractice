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
                levelSettingsList.add(new LevelSettings(3, 2, 60, 2000));
                /** Difficulty settings for level 2. */
                levelSettingsList.add(new LevelSettings(3, 3, 60, 2000));
                /** Difficulty settings for level 3. */
                levelSettingsList.add(new LevelSettings(4, 3, 50, 1500));
                /** Difficulty settings for level 4. */
                levelSettingsList.add(new LevelSettings(4, 3, 40, 1500));
                /** Difficulty settings for level 5. */
                levelSettingsList.add(new LevelSettings(4, 3, 40, 1000));
                /** Difficulty settings for level 6. */
                levelSettingsList.add(new LevelSettings(4, 4, 30, 1000));
                /** Difficulty settings for level 7. */
                levelSettingsList.add(new LevelSettings(4, 4, 20, 500));
                break;
            case NORMAL:
                /** Difficulty settings for level 1. */
                levelSettingsList.add(new LevelSettings(3, 3, 40, 1500));
                /** Difficulty settings for level 2. */
                levelSettingsList.add(new LevelSettings(4, 3, 35, 1500));
                /** Difficulty settings for level 3. */
                levelSettingsList.add(new LevelSettings(4, 4, 35, 1000));
                /** Difficulty settings for level 4. */
                levelSettingsList.add(new LevelSettings(5, 4, 30, 800));
                /** Difficulty settings for level 5. */
                levelSettingsList.add(new LevelSettings(5, 4, 30, 700));
                /** Difficulty settings for level 6. */
                levelSettingsList.add(new LevelSettings(6, 5, 25, 500));
                /** Difficulty settings for level 7. */
                levelSettingsList.add(new LevelSettings(6, 5, 20, 500));
                break;
            case HARD:
                /** Difficulty settings for level 1. */
                levelSettingsList.add(new LevelSettings(4, 3, 30, 1000));
                /** Difficulty settings for level 1. */
                levelSettingsList.add(new LevelSettings(4, 4, 25, 1000));
                /** Difficulty settings for level 1. */
                levelSettingsList.add(new LevelSettings(5, 4, 20, 800));
                /** Difficulty settings for level 1. */
                levelSettingsList.add(new LevelSettings(6, 5, 15, 700));
                /** Difficulty settings for level 1. */
                levelSettingsList.add(new LevelSettings(7, 6, 15, 600));
                /** Difficulty settings for level 1. */
                levelSettingsList.add(new LevelSettings(7, 7, 10, 400));
                /** Difficulty settings for level 1. */
                levelSettingsList.add(new LevelSettings(8, 7, 5, 200));
                break;
            default:
                break;
        }
        Core.setLevelSettings(levelSettingsList);
    }
}
