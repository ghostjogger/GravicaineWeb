package com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.screens.GameScreen;

/**
 * Created by stephenball on 28/03/2017.
 */

public class PlayerProjectile extends  Projectile {

    public static final int FIRE_RATE = 10;// higher is slower
    public static final Texture t = new Texture(Gdx.files.internal("graphics/player_bullet.png"));

    public PlayerProjectile( double x, double y, double dx, double dy, double speed, GameScreen screen) {
        super(t, x, y, dx, dy, speed,screen);
        this.image = new Texture(Gdx.files.internal("graphics/player_bullet.png"));
    }

    @Override
    public void update(double delta) {

        this.x += dx * speed * delta;
        this.y += dy * speed * delta;

        if(y > 500){
            this.setVisible(false);
        }

        for(Entity e: screen.hostiles){
            if(isCollision(e)){

                if(e instanceof Asteroid) {
                    this.setVisible(false);
                    //screen.explosionSprites.get(0).getTexture()
                    screen.exploders.add(new Explosion(null, e.getX(), e.getY(), screen.gam));
                    e.kill();
                    break;
                }

            }
        }
    }
}
