package screen;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import engine.*;
import entity.Bullet;
import entity.BulletPool;
import entity.EnemyShip;
import entity.EnemyShipFormation;
import entity.Entity;
import entity.Ship;

/**
 * Implements the game screen, where the action happens.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public class GameScreen extends Screen {
   /** Milliseconds until the screen accepts user input. */
   private static final int INPUT_DELAY = 6000;
   /** Bonus score for each life remaining at the end of the level. */
   private static final int LIFE_SCORE = 100;
   /** Minimum time between bonus ship's appearances. */
   private static final int BONUS_SHIP_INTERVAL = 20000;
   /** Maximum variance in the time between bonus ship's appearances. */
   private static final int BONUS_SHIP_VARIANCE = 10000;
   /** Time until bonus ship explosion disappears. */
   private static final int BONUS_SHIP_EXPLOSION = 500;
   /** Time from finishing the level to screen change. */
   private static final int SCREEN_CHANGE_INTERVAL = 1500;
   /** Height of the interface separation line. */
   private static final int SEPARATION_LINE_HEIGHT = 80;

   /** Current game difficulty settings. */
   private LevelSettings levelSettings;
   /** Current difficulty level number. */
   private int level;
   private int level2;
   /** Formation of enemy ships. */
   private EnemyShipFormation enemyShipFormation;
   /** Player's ship. */
   private Ship ship;
   private Ship ship2;
   /** Bonus enemy ship that appears sometimes. */
   private EnemyShip enemyShipSpecial;
   /** Minimum time between bonus ship appearances. */
   private Cooldown enemyShipSpecialCooldown;
   /** Time until bonus ship explosion disappears. */
   private Cooldown enemyShipSpecialExplosionCooldown;
   /** Time from finishing the level to screen change. */
   private Cooldown screenFinishedCooldown;
   /** Set of all bullets fired by on screen ships. */
   private Set<Bullet> bullets;
   /** Current score. */
   private int score;
   private int score2;
   /** Player 1 & 2 lives left. */
   private int lives;
   private int lives2;
   /** Total bullets shot by the player. */
   private int bulletsShot;
   private int bulletsShot2;
   /** Total ships destroyed by the player. */
   private int shipsDestroyed;
   private int shipsDestroyed2;
   /** Moment the game starts. */
   private long gameStartTime;
   /** Checks if the level is finished. */
   private boolean levelFinished;
   /** Checks if a bonus life is received. */
   private boolean bonusLife;

   /**
    * Constructor, establishes the properties of the screen.
    *
    * @param gameState
    *            Current game state.
    * @param levelSettings
    *            Current game settings.
    * @param bonusLife
    *            Checks if a bonus life is awarded this level.
    * @param width
    *            Screen width.
    * @param height
    *            Screen height.
    * @param fps
    *            Frames per second, frame rate at which the game is run.
    */
   public GameScreen(final GameState gameState, final GameState gameState2,
                 final LevelSettings levelSettings, final boolean bonusLife,
                 final int width, final int height, final int fps) {
      super(width, height, fps);

      this.levelSettings = levelSettings;
      this.bonusLife = bonusLife;
      this.level = gameState.getLevel();
      this.score = gameState.getScore();
      this.lives = gameState.getLivesRemaining();

      this.level2 = gameState2.getLevel();
      this.score2 = gameState2.getScore();
      this.lives2 = gameState2.getLivesRemaining();

      if (this.bonusLife) {
         if(this.lives > 0 && this.lives < 3) this.lives++;
         if(this.lives2 > 0 && this.lives2 < 3) this.lives2++;
      }
      this.bulletsShot = gameState.getBulletsShot();
      this.shipsDestroyed = gameState.getShipsDestroyed();

      this.bulletsShot2 = gameState2.getBulletsShot();
      this.shipsDestroyed2 = gameState2.getShipsDestroyed();
   }

   /**
    * Initializes basic screen properties, and adds necessary elements.
    */
   public final void initialize() {
      super.initialize();

      enemyShipFormation = new EnemyShipFormation(this.levelSettings);
      enemyShipFormation.attach(this);
      this.ship = new Ship(this.width / 2 - 20, this.height - 30, Color.GREEN, 1);
      this.ship2 = new Ship(this.width / 2 + 20, this.height - 30, Color.BLUE, 2);
      // Appears each 10-30 seconds.
      this.enemyShipSpecialCooldown = Core.getVariableCooldown(
            BONUS_SHIP_INTERVAL, BONUS_SHIP_VARIANCE);
      this.enemyShipSpecialCooldown.restart();
      this.enemyShipSpecialExplosionCooldown = Core
            .getCooldown(BONUS_SHIP_EXPLOSION);
      this.screenFinishedCooldown = Core.getCooldown(SCREEN_CHANGE_INTERVAL);
      this.bullets = new HashSet<Bullet>();

      // Special input delay / countdown.
      this.gameStartTime = System.currentTimeMillis();
      this.inputDelay = Core.getCooldown(INPUT_DELAY);
      this.inputDelay.restart();
   }

   /**
    * Starts the action.
    *
    * @return Next screen code.
    */
   public final int run() throws IOException {
      super.run();
      if(this.lives > 0)
         this.score += LIFE_SCORE * (this.lives - 1);

      if(this.lives2 > 0)
         this.score2 += LIFE_SCORE * (this.lives2 - 1);
      this.logger.info("Screen cleared with a score of " + this.score + " " + this.score2);

      return this.returnCode;
   }

   /**
    * Updates the elements on screen and checks for events.
    */
   protected final void update() throws IOException {
      super.update();

      if (this.inputDelay.checkFinished() && !this.levelFinished) {

         if (!this.ship.isDestroyed() && this.lives > 0) {
            boolean moveRight = inputManager.isKeyDown(KeyEvent.VK_RIGHT);
            boolean moveLeft = inputManager.isKeyDown(KeyEvent.VK_LEFT);
            boolean isRightBorder = this.ship.getPositionX()
                  + this.ship.getWidth() + this.ship.getSpeed() > this.width - 1;
            boolean isLeftBorder = this.ship.getPositionX()
                  - this.ship.getSpeed() < 1;

            if (moveRight && !isRightBorder) {
               this.ship.moveRight();
            }
            if (moveLeft && !isLeftBorder) {
               this.ship.moveLeft();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_UP))
               if (this.ship.shoot(this.bullets))
                  this.bulletsShot++;
         }

         if(GameMode.getGameMode() == GameMode.P2){
            if (!this.ship2.isDestroyed() && this.lives2 > 0) {
               boolean moveRight = inputManager.isKeyDown(KeyEvent.VK_D);
               boolean moveLeft = inputManager.isKeyDown(KeyEvent.VK_A);

               boolean isRightBorder = this.ship2.getPositionX()
                     + this.ship2.getWidth() + this.ship2.getSpeed() > this.width - 1;
               boolean isLeftBorder = this.ship2.getPositionX()
                     - this.ship2.getSpeed() < 1;

               if (moveRight && !isRightBorder) {
                  this.ship2.moveRight();
               }
               if (moveLeft && !isLeftBorder) {
                  this.ship2.moveLeft();
               }
               if (inputManager.isKeyDown(KeyEvent.VK_W))
                  if (this.ship2.shoot(this.bullets))
                     this.bulletsShot2++;
            }
         }



         if (this.enemyShipSpecial != null) {
            if (!this.enemyShipSpecial.isDestroyed())
               this.enemyShipSpecial.move(2, 0);
            else if (this.enemyShipSpecialExplosionCooldown.checkFinished())
               this.enemyShipSpecial = null;

         }
         if (this.enemyShipSpecial == null
               && this.enemyShipSpecialCooldown.checkFinished()) {
            this.enemyShipSpecial = new EnemyShip();
            this.enemyShipSpecialCooldown.restart();
            this.logger.info("A special ship appears");
         }
         if (this.enemyShipSpecial != null
               && this.enemyShipSpecial.getPositionX() > this.width) {
            this.enemyShipSpecial = null;
            this.logger.info("The special ship has escaped");
         }

         this.ship.update();
         this.ship2.update();
         this.enemyShipFormation.update();
         this.enemyShipFormation.shoot(this.bullets);
      }

      manageCollisions();
      cleanBullets();
      draw();

      if ((this.enemyShipFormation.isEmpty() || (this.lives == 0 && (GameMode.getGameMode() == GameMode.P1 || this.lives2 == 0)))
            && !this.levelFinished) {
         this.levelFinished = true;
         this.screenFinishedCooldown.restart();
      }

      if (this.levelFinished && this.screenFinishedCooldown.checkFinished())
         this.isRunning = false;

   }

   /**
    * Draws the elements associated with the screen.
    */
   private void draw() {
      drawManager.initDrawing(this);

      if(this.lives > 0)
         drawManager.drawEntity(this.ship, this.ship.getPositionX(),
                 this.ship.getPositionY());

      if(GameMode.getGameMode() == GameMode.P2) {
         if(this.lives2 > 0)
            drawManager.drawEntity(this.ship2, this.ship2.getPositionX(),
                    this.ship2.getPositionY());
      }

      if (this.enemyShipSpecial != null)
         drawManager.drawEntity(this.enemyShipSpecial,
               this.enemyShipSpecial.getPositionX(),
               this.enemyShipSpecial.getPositionY());

      enemyShipFormation.draw();

      for (Bullet bullet : this.bullets)
         drawManager.drawEntity(bullet, bullet.getPositionX(),
               bullet.getPositionY());

      // Interface.
      drawManager.drawScore(this, this.score, 1);
      drawManager.drawLives(this, this.lives, Color.GREEN, 1);

      if(GameMode.getGameMode() == GameMode.P2) {
         drawManager.drawScore(this, this.score2, 2);
         drawManager.drawLives(this, this.lives2, Color.BLUE, 2);
      }

      drawManager.drawHorizontalLine(this, SEPARATION_LINE_HEIGHT - 1);

      // Countdown to game start.
      if (!this.inputDelay.checkFinished()) {
         int countdown = (int) ((INPUT_DELAY
               - (System.currentTimeMillis()
                     - this.gameStartTime)) / 1000);
         drawManager.drawCountDown(this, this.level, countdown,
               this.bonusLife);
         drawManager.drawHorizontalLine(this, this.height / 2 - this.height
               / 12);
         drawManager.drawHorizontalLine(this, this.height / 2 + this.height
               / 12);
      }

      drawManager.completeDrawing(this);
   }

   /**
    * Cleans bullets that go off screen.
    */
   private void cleanBullets() {
      Set<Bullet> recyclable = new HashSet<Bullet>();
      for (Bullet bullet : this.bullets) {
         bullet.update();
         if (bullet.getPositionY() < SEPARATION_LINE_HEIGHT
               || bullet.getPositionY() > this.height)
            recyclable.add(bullet);
      }
      this.bullets.removeAll(recyclable);
   }

   /**
    * Manages collisions between bullets and ships.
    */
   private void manageCollisions() {
      Set<Bullet> bulletSet = new HashSet<Bullet>();
      for (Bullet bullet : this.bullets)
         if (bullet.getSpeed() > 0) {
            // Player-Bullet 異⑸룎 泥섎━
            if(GameMode.getGameMode() == GameMode.P2) {
               if (checkCollision(bullet, this.ship2) && !this.levelFinished) {
                  // recyclable.add(bullet);
                  if (!this.ship2.isDestroyed()) {
                     this.ship2.destroy();
                     if (this.lives2 > 0)
                        this.lives2--;
                     this.logger.info("Hit on player ship2, " + this.lives2
                             + " lives remaining.");
                  }
               }
            }
            if (checkCollision(bullet, this.ship) && !this.levelFinished) {
               bulletSet.add(bullet);
               if (!this.ship.isDestroyed()) {
                  this.ship.destroy();
                  if(this.lives > 0)
                     this.lives--;
                  this.logger.info("Hit on player ship, " + this.lives
                        + " lives remaining.");
               }
            }
         } else {
            // Enemy-Bullet 異⑸룎 泥섎━
            for (EnemyShip enemyShip : this.enemyShipFormation)
               if (!enemyShip.isDestroyed()
                     && checkCollision(bullet, enemyShip)) {
                  System.out.println(bullet.getOwner());
                  if(bullet.getOwner()==1) {
                     this.score += enemyShip.getPointValue();
                     this.shipsDestroyed++;
                  }
                  if(bullet.getOwner()==2) {
                     this.score2 += enemyShip.getPointValue();
                     this.shipsDestroyed2++;
                  }

                  this.enemyShipFormation.destroy(enemyShip);
                  bulletSet.add(bullet);
               }
            if (this.enemyShipSpecial != null
                  && !this.enemyShipSpecial.isDestroyed()
                  && checkCollision(bullet, this.enemyShipSpecial)) {
               if(bullet.getOwner()==1) {
                  this.score += this.enemyShipSpecial.getPointValue();
                  this.shipsDestroyed++;
               }
               if(bullet.getOwner()==2) {
                  this.score2 += this.enemyShipSpecial.getPointValue();
                  this.shipsDestroyed2++;
               }

               this.enemyShipSpecial.destroy();
               this.enemyShipSpecialExplosionCooldown.restart();
               bulletSet.add(bullet);
            }
         }
      this.bullets.removeAll(bulletSet);
   }

   /**
    * Checks if two entities are colliding.
    *
    * @param a
    *            First entity, the bullet.
    * @param b
    *            Second entity, the ship.
    * @return Result of the collision test.
    */
   private boolean checkCollision(final Entity a, final Entity b) {
      // Calculate center point of the entities in both axis.
      int centerAX = a.getPositionX() + a.getWidth() / 2;
      int centerAY = a.getPositionY() + a.getHeight() / 2;
      int centerBX = b.getPositionX() + b.getWidth() / 2;
      int centerBY = b.getPositionY() + b.getHeight() / 2;
      // Calculate maximum distance without collision.
      int maxDistanceX = a.getWidth() / 2 + b.getWidth() / 2;
      int maxDistanceY = a.getHeight() / 2 + b.getHeight() / 2;
      // Calculates distance.
      int distanceX = Math.abs(centerAX - centerBX);
      int distanceY = Math.abs(centerAY - centerBY);

      return distanceX < maxDistanceX && distanceY < maxDistanceY;
   }

   /**
    * Returns a GameState object representing the status of the game.
    *
    * @return Current game state.
    */
   public final GameState getGameState(int player) {
		if(player == 1)
			return new GameState(this.level, this.score, this.lives,
					this.bulletsShot, this.shipsDestroyed);
		else
			return new GameState(this.level2, this.score2, this.lives2,
					this.bulletsShot2, this.shipsDestroyed2);
	}
}