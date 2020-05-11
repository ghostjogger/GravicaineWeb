package com.mygdx.game.entities;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by stephenball on 21/03/2017.
 */

public class Entity extends Drawable {


    private double dx,dy;
    private double speed;
    private boolean visible;
    public boolean killed;


    public Entity(Texture t, double x, double y) {
        super(x, y);
        this.image = t;
        this.visible = true;
        this.killed = false;
    }

    @Override
    public void update(double delta) {

    }

    public boolean isVisible(){
        return visible;
    }

    public void setVisible(boolean visible){
        this.visible = visible;
    }

    public void kill(){
        this.killed = true;
    }



    public Texture getImage(){
        return image;
    }
}
