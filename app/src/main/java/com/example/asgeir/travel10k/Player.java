package com.example.asgeir.travel10k;

import android.widget.ImageView;
import android.widget.LinearLayout;

public class Player {
    private final LinearLayout scoreView;
    private final ImageView playerVictory;
    private int score;

    public Player(LinearLayout scoreView, ImageView playerVictory, int score) {
        this.scoreView = scoreView;
        this.playerVictory = playerVictory;
        this.score = score;
    }

    public LinearLayout getScoreView() {
        return scoreView;
    }

    public void addPoints(int points) {
        score += points;
    }

    public int getScore() {
        return score;
    }
}
