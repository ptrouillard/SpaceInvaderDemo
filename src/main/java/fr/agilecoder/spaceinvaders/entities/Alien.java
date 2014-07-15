package fr.agilecoder.spaceinvaders.entities;

import fr.agilecoder.game.Sprite;
import fr.agilecoder.game.SpriteEntity;

/**
 * User: ptrouillard@gmail.com
 * Date: 06/07/14 20:49
 */
public class Alien extends SpriteEntity {

    public Alien(Sprite sprite) {
        super(sprite);
    }

    public Alien(Sprite sprite, int x, int y) {
        super(sprite, x, y);
    }

    @Override
    public int move(long delta) {
        if ((dx < 0) && (x < 10)) {
            return SpaceInvaderActionEnum.UPDATE_LOGIC;
        }
        if ((dx > 0) && (x > 750)) {
            return SpaceInvaderActionEnum.UPDATE_LOGIC;
        }
        // proceed with normal move
        return super.move(delta);
    }

    public int doLogic() {
        // swap over horizontal movement and move down the
        // screen a bit
        dx = -dx;
        y += 10;

        // if we've reached the bottom of the screen then the player
        // dies
        if (y > 570) {
            return SpaceInvaderActionEnum.NOTIFY_DEATH;
        }
        return SpaceInvaderActionEnum.LOGIC_DONE;
    }

    @Override
    public String toString() {
        return "Alien{" +
                "x=" + x +
                ", y=" + y +
                ", dx=" + dx +
                ", dy=" + dy +
                '}';
    }
}
