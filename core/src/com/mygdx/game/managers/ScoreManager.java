package com.mygdx.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Gravicaine;
import com.mygdx.game.score.Score;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by stephenball on 20/04/2017.
 */

public class ScoreManager {

    private static ScoreManager uniqueInstance;





    private static int NUM_HIGH_SCORES = 5;


    public static List<Score> high_scores;


    public ScoreManager(){

        this.high_scores = new ArrayList<Score>();



    }

    public static ScoreManager getUniqueInstance(){
        if (uniqueInstance == null) {
            uniqueInstance = new ScoreManager();
        }
        return uniqueInstance;
    }

    public static void sortScores(){
        Collections.sort(high_scores);
        //truncate list
        if (high_scores.size() > NUM_HIGH_SCORES ) {
            high_scores.remove(high_scores.size()-1);
        }
    }



    public static boolean updateHighScore(double score){


        Collections.sort(high_scores);
			/* find our score on the high score list if it qualifies
			 * and add it
			 */
            for (int s = 0; s < high_scores.size(); s++) {

                if (score > high_scores.get(s).score) {

                    return true;

                }
            }
            //make sure also add for size 0
            if (high_scores.size() < NUM_HIGH_SCORES ) {
                return true;
            }

            return false;




    }


}
