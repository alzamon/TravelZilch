package com.example.asgeir.travel10k;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageButton;

import com.android.internal.util.Predicate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicePool {
    private Context applicationContext;
    private List<Die> diceList;
    private Predicate<Die> wantToKeepPredicate = new Predicate<Die>() {
        @Override
        public boolean apply(Die die) {
            return die.isWantToKeep();
        }
    };
    private Predicate<Die> trivialPredicate = new Predicate<Die>() {
        @Override
        public boolean apply(Die die) {
            return true;
        }
    };

    public DicePool(List<ImageButton> diceViews, Context applicationContext) {
        this.diceList = new ArrayList<>();
        this.applicationContext = applicationContext;
        initializeDice(diceViews);
    }

    public DicePool() {

    }

    private void initializeDice(List<ImageButton> diceViews) {
        for (int i = 0; i < 6; i++) {
            this.diceList.add(new Die(diceViews.get(i), applicationContext));
            diceViews.get(i).setBackgroundColor(Color.TRANSPARENT);
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

    public boolean wasAThrow() {
        for (int i = 0; i < 6; i++) {
            if (diceList.get(i).isActive() && !diceList.get(i).isWantToKeep()) {
                return true;
            }
        }
        return false;
    }

    private Map<Integer, Integer> createValueCounter(Predicate<Die> predicate) {
        Map<Integer, Integer> valueCounter = new HashMap<>();
        for (int i = 1; i <= 6; i++) {
            valueCounter.put(i, 0);
        }
        for (int i = 0; i < 6; i++) {
            Die die = diceList.get(i);
            if (die.isActive() && predicate.apply(die)) {
                valueCounter.put(die.getValue(), valueCounter.get(die.getValue()) + 1);
            }
        }
        return valueCounter;
    }

    public void activateDice() {
        for (int i = 0; i < 6; i++) {
            diceList.get(i).activate();
        }
    }

    public boolean noPointsOnActiveDice(){
        return  calculatePointsOnActiveDice() == 0;
    }

    public int calculatePointsOnActiveDice(){
        return calculatePoints(createValueCounter(trivialPredicate));
    }


    public void disableDiceYouWantedToKeep() {
        Map<Integer, Integer> valueCounter = createValueCounter(wantToKeepPredicate);
        for (int i = 0; i < 6; i++) {
            Die die = diceList.get(i);
            if (die.isWantToKeep()) {
                if (hasValue(valueCounter, die)) {
                    die.disable(true);
                }
                else {
                    die.disable(false);
                }
            }
        }
    }


    private boolean hasValue(Map<Integer, Integer> valueCounter, Die die) {
        return die.getValue() == 1 || die.getValue() == 5 || valueCounter.get(die.getValue()) >= 3;
    }

    int calculatePointsLastRound() {
        Map<Integer, Integer> valueCounter = createValueCounter(wantToKeepPredicate);
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

    public boolean pointsOnAllDice() {
        if (wasAThrow()) {
            return false;
        }
        for (int i = 0; i < 6; i++) {
            if (!diceList.get(i).hadValue() && !diceList.get(i).isActive()) {
                return false;
            }
        }
        Map<Integer, Integer> valueCounter = createValueCounter(wantToKeepPredicate);
        for(int i = 0; i < 6; i++) {
            if (diceList.get(i).isWantToKeep() && !hasValue(valueCounter, diceList.get(i))) {
                return false;
            }
        }
        return true;
    }
}
