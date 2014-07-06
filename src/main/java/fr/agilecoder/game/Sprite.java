package fr.agilecoder.game;

import java.awt.*;

/**
 * User: ptrouillard@gmail.com
 * Date: 06/07/14 20:04
 */
public class Sprite {

    private Image image;

    public Sprite(Image image) {
        this.image = image;
    }

    public int getWidth() {
        return image.getWidth(null);
    }

    public int getHeight() {
        return image.getHeight(null);
    }

    public void draw(Graphics g,int x,int y) {
        g.drawImage(image,x,y,null);
    }
}