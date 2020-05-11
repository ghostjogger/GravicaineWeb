package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by stephenball on 19/03/2017.
 */

public class Fragment extends Drawable{

    private TextureRegion image;
    private double endx,endy,ypart;
    private double dx,dy;
    private double speed;
    private double time;


    // ypart is the y offset to enable rebuilding the whole object again with the fragments in the correct y position
    // dx and dy are the movement vectors to get from the start point to the end point
    public Fragment (TextureRegion i, double startx, double starty, double endx, double endy, double ypart, double time){
        super(startx, starty);
        this.image = i;
        this.endx = endx;
        this.endy = endy;
        this.ypart = ypart;
        this.time = time;
        setDirections();
        this.speed = distance()/time * 39.5;




    }



    @Override
    public void update(double delta) {

        y += dy * delta * speed;
        x += dx * delta * speed;

        if(dx > 0){
            if(x > endx){
                x = endx;
            }
        }
        if(dx < 0){
            if(x < endx) {
                x = endx;
            }
        }
        if(dy > 0){
            if(y > endy){
                y = endy;
            }
        }
        if(dy < 0){
            if(y < endy){
                y = endy;
            }
        }


    }

    public void setDirections() {


        // get differences between alien position and player position
        double px = endx - x;
        double py = endy - y;

        //work out angle between the two
        double d = Math.atan2(py, px);

        //work out the direction alien needs to travel to intercept player
        dx =  Math.cos(d);
        dy =  Math.sin(d);




    }

    public TextureRegion getTexture(){
        return image;
    }

    public double getYOffset(){
        return ypart;
    }

    private double distance() {

        double dist = 0;

        dist = Math.sqrt(Math.abs((endx - x) * (endx - x) + (endy - y) * (endy - y)));

        return dist;
    }

}