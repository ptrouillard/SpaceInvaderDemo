package fr.agilecoder.game;

import fr.agilecoder.spaceinvaders.entities.Alien;
import fr.agilecoder.spaceinvaders.entities.SpaceInvaderActionEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

/**
 * User: ptrouillard@gmail.com
 * Date: 06/07/14 20:46
 */
public abstract class SpriteEntity implements Entity {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private Sprite sprite;
    protected int x;
    protected int y;
    protected int dx;
    protected int dy;

    public SpriteEntity(Sprite sprite) {
        this.sprite = sprite;
    }

    public SpriteEntity(Sprite sprite, int x, int y) {
        this.sprite = sprite;
        this.x = x;
        this.y = y;
    }

    @Override
    public int move(long delta) {
        //logger.info("move x: {}, y: {}, dx: {}, dy: {}", x,y,dx,dy);
        x += (delta * dx) / 1000;
        y += (delta * dy) / 1000;
        return ActionEnum.MOVED;
    }

    public void draw(Graphics2D g) {
        sprite.draw(g, x, y);
    }

    public int collidesWith(SpriteEntity other) {
        Rectangle me = new Rectangle();
        Rectangle him = new Rectangle();
        me.setBounds(x, y, sprite.getWidth(), sprite.getHeight());
        him.setBounds(other.x, other.y, other.getSprite().getWidth(), other.sprite.getHeight());
        if (me.intersects(him))
            return SpaceInvaderActionEnum.COLLISION_DETECTED;
        return SpaceInvaderActionEnum.NO_COLLISION_DETECTED;
    }

    public abstract int doLogic();

    public Sprite getSprite() {
        return sprite;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public SpriteEntity withDy(int dy) {
        this.dy = dy;
        return this;
    }

    public SpriteEntity withDx(int dx) {
        this.dx = dx;
        return this;
    }

    @Override
    public String toString() {
        return "SpriteEntity{" +
                "x=" + x +
                ", y=" + y +
                ", dx=" + dx +
                ", dy=" + dy +
                '}';
    }

}
