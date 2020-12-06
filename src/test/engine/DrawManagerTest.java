package engine;

import entity.Entity;
import org.junit.jupiter.api.Test;
import screen.ModeScreen;
import screen.Screen;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
class DrawManagerTest {
    DrawManager instance;
    Frame frame;
    Frame b;
    Frame c = new Frame(1, 1);
    int option;
    Graphics backBufferGraphics;
    Font fontRegular;
    Screen sc = new Screen(1, 1, 1);
    BufferedImage bBuffer = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
    Entity en = new Entity(1, 1, 1, 1, Color.BLACK);
    Map<DrawManager.SpriteType, boolean[][]> spriteMap;
    FontMetrics fontRegularMetrics;
    DrawManager dr = new DrawManager();


    @Test
    void getInstance() {
        DrawManager instance = new DrawManager();
        instance.getInstance();
        assertNotNull(instance);

    }

    @Test
    void setFrame() {
        DrawManager sf = new DrawManager();
        sf.setFrame(b);
        assertEquals(frame, b);

    }

    @Test
    void initDrawing() {
        DrawManager init = new DrawManager();
        init.setFrame(c);
        init.initDrawing(sc);
        assertEquals(bBuffer.getHeight(),1);
        assertEquals(bBuffer.getWidth(), 1);
    }

    @Test
    void completeDrawing() {
    }

    @Test
    void drawScore() {
        DrawManager ds = new DrawManager();
        ds.drawScore(sc, 1, 1);
        BufferedImage bBuffer = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        backBufferGraphics = bBuffer.getGraphics();
        assertEquals(backBufferGraphics.getColor(), Color.WHITE);
    }

    @Test
    void drawLives() {
    }

    @Test
    void drawHorizontalLine() {
    }

    @Test
    void drawTitle() {
    }

    @Test
    void drawMenu() {
        DrawManager dm = new DrawManager();
        dm.drawMenu(sc, option);
        BufferedImage bBuffer = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        backBufferGraphics = bBuffer.getGraphics();
        assertEquals(backBufferGraphics.getColor(), Color.WHITE);
    }

    @Test
    void drawDifficultyMenu() {
        DrawManager ddm = new DrawManager();
        ddm.drawDifficultyMenu(sc, option);
        BufferedImage bBuffer = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        backBufferGraphics = bBuffer.getGraphics();
        assertEquals(backBufferGraphics.getColor(), Color.WHITE);
    }

    @Test
    void drawModeMenu() {
        DrawManager dmm = new DrawManager();
        dmm.drawModeMenu(sc, option);
        BufferedImage bBuffer = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        backBufferGraphics = bBuffer.getGraphics();
        assertEquals(backBufferGraphics.getColor(), Color.WHITE);
    }

    @Test
    void drawHighScoreMenu() {
        DrawManager hsm = new DrawManager();
        hsm.drawDifficultyMenu(sc, option);
        BufferedImage bBuffer = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        backBufferGraphics = bBuffer.getGraphics();
        assertEquals(backBufferGraphics.getColor(), Color.WHITE);
    }

    @Test
    void drawResults() {
        dr.drawResults(sc, 1, 1, 1, 1/2, true);

    }

    @Test
    void drawNameInput() {
    }

    @Test
    void drawGameOver() {
    }

    @Test
    void drawHighScores() {
    }

    @Test
    void drawCenteredRegularString() {
    }

    @Test
    void drawCenteredBigString() {
    }

    @Test
    void drawCountDown() {
        int rectWidth = sc.getWidth();
        DrawManager dcd = new DrawManager();
        dcd.drawCountDown(sc, 1, 2, true);
        BufferedImage bBuffer = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        backBufferGraphics = bBuffer.getGraphics();
        backBufferGraphics.setColor(Color.BLACK);
        assertEquals(rectWidth, 1);
        assertEquals(backBufferGraphics.getColor(), Color.BLACK);
    }
}
