package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Gravicaine;
import com.mygdx.game.entities.Fragment;
import com.mygdx.game.managers.LevelManager;
import com.mygdx.game.managers.ScoreManager;
import com.mygdx.game.managers.TitleAssetManager;
import com.mygdx.game.score.Score;

import java.util.Random;

public class MainMenuScreen implements Screen {
    final Gravicaine game;
    OrthographicCamera camera;
    Sound startSound;
    Sound titleSound;
    String[] LEVELS_MSGS = {"NORMAL", "HARD", "CRAZY"};


    private double time;
    private int bgY;

    private final int MENU_TITLE_ANIM = 0;
    private final int MENU_WAITING = 1;
    private TitleAssetManager tam;
    private int MENU_STATE;

    private float cam_yStart;

    private Array<Fragment> allFragments;
    private Array<Fragment> fragments;

    private Random random;



    public MainMenuScreen(final Gravicaine gam) {
        game = gam;
        tam = new TitleAssetManager();
        camera = new OrthographicCamera();
        time = 0;
        bgY = 0;
        camera.setToOrtho(false, 800, 480);
        cam_yStart = camera.position.y;
        random = new Random();
        MENU_STATE = MENU_TITLE_ANIM;
        allFragments = new Array<Fragment>();
        this.startSound = Gdx.audio.newSound(Gdx.files.internal("sounds/sound43.wav"));
        this.titleSound = Gdx.audio.newSound(Gdx.files.internal("sounds/sound62.wav"));
        setupFragments();
        startSound.play();


    }

    @Override
    public void render(float delta) {

        time += Gdx.graphics.getDeltaTime();


        game.batch.setProjectionMatrix(camera.combined);

        camera.update();
        renderBackground();
        scrollMap();


        switch(MENU_STATE){

            case(0):{


                animateTitle();
                if(time > 1.3){
                    MENU_STATE = MENU_WAITING;
                    titleSound.play();
                    time = 0;
                }

                break;
            }

            case(1):{

                if(time > 0.2){
                    checkLevelChange();
                    time = 0;
                }
                displayTitle();
                //displayLevelMenu();

                renderText();
                renderHighScores();
                if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
                    game.setScreen(new GameScreen(game));
                    dispose();
                }

                break;
            }

            default: break;

        }




    }
    public void checkLevelChange(){

        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            LevelManager.increaseLevel();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            LevelManager.decreaseLevel();
        }
    }

    public void renderText(){

        game.batch.begin();
        game.font14.setColor(Color.WHITE);
        game.font18.setColor(Color.YELLOW);
        game.font14.draw(game.batch, "PRESS ENTER TO BEGIN!!", 180, camera.position.y - 210);
        game.font18.draw(game.batch, "Fly through the gates, avoid ", 400, camera.position.y);
        game.font18.draw(game.batch, "Asteroids and collect Fuel!! ", 400, camera.position.y - 20);
        game.font18.draw(game.batch, "L arrow key - move left", 400, camera.position.y - 40);
        game.font18.draw(game.batch, "R arrow key - move right", 400, camera.position.y - 60);
        game.font18.draw(game.batch, "Space - fire projectile", 400, camera.position.y - 80);
        game.font18.draw(game.batch, "P - Pause game", 400, camera.position.y - 100);
        game.font18.draw(game.batch, "Gravity pulls left and right! ", 400, camera.position.y - 120);
        game.batch.end();

    }

    public void renderHighScores(){

        if(ScoreManager.high_scores.size() == 0){
            return;
        }
        else{
            ScoreManager.sortScores();
            game.batch.begin();
            game.font14.setColor(Color.CYAN);
            game.font14.draw(game.batch, "Top Gravicainers:", 50, camera.position.y + 40);
            game.font24.setColor(Color.WHITE);
            int i = 0;
            for(Score s: ScoreManager.high_scores){
                game.font24.draw(game.batch, s.name, 50, camera.position.y - (i*25));
                game.font24.draw(game.batch, String.valueOf(s.score), 150, camera.position.y - (i * 25));
                i++;
            }
            game.batch.end();
        }

    }

    public void scrollMap(){

        bgY--;
        if(bgY < -32){
            bgY = 0;
        }


    }

    public void renderBackground(){
        game.batch.begin();

            game.batch.draw(tam.background, 0,  camera.position.y - 240 + bgY);

        game.batch.end();
    }

    public void animateTitle(){


        game.batch.begin();
        for(Fragment f: allFragments){
            f.update(Gdx.graphics.getDeltaTime());
            game.batch.draw(f.getTexture(), (float)f.getX(),  camera.position.y - 240 + (float)f.getY() );
        }
        game.batch.end();

    }

    public void displayTitle() {
        game.batch.begin();

        for(Fragment f: allFragments){
            f.update(Gdx.graphics.getDeltaTime());
            game.batch.draw(f.getTexture(), (float)f.getX(),  camera.position.y - 240 + (float)f.getY() );
        }
        game.batch.end();
    }

    public void displayLevelMenu(){
        game.batch.begin();
        game.font14.setColor(Color.PINK);
        game.font14.draw(game.batch, "SELECT DIFFICULTY:", 600, camera.position.y - 100);
        for (int i = 0; i < 3; i++ ) {
            game.font14.setColor(Color.WHITE);

            if (LevelManager.getLevel().ordinal() == i) {
                game.font14.setColor(Color.RED);
            }
            game.font14.draw(game.batch, LEVELS_MSGS[i], 640, camera.position.y - 100 - 20 - (i * 20));

        }
        game.batch.end();
    }



    public void setupFragments(){

        for(int i = 0; i < tam.titleCharPieces.size; i++){
            TextureRegion[][] t = tam.titleCharPieces.get(i);
            for(int j = 0; j < 72/4; j++){

                Fragment f = new Fragment(t[j][0], random.nextFloat() * 800,
                        random.nextFloat() * 480, 10 + (i * 72), 480 - 32  - (j * 4), 0, 50);
                allFragments.add(f);
            }
        }

        for(int i = 0; i < tam.sigCharPieces.size; i++){
            TextureRegion[][] t = tam.sigCharPieces.get(i);
            for(int j = 0; j < 54/4; j++){

                Fragment f = new Fragment(t[j][0], random.nextFloat() * 800,
                        random.nextFloat() * 480, 310 + (i * 30), 480 - 110  - (j * 4), 0, 50);
                allFragments.add(f);
            }
        }


    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
}