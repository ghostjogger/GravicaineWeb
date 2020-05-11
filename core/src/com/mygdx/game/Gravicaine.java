package com.mygdx.game;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.managers.LevelManager;
import com.mygdx.game.managers.ScoreManager;
import com.mygdx.game.screens.MainMenuScreen;

public class Gravicaine extends Game {

	public SpriteBatch batch;
	public BitmapFont font14;
	public BitmapFont font18;
    public BitmapFont font24;
	public BitmapFont font44;
	public LevelManager l;
	public ScoreManager s;



	public void create() {
		batch = new SpriteBatch();


		//generate true type font
		//FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Jellee-Roman.ttf"));
		//FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		//parameter.size = 14;
		//font14 = generator.generateFont(parameter); // font size 14 pixels
		font14 = new BitmapFont(Gdx.files.internal("fonts/newFont.fnt"));
		font18 = new BitmapFont(Gdx.files.internal("fonts/smaller.fnt"));
        //parameter.size = 24;
        //font24 = generator.generateFont(parameter); // font size 24 pixels
		font24 = new BitmapFont(Gdx.files.internal("fonts/newFont2.fnt"));
		//parameter.size = 44;
		//font44 = generator.generateFont(parameter); // font size 24 pixels
		font44 = new BitmapFont(Gdx.files.internal("fonts/newFont3.fnt"));
		//generator.dispose(); // don't forget to dispose to avoid memory leaks!

		this.setScreen(new MainMenuScreen(this));
		this.l = new LevelManager();
		this.s = new ScoreManager();

	}

	public void render() {
		super.render(); // important!
	}

	public void dispose() {
		batch.dispose();
		font14.dispose();
        font24.dispose();
		font44.dispose();
	}

}

