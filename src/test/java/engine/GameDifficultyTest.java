package engine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameDifficultyTest {

    @Test
    void TestGetDifficulty_ShouldReturnInvalidDifficulty()
    {
        assertTrue(GameDifficulty.getDiffculty() == GameDifficulty.EASY ||
                GameDifficulty.getDiffculty() == GameDifficulty.NORMAL ||
                GameDifficulty.getDiffculty() == GameDifficulty.HARD, "getDifficulty should return valid difficulty");
    }

//    @Test
//    void TestSetDifficulty_InvalidDifficulty_ShouldThrowException(){
//        assertThrow(GameDifficulty.setDifficulty(10000), "When you set Invalid difficulty," +
//                "setDifficulty should throw exception");
//    }
}