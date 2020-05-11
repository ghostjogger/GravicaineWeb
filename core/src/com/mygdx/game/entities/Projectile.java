package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.screens.GameScreen;

/**
 * Created by stephenball on 28/03/2017.
 */

public abstract class Projectile extends Entity {

    protected double dx,dy,xOrigin,yOrigin,speed,damage,angle;
    protected boolean active;
    protected Texture image;

    protected Rectangle me;
    protected Rectangle him;
    protected GameScreen screen;



    public Projectile(Texture t, double x, double y, double dx, double dy, double speed,GameScreen screen) {
        super(t, x, y);
        this.xOrigin = x;
        this.yOrigin = y;
        this.dx = dx;
        this.dy = dy;
        this.speed = speed;
        this.setVisible(true);
        this.screen = screen;
    }

    @Override
    public void update(double delta) {

    }


    public boolean isCollision(Entity e){

        me = new Rectangle((int)x,(int)y,image.getWidth(),image.getHeight());
        him = new Rectangle((int)e.getX(),(int)e.getY(),e.image.getWidth(),e.image.getHeight());

        return me.overlaps(him);
    }

    private double distance() {

        double dist = 0;

        dist = Math.sqrt(Math.abs((xOrigin - x) * (xOrigin - x) + (yOrigin - y) * (yOrigin - y)));

        return dist;
    }
}
