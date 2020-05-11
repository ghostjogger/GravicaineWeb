package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by stephenball on 26/03/2017.
 */

public class Asteroid extends Entity {

    private double dx,dy,speed;
    
    public Asteroid(Texture t, double x, double y, double dx, double dy, double speed) {
        super(t, x, y);
        this.dx = dx;
        this.dy = dy;
        this.speed = speed;
        
    }

    @Override
    public void update(double delta) {
        this.x += dx * speed * delta;
        this.y += dy * speed * delta;
        
        if(y < -50){
            setVisible(false);
        }
    }
    
}
