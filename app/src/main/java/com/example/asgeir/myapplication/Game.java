package com.example.asgeir.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {
    TextView currentScoreHanna;
    TextView currentScoreAsgeir;
    private List<Die> diceList;
    private LinearLayout hannaScoreView;
    private LinearLayout asgeirScoreView;
    private Context applicationContext;

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
        this.diceList = new ArrayList<>();
        initializeDice(diceViews);
    }

    private void initializeDice(List<ImageButton> diceViews) {
        for (int i = 0; i < 6; i++) {
            this.diceList.add(new Die(diceViews.get(i), applicationContext));
            diceViews.get(i).setBackgroundColor(Color.TRANSPARENT);
            final int finalI = i;
        }
        rollAllActiveDice();
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
        if (!wasAThrow()) {
            addPoints(pointsEarnedThisTurn+calculatePointsLastRound());
            endTurn();
            return;
        }
        if (wasZilch()) {
            addPoints(0);
            endTurn();
            return;
        }
        int pointsEarnedLastRound = calculatePointsLastRound();

        pointsEarnedThisTurn += pointsEarnedLastRound;
        disableDiceYouWantedToKeep();
        numberOfThrowsThisTurn++;
        rollAllActiveDice();
    }

    private void endTurn() {
        pointsEarnedThisTurn = 0;
        isHannasTurn = !isHannasTurn;
        activateDice();
        rollAllActiveDice();
        numberOfThrowsThisTurn = 0;
    }

    private boolean wasAThrow() {
        for(int i = 0; i < 6; i++) {
            if (diceList.get(i).isActive() && !diceList.get(i).isWantToKeep()) {
                return true;
            }
        }
        return false;
    }

    private boolean wasZilch() {
        if (numberOfThrowsThisTurn == 0) {
            return false;
        }
        Map<Integer, Integer> valueCounter = new HashMap<>();
        for (int i = 1; i <= 6; i++) {
            valueCounter.put(i, 0);
        }
        for (int i = 0; i < 6; i++) {
            Die die = diceList.get(i);
            if (die.isActive()) {
                valueCounter.put(die.getValue(), valueCounter.get(die.getValue()) + 1);
            }
        }
        int pointsThrown = calculatePoints(valueCounter);
        if (pointsThrown == 0) {
            return true;
        }
        else {
            return false;
        }
    }

    private void resolveTurnIfItIsOver(int pointsEarnedThisRound) {
        if (pointsEarnedThisRound == 0) {
            pointsEarnedThisTurn = 0;
            isHannasTurn = !isHannasTurn;
        }
    }

    private void disableDiceYouWantedToKeep() {
        for (int i = 0; i < 6; i++) {
            if (diceList.get(i).isWantToKeep()) {
                diceList.get(i).setActive(false);
            }
        }
    }

    private void rollAllActiveDice() {
        for (int i = 0; i < 6; i++) {
            if (diceList.get(i).isActive() && !diceList.get(i).isWantToKeep()) {
                diceList.get(i).roll();
            }
        }
    }

    int calculatePointsLastRound() {
        Map<Integer, Integer> valueCounter = createValueCounter();
        return calculatePoints(valueCounter);
    }

    int calculatePoints(Map<Integer, Integer> valueCounter) {
        int valueTotal = 0;
        for (int value = 1; value <= 6; value++) {
            Integer numberOfValues = valueCounter.get(value);
            if (numberOfValues == null) {
                continue;
            }
            if (numberOfValues >= 3) {
                int x;
                if (value == 1) {
                    x = 1000;
                }
                else {
                    x = 100 * value;
                }
                valueTotal += x * Math.pow(2, numberOfValues - 3);
                continue;
            }
            if (value == 1) {
                valueTotal += 100 * numberOfValues;
            }
            if (value == 5) {
                valueTotal += 50 * numberOfValues;
            }
        }
        return valueTotal;
    }

    private Map<Integer, Integer> createValueCounter() {
        Map<Integer, Integer> valueCounter = new HashMap<>();
        for (int i = 1; i <= 6; i++) {
            valueCounter.put(i, 0);
        }
        for (int i = 0; i < 6; i++) {
            Die die = diceList.get(i);
            if (die.isActive() && die.isWantToKeep()) {
                valueCounter.put(die.getValue(), valueCounter.get(die.getValue()) + 1);
            }
        }
        return valueCounter;
    }

    private void activateDice() {
        for(int i = 0; i < 6; i++) {
            diceList.get(i).setActive(true);
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
