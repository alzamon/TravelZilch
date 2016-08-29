package com.example.asgeir.travel10k;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.List;

public class Game {
    private ScrollView scrollView;
    private ImageButton rollAgainButton;
    private ImageButton stopButton;
    DicePool dicePool;
    private Turn turn;
    private Player player1;
    private Player player2;
    TextViewFactory textViewFactory;

    public Game(List<ImageButton> diceViews, LinearLayout firstPlayerScoreView, LinearLayout secondPlayerScoreView,
                TextViewFactory textViewFactory, ImageView firstPlayerVictory, ImageView secondPlayerVictory, ScrollView scrollView,
                ImageButton rollAgainButton, ImageButton stopButton) {
        this.textViewFactory = textViewFactory;
        player1 = new Player(firstPlayerScoreView, firstPlayerVictory, textViewFactory);
        player2 = new Player(secondPlayerScoreView, secondPlayerVictory, textViewFactory);
        this.scrollView = scrollView;
        this.rollAgainButton = rollAgainButton;
        this.stopButton = stopButton;
        this.dicePool = new DicePool(diceViews, textViewFactory.getApplicationContext());
        turn = new Turn(player1, player2);
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
        addPointsToCurrentPlayer(0);
        endTurn();
    }

    private void addPointsToCurrentPlayer(int points) {
        turn.getCurrentPlayer().addPoints(points);
        scrollViewDown();
    }

    void calculatePointsAndEndTurn() {
        int points = turn.getPointsEarnedThisTurn() + dicePool.calculatePointsOnActiveDice();
        setBarrierBrokenStatusForPlayer(points);
        addPointsToCurrentPlayer(turn.getCurrentPlayer().is1000PointBarrierBroken() ? points : 0);
        endTurn();
    }

    private void setBarrierBrokenStatusForPlayer(int points) {
        if (points >= 1000) {
            turn.getCurrentPlayer().break1000PointBarrier();
        }
    }

    private void endTurn() {
        evaluateVictoryConditions();
        turn.resetTurnData();
        dicePool.activateDice();
        dicePool.rollAllActiveDice();
    }


    private void evaluateVictoryConditions() {
        if (turn.isLastPlayer() && Math.max(player2.getScore(), player1.getScore()) >= 10000) {
            hideFloatingButtons();
            showVictory(player1.getScore() >= player2.getScore() ? player1 : player2);
        }
    }

    private void hideFloatingButtons() {
        rollAgainButton.setVisibility(View.INVISIBLE);
        stopButton.setVisibility(View.INVISIBLE);
    }

    private void showVictory(Player player) {
        player.getPlayerVictory().bringToFront();
        player.getPlayerVictory().setVisibility(View.VISIBLE);
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
