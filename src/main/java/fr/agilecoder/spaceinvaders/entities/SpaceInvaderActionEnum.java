package fr.agilecoder.spaceinvaders.entities;

import fr.agilecoder.game.ActionEnum;

/**
 * User: ptrouillard@gmail.com
 * Date: 06/07/14 21:19
 */
public interface SpaceInvaderActionEnum extends ActionEnum {
    public final static int UPDATE_LOGIC = 10;
    public final static int COLLISION_DETECTED = 11;
    public final static int NO_COLLISION_DETECTED = 12;
    public final static int NOTIFY_DEATH = 13;
    public final static int COLLISION_WITHOUT_EFFECT = 14;
    public final static int KILL_SHOT_AND_ALIEN = 15;
    public final static int LOGIC_DONE = 16;
}
