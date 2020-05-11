package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by stephenball on 19/03/2017.
 */
public abstract class Drawable {

    protected double x;
    protected double y;
    protected Texture image;

    public Drawable(double x, double y) {
        this.x = x;
        this.y = y;
    }



    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }


    public abstract void update(double delta);

}