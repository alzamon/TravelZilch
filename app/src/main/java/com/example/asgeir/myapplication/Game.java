package com.example.asgeir.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

public class Game {
    TextView currentScoreHanna;
    int scoreHanna;
    TextView currentScoreAsgeir;
    int scoreAsgeir;
    private LinearLayout hannaScoreView;
    private LinearLayout asgeirScoreView;
    private Context applicationContext;
    private ImageView hannaVictory;
    private ImageView asgeirVictory;
    private ScrollView scrollView;
    private FloatingActionButton rollAgainButton;
    private FloatingActionButton stopButton;
    DicePool dicePool;
    boolean isHannasTurn;
    int pointsEarnedThisTurn;
    boolean hannaHasBrokenBarrier;
    boolean asgeirHasBrokenBarrier;
    private int numberOfThrows;

    public Game(List<ImageButton> diceViews, LinearLayout hannaScoreView, LinearLayout asgeirScoreView, Context applicationContext, ImageView hannaVictory, ImageView asgeirVictory, ScrollView scrollView, FloatingActionButton rollAgainButton, FloatingActionButton stopButton) {
        this.hannaScoreView = hannaScoreView;
        this.asgeirScoreView = asgeirScoreView;
        this.applicationContext = applicationContext;
        this.hannaVictory = hannaVictory;
        this.asgeirVictory = asgeirVictory;
        this.scrollView = scrollView;
        this.rollAgainButton = rollAgainButton;
        this.stopButton = stopButton;
        this.dicePool = new DicePool(diceViews, applicationContext);
        this.isHannasTurn = true;
    }


    void addPointsToHanna(int points) {
        addPointsToCurrentPlayer(points, currentScoreHanna, hannaScoreView, scoreHanna);
    }

    void addPointsToAsgeir(int points) {
        addPointsToCurrentPlayer(points, currentScoreAsgeir, asgeirScoreView, scoreAsgeir);
    }

    private void addPointsToCurrentPlayer(int points, TextView currentScore, LinearLayout scoreView, int score) {
        int totalPoints = points;
        TextView textView = new TextView(applicationContext);

        if (currentScore != null) {
            totalPoints += Integer.parseInt(currentScore.getText().toString());
            currentScore.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        scoreView.addView(textView);
        textView.setTextColor(Color.GRAY);
        textView.setText(Integer.toString(totalPoints));
        if(isHannasTurn){
            currentScoreHanna = textView;
            scoreHanna = totalPoints;
        }
        else{
            currentScoreAsgeir = textView;
            scoreAsgeir = totalPoints;
        }
        scrollViewDown();
    }

    private void scrollViewDown() {
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    public void calculatePointsAndRoll() {
        if (!dicePool.wasAThrow() && !dicePool.pointsOnAllDice()) {
            calculatePointsAndEndTurn(true);
            return;
        }
        int pointsEarnedLastRound = dicePool.calculatePointsLastRound();
        if ((numberOfThrows != 0 && pointsEarnedLastRound == 0) || (dicePool.noPointsOnActiveDice())) {
            zilchAction();
            return;
        }
        pointsEarnedThisTurn += pointsEarnedLastRound;
        dicePool.disableDiceYouWantedToKeep();

        if (dicePool.pointsOnAllDice()) {
            dicePool.activateDice();
        }
        numberOfThrows++;
        dicePool.rollAllActiveDice();
    }

    private void zilchAction() {
        addPoints(0);
        endTurn();
    }

    private void calculatePointsAndEndTurn(boolean isAThrow) {
        int points;
        if(isAThrow){
            points = pointsEarnedThisTurn + dicePool.calculatePointsLastRound();
        }
        else{
            points = pointsEarnedThisTurn + dicePool.calculatePointsOnActiveDice();
        }
        setBarrierBrokenStatusForPlayer(points);
        if (playerHasBrokenBarrier()) {
            addPoints(points);
        }
        else{
            addPoints(0);
        }
        endTurn();
    }

    private void setBarrierBrokenStatusForPlayer(int points) {
        if (points >= 1000) {
            if (isHannasTurn) {
                hannaHasBrokenBarrier = true;
            }
            else {
                asgeirHasBrokenBarrier = true;
            }
        }
    }

    private boolean playerHasBrokenBarrier() {
        return (isHannasTurn && hannaHasBrokenBarrier) || (!isHannasTurn && asgeirHasBrokenBarrier);
    }

    private void endTurn() {
        if(!isHannasTurn && Math.max(scoreAsgeir, scoreHanna) >= 10000){
            hideFloatingButtons();
            if (scoreHanna > scoreAsgeir) {
                showHannaVictory();
            }
            else {
                showAsgeirVictory();
            }
        }
        pointsEarnedThisTurn = 0;
        isHannasTurn = !isHannasTurn;
        numberOfThrows = 0;
        dicePool.activateDice();
        dicePool.rollAllActiveDice();
    }

    private void hideFloatingButtons() {
        rollAgainButton.setVisibility(View.INVISIBLE);
        stopButton.setVisibility(View.INVISIBLE);
    }

    private void showAsgeirVictory() {
        asgeirVictory.bringToFront();
        asgeirVictory.setVisibility(View.VISIBLE);
    }

    private void showHannaVictory() {
        hannaVictory.bringToFront();
        hannaVictory.setVisibility(View.VISIBLE);
    }

    private void addPoints(int points) {
        if (isHannasTurn) {
            addPointsToHanna(points);
        }
        else {
            addPointsToAsgeir(points);
        }
    }

    public void stop() {
        if (dicePool.noPointsOnActiveDice()) {
            zilchAction();
        }
        else {
            calculatePointsAndEndTurn(false);
        }
    }
}
