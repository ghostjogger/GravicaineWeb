package com.mygdx.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by stephenball on 21/03/2017.
 */

public class GameAssetManager {

    public Texture playerShipImage;
    public Texture right_thruster;
    public Texture left_thruster;

    public Texture hud_lives;

    public Texture barrier_0_left;
    public Texture barrier_0_right;
    public Texture barrier_1_left;
    public Texture barrier_1_right;
    public Texture barrier_2_left;
    public Texture barrier_2_right;
    public Texture barrier_3_left;
    public Texture barrier_3_right;
    public Texture barrier_4_left;
    public Texture barrier_4_right;
    public Texture barrier_5_left;
    public Texture barrier_5_right;
    public Texture barrier_6_left;
    public Texture barrier_6_right;

    public Texture background;


    public Texture playerProjectileImage;
    public TextureRegion[][] playerPieces;
    public TextureAtlas effects;
    public Array<Sprite> redexplosionsprites;
    public Array<Sprite> blueexplosionsprites;
    public Array<Sprite> galaxysprites;
    public Array<Sprite> thrustersprites;
    public Array<Sprite> asteroidsprites;

    public Array<Texture> asteroidTextures;
    public Array<Texture> explosionTextures;
    public Array<Texture> leftBarriers;
    public Array<Texture> rightBarriers;

    public Texture fuelUp;





    public GameAssetManager(){

        this.playerShipImage = new Texture(Gdx.files.internal("graphics/playerShip.png"));
        left_thruster = new Texture(Gdx.files.internal("graphics/l_thruster_small.png"));
        right_thruster = new Texture(Gdx.files.internal("graphics/r_thruster_small.png"));
        hud_lives = new Texture(Gdx.files.internal("graphics/playership_lives_hud.png"));
        this.playerProjectileImage = new Texture(Gdx.files.internal("graphics/player_bullet.png"));


        barrier_0_left = new Texture(Gdx.files.internal("graphics/Obstacles/barrier_0_left_new.png"));
        barrier_0_right = new Texture(Gdx.files.internal("graphics/Obstacles/barrier_0_right_new.png"));
        barrier_1_left = new Texture(Gdx.files.internal("graphics/Obstacles/barrier_1_left_new.png"));
        barrier_1_right = new Texture(Gdx.files.internal("graphics/Obstacles/barrier_1_right_new.png"));
        barrier_2_left = new Texture(Gdx.files.internal("graphics/Obstacles/barrier_2_left_new.png"));
        barrier_2_right = new Texture(Gdx.files.internal("graphics/Obstacles/barrier_2_right_new.png"));
        barrier_3_left = new Texture(Gdx.files.internal("graphics/Obstacles/barrier_3_left_new.png"));
        barrier_3_right = new Texture(Gdx.files.internal("graphics/Obstacles/barrier_3_right_new.png"));
        barrier_4_left = new Texture(Gdx.files.internal("graphics/Obstacles/barrier_4_left_new.png"));
        barrier_4_right = new Texture(Gdx.files.internal("graphics/Obstacles/barrier_4_right_new.png"));
        barrier_5_left = new Texture(Gdx.files.internal("graphics/Obstacles/barrier_5_left_new.png"));
        barrier_5_right = new Texture(Gdx.files.internal("graphics/Obstacles/barrier_5_right_new.png"));
        barrier_6_left = new Texture(Gdx.files.internal("graphics/Obstacles/barrier_6_left_new.png"));
        barrier_6_right = new Texture(Gdx.files.internal("graphics/Obstacles/barrier_6_right_new.png"));

        leftBarriers = new Array<Texture>();
        rightBarriers = new Array<Texture>();

        leftBarriers.add(barrier_0_left);
        leftBarriers.add(barrier_1_left);
        leftBarriers.add(barrier_2_left);
        leftBarriers.add(barrier_3_left);
        leftBarriers.add(barrier_4_left);
        leftBarriers.add(barrier_5_left);
        leftBarriers.add(barrier_6_left);

        rightBarriers.add(barrier_0_right);
        rightBarriers.add(barrier_1_right);
        rightBarriers.add(barrier_2_right);
        rightBarriers.add(barrier_3_right);
        rightBarriers.add(barrier_4_right);
        rightBarriers.add(barrier_5_right);
        rightBarriers.add(barrier_6_right);

        this.background = new Texture(Gdx.files.internal("maps/Level_1_complete.png"));


        fuelUp = new Texture(Gdx.files.internal("graphics/fuelUp.png"));
        TextureRegion t = new TextureRegion(playerShipImage);
        playerPieces = t.split(32,1);



        asteroidTextures = new Array<Texture>();
        Texture asteroid1 = new Texture(Gdx.files.internal("graphics/asteroid_1.png"));
        Texture asteroid2 = new Texture(Gdx.files.internal("graphics/asteroid_2.png"));
        Texture asteroid3 = new Texture(Gdx.files.internal("graphics/asteroid_3.png"));
        asteroidTextures.add(asteroid1);
        asteroidTextures.add(asteroid2);
        asteroidTextures.add(asteroid3);

        effects = new TextureAtlas(Gdx.files.internal("graphics/effect_sprites.txt"));
        redexplosionsprites = effects.createSprites("redxploder");
        blueexplosionsprites = effects.createSprites("bluexploder");
        galaxysprites = effects.createSprites("galaxy");
        thrustersprites = effects.createSprites("thruster");
        explosionTextures = getTextures(redexplosionsprites);
        //asteroidsprites = effects.createSprites("asteroid");




    }

    public Array <Texture> getTextures(Array<Sprite> sprites){

        Array <Texture> regions = new Array<Texture>();

        for(Sprite s: sprites){
            Texture t = s.getTexture();

            regions.add(t);
        }

        return regions;
    }
}
