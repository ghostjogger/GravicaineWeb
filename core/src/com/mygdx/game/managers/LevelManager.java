package com.mygdx.game.managers;

/**
 * Created by stephenball on 20/03/2017.
 */


public class LevelManager {

    private static LevelManager uniqueInstance;



    public static enum LEVELS {
        EASY, MEDIUM, HARD;

        public LEVELS increase() {
            return values()[Math.min(2, (ordinal() + 1) % values().length)];
        }
        public LEVELS decrease() {
            return  ( ( (ordinal() - 1) % values().length) < 0 ?
                    values()[2] : values()[((ordinal() - 1) % values().length)] );
        }
    }

    public static LEVELS level;

    public  LevelManager() {
        level = LEVELS.EASY;
    }

    public static LevelManager getUniqueInstance(){
        if (uniqueInstance == null) {
            uniqueInstance = new LevelManager();
        }
        return uniqueInstance;
    }

    public static void increaseLevel() {
        level = level.increase();
    }
    public static void decreaseLevel() {
        level = level.decrease();
    }

    public static LEVELS getLevel() {
        return level;
    }
    public static void resetLevel() {
        level = LEVELS.EASY;
    }
}
