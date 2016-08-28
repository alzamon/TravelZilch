package com.example.asgeir.travel10k;

import android.graphics.Color;
import android.graphics.Paint;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Player {
    private final LinearLayout scoreView;
    private final ImageView playerVictory;
    private int score;
    private TextView currentScoreView;
    private boolean is1000PointBarrierBroken;

    public Player(LinearLayout scoreView, ImageView playerVictory) {
        this.scoreView = scoreView;
        this.playerVictory = playerVictory;
    }

    public void addPoints(int points, TextView newCurrentScoreView) {
        int totalPoints = points + score;
        if (currentScoreView != null) {
            currentScoreView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        scoreView.addView(newCurrentScoreView);
        newCurrentScoreView.setTextColor(Color.GRAY);
        newCurrentScoreView.setText(Integer.toString(totalPoints));
        this.currentScoreView = newCurrentScoreView;
        score += points;
    }

    public int getScore() {
        return score;
    }

    public ImageView getPlayerVictory() {
        return playerVictory;
    }

    public boolean is1000PointBarrierBroken() {
        return is1000PointBarrierBroken;
    }

    public void break1000PointBarrier() {
        is1000PointBarrierBroken = true;
    }
}
