package com.example.asgeir.travel10k;

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
    TextView currentScorePlayer1;
    int scorePlayer1;
    TextView currentScorePlayer2;
    int scorePlayer2;
    private LinearLayout player1ScoreView;
    private LinearLayout player2ScoreView;
    private Context applicationContext;
    private ImageView player1Victory;
    private ImageView player2Victory;
    private ScrollView scrollView;
    private FloatingActionButton rollAgainButton;
    private FloatingActionButton stopButton;
    DicePool dicePool;
    boolean isFirstPlayerTurn;
    int pointsEarnedThisTurn;
    boolean player1HasBroken1000PointBarrier;
    boolean player2HasBroken1000PointBarrier;
    private int numberOfThrows;

    public Game(List<ImageButton> diceViews, LinearLayout player1ScoreView, LinearLayout player2ScoreView, Context applicationContext, ImageView player1Victory, ImageView player2Victory, ScrollView scrollView, FloatingActionButton rollAgainButton, FloatingActionButton stopButton) {
        this.player1ScoreView = player1ScoreView;
        this.player2ScoreView = player2ScoreView;
        this.applicationContext = applicationContext;
        this.player1Victory = player1Victory;
        this.player2Victory = player2Victory;
        this.scrollView = scrollView;
        this.rollAgainButton = rollAgainButton;
        this.stopButton = stopButton;
        this.dicePool = new DicePool(diceViews, applicationContext);
        this.isFirstPlayerTurn = true;
    }


    void addPointsToHanna(int points) {
        addPointsToCurrentPlayer(points, currentScorePlayer1, player1ScoreView);
    }

    void addPointsToAsgeir(int points) {
        addPointsToCurrentPlayer(points, currentScorePlayer2, player2ScoreView);
    }

    private void addPointsToCurrentPlayer(int points, TextView currentScore, LinearLayout scoreView) {
        int totalPoints = points;
        TextView textView = new TextView(applicationContext);

        if (currentScore != null) {
            totalPoints += Integer.parseInt(currentScore.getText().toString());
            currentScore.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        scoreView.addView(textView);
        textView.setTextColor(Color.GRAY);
        textView.setText(Integer.toString(totalPoints));
        if(isFirstPlayerTurn){
            currentScorePlayer1 = textView;
            scorePlayer1 = totalPoints;
        }
        else{
            currentScorePlayer2 = textView;
            scorePlayer2 = totalPoints;
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
            if (isFirstPlayerTurn) {
                player1HasBroken1000PointBarrier = true;
            }
            else {
                player2HasBroken1000PointBarrier = true;
            }
        }
    }

    private boolean playerHasBrokenBarrier() {
        return (isFirstPlayerTurn && player1HasBroken1000PointBarrier) || (!isFirstPlayerTurn && player2HasBroken1000PointBarrier);
    }

    private void endTurn() {
        evaluateVictoryConditions();
        pointsEarnedThisTurn = 0;
        isFirstPlayerTurn = !isFirstPlayerTurn;
        numberOfThrows = 0;
        dicePool.activateDice();
        dicePool.rollAllActiveDice();
    }

    private void evaluateVictoryConditions() {
        if(!isFirstPlayerTurn && Math.max(scorePlayer2, scorePlayer1) >= 10000){
            hideFloatingButtons();
            if (scorePlayer1 > scorePlayer2) {
                showHannaVictory();
            }
            else {
                showAsgeirVictory();
            }
        }
    }

    private void hideFloatingButtons() {
        rollAgainButton.setVisibility(View.INVISIBLE);
        stopButton.setVisibility(View.INVISIBLE);
    }

    private void showAsgeirVictory() {
        player2Victory.bringToFront();
        player2Victory.setVisibility(View.VISIBLE);
    }

    private void showHannaVictory() {
        player1Victory.bringToFront();
        player1Victory.setVisibility(View.VISIBLE);
    }

    private void addPoints(int points) {
        if (isFirstPlayerTurn) {
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
