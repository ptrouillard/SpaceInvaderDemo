package fr.agilecoder.game;

import java.awt.*;

/**
 * User: ptrouillard@gmail.com
 * Date: 06/07/14 20:41
 */
public interface Entity {

    int move(long delta);

    void draw(Graphics2D g);

    int collidesWith(SpriteEntity other);
}
