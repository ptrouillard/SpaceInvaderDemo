package fr.agilecoder.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * User: ptrouillard@gmail.com
 * Date: 06/07/14 20:07
 */
public class SpriteStore {

    private Logger logger = LoggerFactory.getLogger(SpriteStore.class);

    private Map<String, Sprite> sprites = new HashMap<String, Sprite>();

    private static SpriteStore single = new SpriteStore();
    public static SpriteStore get() {
        return single;
    }

    public Sprite getSprite(String ref) {

        Sprite sprite = sprites.get(ref);
        if (sprite == null) {
            try {
                sprite = loadSprite(ref);
            } catch (IOException e) {
                logger.error("cannot create sprite '{}'", ref, e);
            }
            sprites.put(ref, sprite);
        }

        return sprite;
    }

    private Sprite loadSprite(String ref) throws IOException {
        URL url = this.getClass().getClassLoader().getResource(ref);
        Image sourceImage = ImageIO.read(url);
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        Image image = gc.createCompatibleImage(sourceImage.getWidth(null),sourceImage.getHeight(null),Transparency.BITMASK);
        image.getGraphics().drawImage(sourceImage,0,0,null);
        return new Sprite(image);  //To change body of created methods use File | Settings | File Templates.
    }
}
