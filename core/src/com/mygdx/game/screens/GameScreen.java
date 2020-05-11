package com.mygdx.game.screens;

/**
 * Created by stephenball on 16/03/2017.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Gravicaine;
import com.mygdx.game.entities.Asteroid;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.entities.Explosion;
import com.mygdx.game.entities.Fragment;
import com.mygdx.game.entities.FuelEntity;
import com.mygdx.game.entities.Obstacle;
import com.mygdx.game.entities.PlayerEntity;
import com.mygdx.game.entities.Projectile;
import com.mygdx.game.managers.GameAssetManager;
import com.mygdx.game.managers.LevelManager;
import com.mygdx.game.managers.ScoreManager;
import com.mygdx.game.score.Score;

import java.util.Random;

public class GameScreen implements Screen {

    final Gravicaine game;
    public OrthographicCamera camera;
    private Sound startSound;
    private Sound explosionSound;
    private Sound fuelSound;
    private Sound thrustSound;
    private Sound asteroidExplosionSound;
    private Music gameSoundtrack;
    public Sound player_shoot;

    public ShapeRenderer shapeRenderer;


    private int playerXStart, playerYStart;

    private final int GAME_START_ANIM = 0;
    private final int GAME_PLAYING = 1;
    private final int GAME_PLAYER_DEATH = 2;
    private final int GAME_PAUSED = 3;
    private final int GAME_OVER = 4;
    private final int GAME_HIGH_SCORE = 5;
    private final int GAME_SUCCESS = 6;
    private int GAME_STATE;

    private double difficulty;

    public Animation explosion;
    public Animation thruster_down;

    private boolean leftThrust;
    private boolean rightThrust;

    public GameAssetManager gam;
    private float cam_yStart;
    private float elapsedTime = 0;
    private float time = 0;
    private float thrustTime = 0;
    private float hostileTime = 0;
    private float fuelTime = 0;
    private float ObstacleTime = 4;
    private float ObstaclePauseTime;

    public Array<Sprite> explosionSprites;
    private PlayerEntity playerShip;
    private Array<Fragment> playerFragments;
    private Array<Fragment> finishFragments;
    private Array<Fragment> fragments;
    public Array<Projectile> projectiles;
    public Array<Explosion> exploders;
    public Array<Obstacle> obstacles;

    public Character[] name = {'A', 'A', 'A'};
    private boolean is_entering_score;
    private boolean createNewScore;
    private int new_Highscore;
    private int score_index;


    private Random random;
    private Random mapLeftRandom;
    private Random mapRightRandom;

    public Array<Entity> hostiles;


    private LevelManager level_manager;

    private double player_Thrust = 0.18; // 0.25
    private double player_gravity = 0.02; // 0.05

    private  double regular_fuel_burn = -0.05;
    private  double thruster_fuel_burn = -0.10;
    private  double initial_fuel = 200;
    private double fuel_burn;


    private  int barrier_spacing = 430;
    private int barrier_ticker = 0;
    private int barrier_count;
    private Array<Integer> barrierSequence;
    private float scrollSpeed;

    public int bgY;


    private SpriteBatch batch;

    public TextureRegion[][] finishChars;
    public Array<TextureRegion[][]> finishCharPieces;
    private Texture finishImage;
    private TextureRegion finishImageRegion;

    public GameScreen (final Gravicaine game){


        this.game = game;
        gam = new GameAssetManager();
        playerShip = new PlayerEntity(gam.playerShipImage, 400,50,5,0,3, this);
        shapeRenderer = new ShapeRenderer();


        this.is_entering_score = false;

        bgY = 0;


        //setup finish animation
        this.finishImage = new Texture(Gdx.files.internal("graphics/GravicaineCongrats.png"));
        this.finishImageRegion = new TextureRegion(finishImage);
        this.finishCharPieces = new Array<TextureRegion[][]>();
        this.finishFragments = new Array<Fragment>();
        this.finishChars = finishImageRegion.split(72,72);

        for (int i = 0; i < 11; i++){
            TextureRegion r = new TextureRegion(finishChars[0][i]);
            TextureRegion[][] t = r.split(72,4);
            finishCharPieces.add(t);
        }


        this.playerXStart = 400;
        this.playerYStart = 50;

        //set up camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);



        random = new Random();
        mapLeftRandom = new Random(15);
        mapRightRandom = new Random(5);

        barrierSequence = new Array<Integer>();
        for(int i = 0; i < 600; i++){
            barrierSequence.add(mapLeftRandom.nextInt(6));
        }
        barrier_count = 0;

        playerShip.setGravity(random.nextBoolean() ? -player_gravity : player_gravity);
        playerShip.setFuel(initial_fuel);
        fuel_burn = regular_fuel_burn;


        //set up animations
        explosion = new Animation((float)0.08, gam.redexplosionsprites);
        thruster_down = new Animation((float)0.05, gam.thrustersprites);

        //set up sounds
        this.startSound = Gdx.audio.newSound(Gdx.files.internal("sounds/sound0.wav"));
        this.explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        this.player_shoot = Gdx.audio.newSound(Gdx.files.internal("sounds/laser6.mp3"));
        this.fuelSound = Gdx.audio.newSound(Gdx.files.internal("sounds/Powerup.wav"));
        this.thrustSound = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx_movement_dooropen1.wav"));
        this.asteroidExplosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx_exp_short_hard3.wav"));
        this.gameSoundtrack = Gdx.audio.newMusic(Gdx.files.internal("sounds/gameSoundtrack.mp3"));

        batch = new SpriteBatch();


        //set up initial game state
        GAME_STATE = GAME_START_ANIM;
        scrollSpeed = 2;

        //set up game arrays
        playerFragments = new Array<Fragment>();
        hostiles = new Array<Entity>();
        projectiles = new Array<Projectile>();
        exploders = new Array<Explosion>();
        obstacles = new Array<Obstacle>();

        setupFinishFragments();
        setupPlayerFragments();
        setupLevel();


    }
    @Override
    public void show() {
        
    }

    public void setupLevel(){



    }


    @Override
    public void render(float delta) {


        time += Gdx.graphics.getDeltaTime();
        thrustTime += Gdx.graphics.getDeltaTime();
        hostileTime += Gdx.graphics.getDeltaTime();
        ObstacleTime += Gdx.graphics.getDeltaTime();
        fuelTime += Gdx.graphics.getDeltaTime();



        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);
        renderBackground();


        switch(GAME_STATE){

            case(0):{

                renderProjectiles();
                renderExploders();
                renderHostiles();
                renderObstacles();
                renderHud();

                if(time == Gdx.graphics.getDeltaTime()){
                    startSound.play();
                }
                animatePlayerFragments();
                if(time > 0.6){
                    GAME_STATE = GAME_PLAYING;
                    gameSoundtrack.setLooping(true);
                    gameSoundtrack.play();
                    time = 0;
                }

                break;
            }

            case(1):{

                if(!playerShip.dead) {

                    if(playerShip.getScore() == 500){
                        GAME_STATE = GAME_SUCCESS;
                        time = 0;
                        break;
                    }

                    if (Gdx.input.isKeyPressed(Keys.P) && time > 0.3) {
                        GAME_STATE = GAME_PAUSED;
                        time = 0;
                        break;
                    }

                    if(Gdx.input.isKeyPressed(Keys.G)){
                        GAME_STATE = GAME_OVER;
                        break;
                    }

                    playerShip.update(Gdx.graphics.getDeltaTime());

//                    currentPlayerCell = mapObstacleLayer.getCell((int) playerShip.getX()/32,
//                            (int) (camera.position.y - 240 + 32 +  playerShip.getY())/32);
//
//
//                    if(currentPlayerCell != null){
//                        currentPlayerTile = currentPlayerCell.getTile();
//                    }


                    renderPlayer();



                    updateObstacles();
                    renderObstacles();

                    updateHostiles();
                    renderHostiles();

                    updateProjectiles();
                    renderProjectiles();

                    updateExploders();
                    renderExploders();

                    renderHud();

                    checkObjectCollisions();

                    if(playerShip.getFuel() > 0) {
                        playerShip.changeFuel(fuel_burn);
                    }


                    if (Gdx.input.isKeyPressed(Keys.SPACE)) {
                        playerShip.updateShooting();
                    }
                    if (Gdx.input.isKeyPressed(Keys.RIGHT) && playerShip.getFuel() > 0) {
                        playerShip.setXSpeed(player_Thrust);
                        leftThrust = true;
                        fuel_burn = thruster_fuel_burn;
                        //thrustSound.play();
                    }
                    else{

                        leftThrust = false;
                        fuel_burn = regular_fuel_burn;
                    }
                    if (Gdx.input.isKeyPressed(Keys.LEFT) && playerShip.getFuel() > 0) {
                        playerShip.setXSpeed(-player_Thrust);
                        rightThrust = true;
                        fuel_burn = thruster_fuel_burn;
                        //thrustSound.play();
                    }
                    else{
                        rightThrust = false;
                        fuel_burn = regular_fuel_burn;
                    }

                    if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
                        game.setScreen(new MainMenuScreen(game));
                        gameSoundtrack.stop();
                        dispose();

                    }



                    if(hostileTime > 5.0){
                        hostileTime = 0;
                        Asteroid a = new Asteroid(gam.asteroidTextures.get(random.nextInt(3)),32 + random.nextFloat() * 736,500,0,-10,20);
                        hostiles.add(a);
                    }

                    if(fuelTime > 3.0){
                        fuelTime = 0;
                        FuelEntity f = new FuelEntity(gam.fuelUp,32 + random.nextFloat() * 736,500,0,-10,30,50);
                        hostiles.add(f);
                    }

                    if((barrier_ticker >= barrier_spacing)  && (barrier_count < barrierSequence.size)){
                        barrier_ticker = 0;
                        barrier_count++;

                        Obstacle ol;
                        Obstacle or;

                            ol = new Obstacle(gam.leftBarriers.get(barrierSequence.get(barrier_count)), 32,550,0,-15,10);
                            or = new Obstacle(gam.rightBarriers.get(5 - barrierSequence.get(barrier_count)),
                                    (800 - 32 - (gam.rightBarriers.get(5 - barrierSequence.get(barrier_count)).getWidth())),550,0,-15,10);

                        obstacles.add(ol);
                        obstacles.add(or);
                    }
                    scrollMap();
                    break;
                }
                else{
                    time = 0;
                    GAME_STATE = GAME_PLAYER_DEATH;
                }


            }

            case(2):{



                renderObstacles();
                renderProjectiles();
                renderExploders();
                renderHostiles();
                renderHud();

                if(playerShip.getLives() != 0 && explosion.isAnimationFinished(elapsedTime)) {
                    renderEnter();
                }


                if(time == Gdx.graphics.getDeltaTime()){
                    explosionSound.play();
                }


                if(!explosion.isAnimationFinished(elapsedTime)) {
                    game.batch.begin();
                    elapsedTime += Gdx.graphics.getDeltaTime();
                    game.batch.draw((TextureRegion) explosion.getKeyFrame(elapsedTime, false), (float) playerShip.getX() - 16,
                            camera.position.y - 240 + (float) playerShip.getY() - 16);
                    game.batch.end();

                }
                else{
                    if(playerShip.getLives() == 0){
                        GAME_STATE = GAME_OVER;
                        time = 0;
                        break;
                    }
                }
                if (Gdx.input.isKeyPressed(Keys.ENTER) && playerShip.getLives() != 0) {
                    GAME_STATE = GAME_START_ANIM;
                    setupPlayerFragments();
                    playerShip.dead = false;
                    playerShip.resetXSpeed(0);
                    playerShip.setX(playerXStart);
                    playerShip.setFuel(initial_fuel);
                    time = 0;
                    elapsedTime = 0;
                    hostiles.clear();
                    projectiles.clear();
                    //currentPlayerCell = null;
                    //currentPlayerTile = null;
                    camera.translate(0,-376);

                    if(camera.position.y < 240){
                        camera.translate(0, 240 - camera.position.y);
                    }

                    obstacles.clear();
                    barrier_count --;

                    exploders.clear();

                }



                break;
            }

            case(3):{


                renderObstacles();
                renderPlayer();
                renderProjectiles();
                renderExploders();
                renderHostiles();
                renderPaused();
                renderHud();

                if (Gdx.input.isKeyPressed(Keys.P) && time > 0.3 ) {
                    GAME_STATE = GAME_PLAYING;
                    time = 0;
                    break;
                }


                break;
            }

            case(4):{



                renderObstacles();
                renderProjectiles();
                renderExploders();
                renderHostiles();
                renderGameOver();
                renderHud();

                if(ScoreManager.updateHighScore(playerShip.getScore()) && time > 3.0){
                    GAME_STATE = GAME_HIGH_SCORE;
                    is_entering_score = true;
                    createNewScore = true;
                    break;

                }

                else if (time > 3.0) {
                    game.setScreen(new MainMenuScreen(game));
                    dispose();
                }


                break;
            }

            case(5):{


                if(is_entering_score) {
                    renderObstacles();
                    renderProjectiles();
                    renderExploders();
                    renderHostiles();
                    renderHud();

                    if(createNewScore) {
                        new_Highscore  = (int) playerShip.getScore();
                        score_index = 0;
                        createNewScore = false;
                    }



                    if(!Gdx.input.isKeyPressed(Keys.ENTER)) {
                        renderHighScoreBackground();
                        renderHighScoreEntry(name, score_index, new_Highscore);

                        if(time > 0.3){

                            if(Gdx.input.isKeyPressed(Keys.LEFT)){

                                if (score_index <= 0) {
                                    score_index = name.length-1;
                                }
                                else {
                                    score_index = (score_index-1) % name.length;
                                }
                                time = 0;
                                break;
                            }

                            if(Gdx.input.isKeyPressed(Keys.RIGHT)){

                                score_index = (score_index+1) % name.length;
                                time = 0;
                                break;
                            }

                            if(Gdx.input.isKeyPressed(Keys.UP)){

                                if (name[score_index] < 'Z') {
                                    name[score_index]++;
                                }
                                time = 0;
                                break;
                            }

                            if(Gdx.input.isKeyPressed(Keys.DOWN)){

                                if (name[score_index] > 'A') {
                                    name[score_index]--;
                                }
                                time = 0;
                                break;
                            }


                        }
                    }


                    else {
                        String newName = new String("" + name[0].charValue() + name[1].charValue() + name[2].charValue());
                        Score s = new Score(new_Highscore, newName);
                        ScoreManager.high_scores.add(s);
                        gameSoundtrack.stop();
                        game.setScreen(new MainMenuScreen(game));
                        dispose();
                    }


                    break;
                }





            }

            case(6):{

                if(time > 10){
                    GAME_STATE = GAME_OVER;
                    time = 0;
                    break;
                }

                renderObstacles();
                renderProjectiles();
                renderExploders();
                renderHostiles();
                renderHud();
                animateFinish();


                break;
            }

            default: break;

        }



    }

    public void renderHighScoreBackground(){

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.02f);
        shapeRenderer.rect(100,camera.position.y, 600, 200);
        shapeRenderer.end();
    }

    public void renderEnter(){

        game.batch.begin();
        game.font14.setColor(Color.CYAN);
        game.font14.draw(game.batch, "PRESS ENTER TO RESTART", 180, camera.position.y - 200);
        game.batch.end();

    }

    public void renderHighScoreEntry(Character[] name, int scoreIndex, int score){

        game.batch.begin();
        game.font24.setColor(Color.RED);
        game.font24.draw(game.batch, "Congratulations! High Score!", 110, camera.position.y + 160);
        game.font24.setColor(Color.CYAN);
        game.font24.draw(game.batch, String.valueOf(score), 380, camera.position.y + 120);
        game.font24.setColor(Color.WHITE);
        for(int i = 0; i < 3; i++){
            game.font24.draw(game.batch, name[i].toString(), 330 + ( i * 60), camera.position.y + 80);
            if(i == scoreIndex){
                game.font24.draw(game.batch, "-", 334 + ( i * 60), camera.position.y + 65);
            }
        }

        game.batch.end();

    }

    public void scrollMap(){
        camera.translate(0,scrollSpeed);
        barrier_ticker += scrollSpeed;

        bgY -= 2;
        if(bgY < -32){
            bgY = 0;
        }

    }

    public void renderHud(){

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 1, 1, 0.02f);
        shapeRenderer.rect(0, 0, 800, camera.position.y - 240 + 20);
        shapeRenderer.setColor(0, 0.8f, 0, 0.02f);
        if(playerShip.getGravity() < 0){
            shapeRenderer.rect(0, 0, 20, camera.position.y - 240 + 20);
        }
        else{
            shapeRenderer.rect(780, 0, 20, camera.position.y - 240 + 20);
        }
        shapeRenderer.setColor(1f, 0, 0, 0.02f);
        shapeRenderer.rect(300, 0, (float)playerShip.getFuel(), camera.position.y - 240 + 20);

        shapeRenderer.end();


        game.batch.begin();
        game.font24.setColor(Color.BLACK);
        game.font24.draw(game.batch, "E", 270,camera.position.y - 222 );
        game.font24.draw(game.batch, "F", 270 + 240,camera.position.y - 222 );
        game.font44.setColor(Color.CYAN);
        game.font44.draw(game.batch, String.valueOf((int)playerShip.getScore()), 710, camera.position.y + 44);

        if(playerShip.getLives() >= 5 ){
            for(int i = 0; i < 5; i++){
                game.batch.draw(gam.hud_lives, 30 + ( i * 25), camera.position.y - 240);
            }
        }
        else{
            for(int i = 0; i < playerShip.getLives(); i++){
                game.batch.draw(gam.hud_lives, 30 + ( i * 25), camera.position.y - 240);
            }
        }

        game.batch.end();
    }

    public void renderGameOver(){
        game.batch.begin();
        game.font24.setColor(Color.RED);
        game.font24.draw(game.batch, "GAME OVER!!", camera.position.x - 100, camera.position.y);
        game.batch.end();
    }

    public void renderPaused(){
        game.batch.begin();
        game.font24.setColor(Color.RED);
        game.font24.draw(game.batch, "PAUSED!", camera.position.x - 80, camera.position.y);
        game.batch.end();
    }

    public void renderPlayer(){
        game.batch.begin();
        game.batch.draw(playerShip.getImage(),(float)playerShip.getX(),camera.position.y - 240 + (float)playerShip.getY());
        game.batch.draw( (TextureRegion)thruster_down.getKeyFrame(thrustTime, true),(float)playerShip.getX(),
                camera.position.y - 240 + (float)playerShip.getY() - 40);
        if(leftThrust){
            game.batch.draw(gam.left_thruster,(float)playerShip.getX() - 15,camera.position.y - 240 + (float)playerShip.getY() + 13);
        }
        if(rightThrust){
            game.batch.draw(gam.right_thruster,(float)playerShip.getX() + 30,camera.position.y - 240 + (float)playerShip.getY() + 13);
        }
        game.batch.end();
    }

    public void setupPlayerFragments(){
        for(int j = 0; j < 32/1; j++){

            Fragment f = new Fragment(gam.playerPieces[j][0], random.nextFloat() * 800,
                    random.nextFloat() * 480, playerXStart, playerYStart, 0, 25);
            playerFragments.add(f);
        }
    }

    public void animatePlayerFragments(){
        game.batch.begin();
        for(Fragment f: playerFragments){
            f.update(Gdx.graphics.getDeltaTime());
            game.batch.draw(f.getTexture(), (float)f.getX(),  camera.position.y - 240 + (float)f.getY() );
        }
        game.batch.end();
    }

    public void renderBackground(){
        game.batch.begin();

        game.batch.draw(gam.background, 0,  camera.position.y - 240 + bgY);

        game.batch.end();
    }

    public void updateHostiles(){

        for(Entity e: hostiles){

            if(playerShip.isCollision(e)){

                if(e instanceof Asteroid) {
                    playerShip.kill();
                    //exploders.add(new Explosion(null, e.getX(), e.getY(), gam));
                    //hostiles.removeValue(e, true);
                    return;
                }

                else if(e instanceof FuelEntity){

                    playerShip.changeFuel(((FuelEntity) e).getFuelAmount());
                    if(playerShip.getFuel() > initial_fuel){
                        playerShip.setFuel(initial_fuel);
                    }
                    hostiles.removeValue(e, true);
                    fuelSound.play();

                }
            }

            if(e.killed && e instanceof Asteroid){
                e.setVisible(false);
                asteroidExplosionSound.play();
            }

            if(!e.isVisible()){
                    hostiles.removeValue(e, true);
            }
            else{
                e.update(Gdx.graphics.getDeltaTime());

            }
        }

    }

    public void renderHostiles(){
        game.batch.begin();

        for(Entity e: hostiles){
            game.batch.draw(e.getImage(),(float)e.getX(),camera.position.y - 240 + (float)e.getY());
        }

        game.batch.end();
    }

    public void checkObjectCollisions(){

//        for(Rectangle r: mapObjectRects){
//            if(playerShip.isCollision(r)){
//                playerShip.kill();
//            }
//        }

    }

    public void updateProjectiles(){

        for(Projectile p: projectiles){


            if(!p.isVisible()){
                projectiles.removeValue(p,true);
            }
            else{
                p.update(Gdx.graphics.getDeltaTime());

            }
        }

    }

    public void renderProjectiles(){
        game.batch.begin();

        for(Projectile p: projectiles){
            game.batch.draw(p.getImage(),(float)p.getX(),camera.position.y - 240 + (float)p.getY());
        }
        game.batch.end();

    }

    public void updateExploders(){


        for(Explosion p: exploders){


            if(!p.isVisible()){
                exploders.removeValue(p,true);
            }
            else{
                p.update(Gdx.graphics.getDeltaTime());

            }
        }



    }

    public void renderExploders(){
        game.batch.begin();

        for(Explosion p: exploders){
            game.batch.draw((TextureRegion) p.explosions.getKeyFrame(p.frameTime,false),(float)p.getX()
                    ,camera.position.y - 240 + (float)p.getY());
        }

        game.batch.end();
    }

    public void updateObstacles(){

        for(Obstacle e:obstacles){

            if(playerShip.isCollision(e)){
                playerShip.kill();
                return;
            }

            else if (!e.isVisible()){
                obstacles.removeValue(e,true);
                playerShip.changeScore(0.5);
            }

            else{
                e.update(Gdx.graphics.getDeltaTime());
            }

        }

    }


    public void renderObstacles(){

        game.batch.begin();

        for(Entity e: obstacles){
            game.batch.draw(e.getImage(),(float)e.getX(),camera.position.y - 240 + (float)e.getY());
        }

        game.batch.end();
    }

    public void animateFinish(){


        game.batch.begin();
        for(Fragment f: finishFragments){
            f.update(Gdx.graphics.getDeltaTime());
            game.batch.draw(f.getTexture(), (float)f.getX(),  camera.position.y - 240 + (float)f.getY() );
        }
        game.batch.end();

    }

    public void setupFinishFragments(){

        for(int i = 0; i < finishCharPieces.size; i++){
            TextureRegion[][] t = finishCharPieces.get(i);
            for(int j = 0; j < 72/4; j++){

                Fragment f = new Fragment(t[j][0], random.nextFloat() * 800,
                        random.nextFloat() * 480, 10 + (i * 72), 480 - 32  - (j * 4), 0, 50);
                finishFragments.add(f);
            }
        }


    }



    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
