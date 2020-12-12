package engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LevelSettingsTest {

    int formationWidth;
    int formationHeight;
    int baseSpeed;
    int shootingFrequency;
    LevelSettings levelSettings;

    @BeforeEach
    void setUp() {
        formationWidth = 3;
        formationHeight = 2;
        baseSpeed = 60;
        shootingFrequency = 2000;
        levelSettings = new LevelSettings(formationWidth, formationHeight, baseSpeed, shootingFrequency);
    }

    @Test
    void TestGetFormationWidth_ShouldReturnCurrentFormationWidth() {
        int expected = formationWidth;
        int actual = levelSettings.getFormationWidth();
        assertEquals(expected, actual, "getFormationWidth should return current formation width");
    }

    @Test
    void TestGetFormationHeight_ShouldReturnCurrentFormationHeight() {
        int expected = formationHeight;
        int actual = levelSettings.getFormationHeight();
        assertEquals(expected, actual, "getFormationHeight should return current formation height");
    }

    @Test
    void TestGetBaseSpeed_ShouldReturnCurrentBaseSpeed() {
        int expected = baseSpeed;
        int actual = levelSettings.getBaseSpeed();
        assertEquals(expected, actual, "getFormationBaseSpeed should return current base speed");
    }

    @Test
    void TestGetShootingFrecuency_ShouldReturnShootingFrecuency() {
        int expected = shootingFrequency;
        int actual = levelSettings.getShootingFrecuency();
        assertEquals(expected, actual, "getShootingFrequency should return current shooting frequency");
    }
}