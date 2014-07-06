package fr.agilecoder.spaceinvaders;

import com.google.common.collect.Lists;
import fr.agilecoder.game.Entity;
import fr.agilecoder.game.Sprite;
import fr.agilecoder.game.SpriteEntity;
import fr.agilecoder.game.SpriteStore;
import fr.agilecoder.spaceinvaders.entities.Alien;
import fr.agilecoder.spaceinvaders.entities.Ship;
import fr.agilecoder.spaceinvaders.entities.Shot;
import fr.agilecoder.spaceinvaders.entities.SpaceInvaderActionEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.util.List;

/**
 * User: ptrouillard@gmail.com
 * Date: 06/07/14 18:53
 */
public class Game extends Canvas {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BufferStrategy strategy;
    private boolean gameRunning = true; // set to false to stop game
    private long lastLoopTime;
    private List<SpriteEntity> entities = Lists.newLinkedList();

    // Entities
    private Ship ship;
    private int alienCount;
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean firePressed;
    private long lastFire;
    private long firingInterval = 1000L;
    private int moveSpeed = 20; // speed of ship
    private boolean logicRequiredThisLoop = true;

    public static void main(String[] args) {
        new Game();
    }

    public Game() {
        JFrame container = new JFrame("Space Invaders 101");
        JPanel panel = (JPanel) container.getContentPane();
        panel.setPreferredSize(new Dimension(800,600));
        panel.setLayout(null);
        setBounds(0, 0, 800, 600);
        panel.add(this);

        setIgnoreRepaint(true);

        container.pack();
        container.setResizable(false);
        container.setVisible(true);

        createBufferStrategy(2);
        strategy = getBufferStrategy();

        initEntities();

        addKeyListener(new KeyInputHandler());

        mainLoop();
    }

    private void initEntities() {

        Sprite shipSprite = SpriteStore.get().getSprite("sprites/ship.gif");
        Sprite alienSprite = SpriteStore.get().getSprite("sprites/alien.gif");

        // create the player ship and place it roughly in the center of the screen
        ship = new Ship(shipSprite, 370, 550);
        entities.add(ship);

        // create a block of aliens (5 rows, by 12 aliens, spaced evenly)
        alienCount = 0;
        for (int row=0;row<5;row++) {
            for (int x=0;x<12;x++) {
                Alien alien = new Alien(alienSprite, 100+(x*50), (50)+row*30);
                entities.add(alien);
                alienCount++;
            }
        }
    }

    private void mainLoop() {
        while (gameRunning) {
            // work out how long its been since the last update, this
            // will be used to calculate how far the entities should
            // move this loop
            long delta = System.currentTimeMillis() - lastLoopTime;
            lastLoopTime = System.currentTimeMillis();

            // Get hold of a graphics context for the accelerated
            // surface and blank it out
            Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
            g.setColor(Color.black);
            g.fillRect(0,0,800,600);

            handleActions();

            checkCollisions();

            handleLogic();

            moveEntities(delta);
            drawEntities(g);

            // finally, we've completed drawing so clear up the graphics
            // and flip the buffer over
            g.dispose();
            strategy.show();

            // finally pause for a bit. Note: this should run us at about
            // 100 fps but on windows this might vary each loop due to
            // a bad implementation of timer
            try { Thread.sleep(10); } catch (Exception e) {}
        }
    }

    private void handleLogic() {
        if (logicRequiredThisLoop) {
            for (int i=0;i<entities.size();i++) {
                SpriteEntity entity = entities.get(i);
                entity.doLogic();
            }
            logicRequiredThisLoop = false;
        }
    }

    private void checkCollisions() {
        // brute force collisions, compare every entity against
        // every other entity. If any of them collide notify
        // both entities that the collision has occurred
        for (int p=0;p<entities.size();p++) {
            for (int s=p+1;s<entities.size();s++) {
                SpriteEntity me = entities.get(p);
                SpriteEntity him = entities.get(s);
                int resultOfCollision = me.collidesWith(him);
                switch(resultOfCollision) {
                    case SpaceInvaderActionEnum.COLLISION_DETECTED:
                        logger.info("collision detected between {} and {}", me, him);
                        break;
                    case SpaceInvaderActionEnum.COLLISION_WITHOUT_EFFECT:
                    case SpaceInvaderActionEnum.NO_COLLISION_DETECTED:
                        break;
                    case SpaceInvaderActionEnum.KILL_SHOT_AND_ALIEN:
                        notifyAlienKilled();
                        break;
                }
            }
        }
    }

    private void handleActions() {
        ship.setDx(0);

        if ((leftPressed) && (!rightPressed)) {
            ship.setDx(-moveSpeed);
        } else if ((rightPressed) && (!leftPressed)) {
            ship.setDx(moveSpeed);
        }
        // if we're pressing fire, attempt to fire
        if (firePressed) {
            tryToFire();
        }
    }

    public void tryToFire() {
        // check that we have waiting long enough to fire
        if (System.currentTimeMillis() - lastFire < firingInterval) {
            return;
        }

        // if we waited long enough, create the shot entity, and record the time.
        lastFire = System.currentTimeMillis();
        Sprite shotSprite = SpriteStore.get().getSprite("sprites/shot.gif");
        Shot shot = new Shot(shotSprite,ship.getX()+10,ship.getY()-30);
        entities.add(shot);
    }

    private void moveEntities(long delta) {

        for (int i=0;i<entities.size();i++) {
            Entity entity = entities.get(i);
            int move = entity.move(delta);
            switch( move) {
                case SpaceInvaderActionEnum.UPDATE_LOGIC:
                    updateLogic();
                break;
                case SpaceInvaderActionEnum.ENTITY_REMOVED:
                    removeEntity(entity);
                break;
            }
        }
    }

    private void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    public void notifyAlienKilled() {
        // reduce the alien count, if there are none left, the player has won!
        alienCount--;

        if (alienCount == 0) {
            notifyWin();
        }

        // if there are still some aliens left then they all need to get faster, so
        // speed up all the existing aliens
        for (int i=0;i<entities.size();i++) {
            SpriteEntity entity = entities.get(i);

            if (entity instanceof Alien) {
                // speed up by 2%
                ((Alien) entity).setDx( (int)(entity.getDx() * 1.02));
            }
        }
    }

    private void notifyWin() {
       logger.info("This is a win !!");
    }

    public void updateLogic() {
        logicRequiredThisLoop = true;
    }

    private void drawEntities(Graphics2D g) {

        for (int i=0;i<entities.size();i++) {
            Entity entity = entities.get(i);
            entity.draw(g);
        }
    }

    private class KeyInputHandler extends KeyAdapter {

        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                leftPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                rightPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                firePressed = true;
            }
        }

        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                leftPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                rightPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                firePressed = false;
            }
        }

        public void keyTyped(KeyEvent e) {
            // if we hit escape, then quit the game
            if (e.getKeyChar() == 27) {
                System.exit(0);
            }
        }
    }

}
