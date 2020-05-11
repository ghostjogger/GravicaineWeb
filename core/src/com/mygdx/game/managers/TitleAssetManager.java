package com.mygdx.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;

/**
 * Created by stephenball on 19/03/2017.
 */

public class TitleAssetManager {

    public TextureRegion[][] titleChars;
    public Array<TextureRegion[][]> titleCharPieces;
    public TextureRegion[][] sigChars;
    public Array<TextureRegion[][]> sigCharPieces;
    private Texture titleImage;
    private Texture sigImage;
    private TextureRegion titleImageRegion;
    private TextureRegion sigImageRegion;
    public Texture background;


    public TitleAssetManager(){

        this.titleImage = new Texture(Gdx.files.internal("graphics/GravicaineTitle.png"));
        this.sigImage = new Texture(Gdx.files.internal("graphics/GravicaineSignature.png"));
        this.background = new Texture(Gdx.files.internal("maps/title.png"));
        this.titleImageRegion = new TextureRegion(titleImage);
        this.titleCharPieces = new Array<TextureRegion[][]>();
        this.sigImageRegion = new TextureRegion(sigImage);
        this.sigCharPieces = new Array<TextureRegion[][]>();
        titleChars = titleImageRegion.split(72,72);
        sigChars = sigImageRegion.split(450/15,54);

        for (int i = 0; i < 11; i++){
            TextureRegion r = new TextureRegion(titleChars[0][i]);
            TextureRegion[][] t = r.split(72,4);
            titleCharPieces.add(t);
        }

        for (int i = 0; i < 15; i++){
            TextureRegion r = new TextureRegion(sigChars[0][i]);
            TextureRegion[][] t = r.split(450/15,4);
            sigCharPieces.add(t);
        }


    }






}
