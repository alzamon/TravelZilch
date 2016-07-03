package com.example.asgeir.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicePool {
    private Context applicationContext;
    private List<Die> diceList;
    private List<ImageButton> diceViews;

    public DicePool(List<ImageButton> diceViews, Context applicationContext) {
        this.diceList = new ArrayList<>();
        this.applicationContext = applicationContext;
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

    public void rollAllActiveDice() {
        for (int i = 0; i < 6; i++) {
            if (diceList.get(i).isActive() && !diceList.get(i).isWantToKeep()) {
                diceList.get(i).roll();
            }
        }
    }

    public Die get(int i) {
        return diceList.get(i);
    }

    public boolean wasAThrow() {
        for(int i = 0; i < 6; i++) {
            if (diceList.get(i).isActive() && !diceList.get(i).isWantToKeep()) {
                return true;
            }
        }
        return false;
    }

    public boolean wasZilch() {
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

    public void activateDice() {
        for(int i = 0; i < 6; i++) {
            diceList.get(i).setActive(true);
        }
    }


    public void disableDiceYouWantedToKeep() {
        for (int i = 0; i < 6; i++) {
            if (diceList.get(i).isWantToKeep()) {
                diceList.get(i).setActive(false);
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
}
