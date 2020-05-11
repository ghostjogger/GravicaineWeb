package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.mygdx.game.managers.GameAssetManager;

/**
 * Created by stephenball on 30/03/2017.
 */

public class Explosion extends Entity {

    public Animation explosions;
    public float frameTime;
    private GameAssetManager gam;

    public Explosion(Texture t, double x, double y, GameAssetManager gam) {
        super(t, x, y);
        this.gam = gam;
        this.frameTime = 0;
        this.explosions = new Animation((float)0.08, gam.blueexplosionsprites);
        setVisible(true);
    }


    @Override
    public void update(double delta)
    {
        frameTime += delta;



            if(explosions.isAnimationFinished(frameTime)){
                setVisible(false);
            }
    }
}
