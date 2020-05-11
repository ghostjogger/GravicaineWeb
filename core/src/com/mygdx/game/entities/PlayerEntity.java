package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.score.Score;
import com.mygdx.game.screens.GameScreen;





/**
 * Created by stephenball on 22/03/2017.
 */

public class PlayerEntity extends Entity{

    private double dx,dy,gravity,fuel_level;
    public boolean dead;
    private int lives;
    private double score;
    private int fireRate;
    private GameScreen screen;
    Rectangle me;
    Rectangle him;

    public PlayerEntity(Texture t, double x, double y, int lives, GameScreen screen) {
        super(t, x, y);
        this.gravity = 0;
        this.lives = lives;
        this.dead = false;
        this.score = 0;
        this.screen = screen;
        this.fireRate = PlayerProjectile.FIRE_RATE;

    }

    public PlayerEntity(Texture t, double x, double y, int lives, double dx, double dy, GameScreen screen) {
        super(t, x, y);
        this.dx = dx;
        this.dy = dy;
        this.gravity = 0;
        this.lives = lives;
        this.dead = false;
        this.score = 0;
        this.screen = screen;
        this.fireRate = PlayerProjectile.FIRE_RATE;
    }

    @Override
    public void update(double delta) {

        this.dx += gravity;
        this.fireRate -= delta;

        this.x += dx;

        if(x < 32){
            x = 32;
            kill();
        }
        else if(x > 800 - 64){
            x = 800 - 64;
            kill();
        }

        if(x < 400 - 16){
            if(gravity > 0){
                gravity = -gravity;
            }
        }
        else if(x >= 400 - 16){
            if(gravity < 0){
                gravity = -gravity;
            }
        }

//        if(screen.currentPlayerTile != null) {
//
//            System.out.println("not null");
//
//            if (screen.currentPlayerTile.getProperties().containsKey("Fatal")) {
//                System.out.println("fatal");
//                kill();
//            }
//        }

    }


    public void updateShooting(){
        if( fireRate <= 0){

            PlayerProjectile p = new PlayerProjectile(this.x + 12,this.y + 28,0,1,500,screen);
            screen.projectiles.add(p);
            fireRate = PlayerProjectile.FIRE_RATE;
            screen.player_shoot.play();
        }
    }
    public double getScore(){
        return score;
    }

    public void changeScore(double change){
        score += change;
    }

    public int getLives(){
        return lives;
    }

    public void setXSpeed(double dx){
        this.dx += dx;
    }

    public void resetXSpeed(double dx){
        this.dx = dx;
    }

    public void setYSpeed(double dx){
        this.dy += dy;
    }

    public void setGravity(double g){
        this.gravity = g;
    }

    public double getGravity(){
        return gravity;
    }

    public double getFuel(){
        return fuel_level;
    }

    public void setFuel(double fuel){
        this.fuel_level = fuel;
    }

    public void changeFuel(double amount){
        this.fuel_level += amount;
    }

    public void kill(){

        lives --;
        dead = true;
    }

    public boolean isCollision(Entity e){

        me = new Rectangle((int)x + 3,(int)y + 3,image.getWidth() - 3,image.getHeight() - 3);
        him = new Rectangle((int)e.getX(),(int)e.getY(),e.image.getWidth(),e.image.getHeight());

        return me.overlaps(him);
    }

    public boolean isCollision(Rectangle r){

        me = new Rectangle((float)x, (float)screen.camera.position.y - 240f + (float)y,image.getWidth() - 3,image.getHeight() - 3);
        him = r;

        return me.overlaps(him);
    }
}
