package com.example.asgeir.myapplication;

import android.graphics.Paint;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Game {
    private final MainActivity mainActivity;
    TextView currentScoreHanna;
    TextView currentScoreAsgeir;

    public Game(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    void addPointsToHanna(int points, TextView textView) {
        int totalPoints = points;
        if (currentScoreHanna != null) {
            totalPoints += Integer.parseInt(currentScoreHanna.getText().toString());
            currentScoreHanna.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        LinearLayout hannaTextSpace = (LinearLayout) mainActivity.findViewById(R.id.hanna_text_space);
        hannaTextSpace.addView(textView);
        textView.setText(Integer.toString(totalPoints));
        currentScoreHanna = textView;

    }

    void addPointsToAsgeir(int points, TextView textView) {
        int totalPoints = points;
        if (currentScoreAsgeir != null) {
            totalPoints += Integer.parseInt(currentScoreAsgeir.getText().toString());
            currentScoreAsgeir.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        LinearLayout asgeirTextSpace = (LinearLayout) mainActivity.findViewById(R.id.asgeir_text_space);
        asgeirTextSpace.addView(textView);
        textView.setText(Integer.toString(totalPoints));
        currentScoreAsgeir = textView;
    }

}
