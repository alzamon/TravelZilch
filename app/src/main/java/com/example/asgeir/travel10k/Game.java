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
    TextView currentScoreFirstPlayer;
    TextView currentScoreSecondPlayer;
    private Context applicationContext;
    private ScrollView scrollView;
    private FloatingActionButton rollAgainButton;
    private FloatingActionButton stopButton;
    DicePool dicePool;
    boolean firstPlayerHasBroken1000PointBarrier;
    boolean secondPlayerHasBroken1000PointBarrier;
    private Turn turn;
    private Player player1;
    private Player player2;

    public Game(List<ImageButton> diceViews, LinearLayout firstPlayerScoreView, LinearLayout secondPlayerScoreView, Context applicationContext, ImageView firstPlayerVictory, ImageView secondPlayerVictory, ScrollView scrollView, FloatingActionButton rollAgainButton, FloatingActionButton stopButton) {
        player1 = new Player(firstPlayerScoreView, firstPlayerVictory, 0);
        player2 = new Player(secondPlayerScoreView, secondPlayerVictory, 0);
        this.applicationContext = applicationContext;
        this.scrollView = scrollView;
        this.rollAgainButton = rollAgainButton;
        this.stopButton = stopButton;
        this.dicePool = new DicePool(diceViews, applicationContext);
        turn = new Turn();
    }


    void addPointsToFirstPlayer(int points) {
        addPointsToCurrentPlayer(points, currentScoreFirstPlayer, player1.getScoreView());
    }

    void addPointsToSecondPlayer(int points) {
        addPointsToCurrentPlayer(points, currentScoreSecondPlayer, player2.getScoreView());
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
        if (turn.isFirstPlayerTurn()) {
            currentScoreFirstPlayer = textView;
            player1.addPoints(totalPoints);
        }
        else {
            currentScoreSecondPlayer = textView;
            player2.addPoints(totalPoints);
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
            calculatePointsAndEndTurn();
            return;
        }
        int pointsEarnedLastRound = dicePool.calculatePointsLastRound();
        if ((turn.getNumberOfThrows() != 0 && pointsEarnedLastRound == 0) || (dicePool.noPointsOnActiveDice())) {
            zilchAction();
            return;
        }
        turn.addPoints(pointsEarnedLastRound);
        dicePool.disableDiceYouWantedToKeep();

        if (dicePool.pointsOnAllDice()) {
            dicePool.activateDice();
        }
        turn.addThrow();
        dicePool.rollAllActiveDice();
    }

    private void zilchAction() {
        addPoints(0);
        endTurn();
    }

    private void calculatePointsAndEndTurn() {
        int points = turn.getPointsEarnedThisTurn() + dicePool.calculatePointsOnActiveDice();
        setBarrierBrokenStatusForPlayer(points);
        addPoints(playerHasBrokenBarrier() ? points : 0);
        endTurn();
    }

    private void setBarrierBrokenStatusForPlayer(int points) {
        if (points >= 1000) {
            if (turn.isFirstPlayerTurn()) {
                firstPlayerHasBroken1000PointBarrier = true;
            }
            else {
                secondPlayerHasBroken1000PointBarrier = true;
            }
        }
    }

    private boolean playerHasBrokenBarrier() {
        return (turn.isFirstPlayerTurn() && firstPlayerHasBroken1000PointBarrier) || (!turn.isFirstPlayerTurn() && secondPlayerHasBroken1000PointBarrier);
    }

    private void endTurn() {
        evaluateVictoryConditions();
        turn.resetTurnData();
        dicePool.activateDice();
        dicePool.rollAllActiveDice();
    }


    private void evaluateVictoryConditions() {
        if (!turn.isFirstPlayerTurn() && Math.max(player2.getScore(), player1.getScore()) >= 10000) {
            hideFloatingButtons();
            if (player1.getScore() > player2.getScore()) {
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
        player2.getScoreView().bringToFront();
        player2.getScoreView().setVisibility(View.VISIBLE);
    }

    private void showHannaVictory() {
        player1.getScoreView().bringToFront();
        player1.getScoreView().setVisibility(View.VISIBLE);
    }

    private void addPoints(int points) {
        if (turn.isFirstPlayerTurn()) {
            addPointsToFirstPlayer(points);
        }
        else {
            addPointsToSecondPlayer(points);
        }
    }

    public void stop() {
        if (dicePool.noPointsOnActiveDice()) {
            zilchAction();
        }
        else {
            calculatePointsAndEndTurn();
        }
    }
}
