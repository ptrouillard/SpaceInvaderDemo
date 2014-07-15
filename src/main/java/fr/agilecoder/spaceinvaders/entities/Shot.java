package fr.agilecoder.spaceinvaders.entities;

import fr.agilecoder.game.Sprite;
import fr.agilecoder.game.SpriteEntity;

/**
 * User: ptrouillard@gmail.com
 * Date: 06/07/14 21:08
 */
public class Shot extends SpriteEntity {

    public Shot(Sprite sprite) {
        super(sprite);
    }

    public Shot(Sprite sprite, int x, int y) {
        super(sprite, x, y);
    }

    @Override
    public int move(long delta) {
        super.move(delta);
        // if we shot off the screen, remove ourselfs
        if (y < -100) {
            return SpaceInvaderActionEnum.ENTITY_REMOVED;
        }
        logger.info("move x: {}, y: {}, dx: {}, dy: {}", x,y,dx,dy);
        return SpaceInvaderActionEnum.MOVED;
    }

    @Override
    public int doLogic() {
        return SpaceInvaderActionEnum.LOGIC_DONE;
    }

    public int collidedWith(SpriteEntity other) {
        // if we've hit an alien, kill it!
        if (other instanceof Alien) {
            return SpaceInvaderActionEnum.COLLISION_DETECTED;
        }
        return SpaceInvaderActionEnum.COLLISION_WITHOUT_EFFECT;
    }

    @Override
    public String toString() {
        return "Shot{" +
                "x=" + x +
                ", y=" + y +
                ", dx=" + dx +
                ", dy=" + dy +
                '}';
    }
}
