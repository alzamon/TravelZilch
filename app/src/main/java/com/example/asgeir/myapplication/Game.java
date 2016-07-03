package com.example.asgeir.myapplication;

import android.content.Context;
import android.graphics.Paint;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class Game {
    TextView currentScoreHanna;
    TextView currentScoreAsgeir;
    private LinearLayout hannaScoreView;
    private LinearLayout asgeirScoreView;
    private Context applicationContext;
    DicePool dicePool;
    boolean isHannasTurn;
    int pointsEarnedThisTurn;
    int numberOfThrowsThisTurn;

    public Game() {
    }

    public Game(List<ImageButton> diceViews, LinearLayout hannaScoreView, LinearLayout asgeirScoreView, Context applicationContext) {
        this.hannaScoreView = hannaScoreView;
        this.asgeirScoreView = asgeirScoreView;
        this.applicationContext = applicationContext;
        this.numberOfThrowsThisTurn = 0;
        this.dicePool = new DicePool(diceViews, applicationContext);
    }


    void addPointsToHanna(int points) {
        int totalPoints = points;
        TextView textView = new TextView(applicationContext);

        if (currentScoreHanna != null) {
            totalPoints += Integer.parseInt(currentScoreHanna.getText().toString());
            currentScoreHanna.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        hannaScoreView.addView(textView);
        textView.setText(Integer.toString(totalPoints));
        currentScoreHanna = textView;

    }

    void addPointsToAsgeir(int points) {
        int totalPoints = points;
        TextView textView = new TextView(applicationContext);
        if (currentScoreAsgeir != null) {
            totalPoints += Integer.parseInt(currentScoreAsgeir.getText().toString());
            currentScoreAsgeir.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        asgeirScoreView.addView(textView);
        textView.setText(Integer.toString(totalPoints));
        currentScoreAsgeir = textView;
    }

    public void calculatePointsAndRoll() {
        if (!dicePool.wasAThrow()) {
            addPoints(pointsEarnedThisTurn+dicePool.calculatePointsLastRound());
            endTurn();
            return;
        }
        if (numberOfThrowsThisTurn != 0 && dicePool.wasZilch()) {
            addPoints(0);
            endTurn();
            return;
        }
        int pointsEarnedLastRound = dicePool.calculatePointsLastRound();

        pointsEarnedThisTurn += pointsEarnedLastRound;
        dicePool.disableDiceYouWantedToKeep();
        numberOfThrowsThisTurn++;
        dicePool.rollAllActiveDice();
    }

    private void endTurn() {
        pointsEarnedThisTurn = 0;
        isHannasTurn = !isHannasTurn;
        dicePool.activateDice();
        dicePool.rollAllActiveDice();
        numberOfThrowsThisTurn = 0;
    }



    private void resolveTurnIfItIsOver(int pointsEarnedThisRound) {
        if (pointsEarnedThisRound == 0) {
            pointsEarnedThisTurn = 0;
            isHannasTurn = !isHannasTurn;
        }
    }


    private void addPoints(int points) {
        if (isHannasTurn) {
            addPointsToHanna(points);
        }
        else {
            addPointsToAsgeir(points);
        }
    }
}
