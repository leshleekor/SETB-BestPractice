package engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ScoreTest {

    /** Player's name. */
    private String name;
    /** Score points. */
    private int playerScore;
    private Score score;

    @BeforeEach
    void setUp(){
        name = "ABC";
        playerScore = 10000;
        score = new Score(name, playerScore);
    }

    @Test
    void TestGetName_ShouldReturnCurrentName() {
        String expected = this.name;
        String actual = score.getName();
        assertEquals(expected, actual, "getName should return current player name");
    }

    @Test
    void TestGetScore_ShouldReturnCurrentScore() {
        int expected = this.playerScore;
        int actual = score.getScore();
        assertEquals(expected, actual, "getCurrentScore should return current score");
    }

    @Test
    void TestCompareTo_ShouldReturnValidCompareInteger(){
        assertTrue(score.compareTo(score) == 1 ||
                score.compareTo(score) == 0 ||
                score.compareTo(score) == -1, "compareTo should return -1 or 0 or 1");
    }
}