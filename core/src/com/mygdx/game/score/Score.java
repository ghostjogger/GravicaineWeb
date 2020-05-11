package com.mygdx.game.score;

/**
 * Created by stephenball on 20/04/2017.
 */

public class Score implements Comparable<Score> {
    public int score;
    public String name;

    public Score(int score, String name) {
        this.score = score;
        this.name = name;
    }

    @Override
    public int compareTo(Score other) {
        if (this.score == other.score) {
            return 0;
        }
        return this.score > other.score ? -1 : 1;
    }


}