package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by stephenball on 07/04/2017.
 */

public class FuelEntity extends Entity{

    private double dx,dy,speed,fuel_amount;

    public FuelEntity(Texture t, double x, double y, double dx, double dy, double speed, double amount) {
        super(t, x, y);
        this.dx = dx;
        this.dy = dy;
        this.speed = speed;
        this.fuel_amount = amount;

    }

    @Override
    public void update(double delta) {
        this.x += dx * speed * delta;
        this.y += dy * speed * delta;

        if(y < -50){
            setVisible(false);
        }
    }

    public double getFuelAmount(){
        return fuel_amount;
    }
}
