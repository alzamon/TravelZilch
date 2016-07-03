package com.example.asgeir.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageButton;

import java.util.Random;

public class Die {
    private Context applicationContext;
    private ImageButton view;
    private boolean wantToKeep;
    private boolean active;
    private Integer value;
    private Random random;

    public Die(ImageButton view, Context applicationContext) {
        this.view = view;
        this.setWantToKeep(false);
        this.setActive(true);
        this.applicationContext = applicationContext;
        this.random = new Random();
        this.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setWantToKeep(!isWantToKeep());
            }
        });

    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
        this.view.setImageDrawable(getImageForDieValue(value));

    }

    public void setActive(boolean active) {
        this.active = active;
        this.view.setVisibility(active ? View.VISIBLE : View.INVISIBLE);
        this.setWantToKeep(false);
    }

    public boolean isActive() {
        return active;
    }

    public boolean isWantToKeep() {
        return wantToKeep;
    }

    public void setWantToKeep(boolean wantToKeep) {
        this.wantToKeep = wantToKeep;
        view.setBackgroundColor(wantToKeep ? Color.YELLOW : Color.TRANSPARENT);
    }

    private Drawable getImageForDieValue(int value){
        switch (value) {
            case 1:
                return applicationContext.getDrawable(R.drawable.one);
            case 2:
                return applicationContext.getDrawable(R.drawable.two);
            case 3:
                return applicationContext.getDrawable(R.drawable.three);
            case 4:
                return applicationContext.getDrawable(R.drawable.four);
            case 5:
                return applicationContext.getDrawable(R.drawable.five);
            case 6:
                return applicationContext.getDrawable(R.drawable.six);

        }
        throw new RuntimeException("Die value too outside range 1-6.");
    }

    public void roll(){
        setValue(random.nextInt(5)+1);
    }

}
