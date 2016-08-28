package com.example.asgeir.travel10k;

public class Turn {

    private int pointsEarnedThisTurn = 0;
    private boolean isFirstPlayerTurn = true;
    private int numberOfThrows = 0;

    public int getPointsEarnedThisTurn() {
        return pointsEarnedThisTurn;
    }

    public boolean isFirstPlayerTurn() {
        return isFirstPlayerTurn;
    }

    public int getNumberOfThrows() {
        return numberOfThrows;
    }

    public void resetTurnData() {
        pointsEarnedThisTurn = 0;
        isFirstPlayerTurn = !isFirstPlayerTurn();
        numberOfThrows = 0;
    }

    public void addPoints(int points) {
        pointsEarnedThisTurn += points;
    }

    public void addThrow() {
        numberOfThrows++;
    }
}
