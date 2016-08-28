package com.example.asgeir.travel10k;

import java.util.Arrays;
import java.util.List;

public class Turn {

    private int pointsEarnedThisTurn = 0;
    private List<Player> players;
    private Player currentPlayer;
    private int numberOfThrows = 0;
    private boolean lastPlayer;

    public Turn(Player player1, Player player2) {
        players = Arrays.asList(player1, player2);
        currentPlayer = player1;
    }

    public int getPointsEarnedThisTurn() {
        return pointsEarnedThisTurn;
    }

    public int getNumberOfThrows() {
        return numberOfThrows;
    }

    public void resetTurnData() {
        pointsEarnedThisTurn = 0;
        currentPlayer = players.get((players.indexOf(currentPlayer) + 1) % players.size());
        numberOfThrows = 0;
    }

    public void addPoints(int points) {
        pointsEarnedThisTurn += points;
    }

    public void addThrow() {
        numberOfThrows++;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isLastPlayer() {
        return currentPlayer.equals(players.get(players.size()-1));
    }
}
