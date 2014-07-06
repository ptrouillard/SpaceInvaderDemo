package fr.agilecoder.spaceinvaders.entities;

import fr.agilecoder.game.Sprite;
import fr.agilecoder.game.SpriteEntity;

/**
 * User: ptrouillard@gmail.com
 * Date: 06/07/14 20:59
 */
public class Ship extends SpriteEntity {

    public Ship(Sprite sprite) {
        super(sprite);
    }

    public Ship(Sprite sprite, int x, int y) {
        super(sprite, x, y);
    }

    @Override
    public int move(long delta) {
        // if we're moving left and have reached the left hand side
        // of the screen, don't move
        if ((dx < 0) && (x < 10)) {
            return SpaceInvaderActionEnum.MOVED;
        }
        // if we're moving right and have reached the right hand side
        // of the screen, don't move
        if ((dx > 0) && (x > 750)) {
            return SpaceInvaderActionEnum.MOVED;
        }
        return super.move(delta);
    }

    @Override
    public int doLogic() {
        return SpaceInvaderActionEnum.LOGIC_DONE;
    }

    public int collidedWith(SpriteEntity other) {
        // if its an alien, notify the game that the player
        // is dead
        if (other instanceof Alien) {
            return SpaceInvaderActionEnum.NOTIFY_DEATH;
        }
        return SpaceInvaderActionEnum.COLLISION_WITHOUT_EFFECT;
    }

    @Override
    public String toString() {
        return "Ship{" +
                "x=" + x +
                ", y=" + y +
                ", dx=" + dx +
                ", dy=" + dy +
                '}';
    }
}
