package com.example.asgeir.travel10k;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.TextView;

public class TextViewFactory {
    private Context applicationContext;

    public TextViewFactory(Context applicationContext) {

        this.applicationContext = applicationContext;
    }

    public Context getApplicationContext() {
        return applicationContext;
    }

    public TextView createTextView(String text){
        TextView textView = new TextView(applicationContext);
        Typeface typeface = Typeface.createFromAsset(applicationContext.getAssets(), "alwaysforever.ttf");
        textView.setTextSize(22);
        textView.setTypeface(typeface, Typeface.BOLD);
        textView.setText(text);
        textView.setTextColor(Color.GRAY);
        return textView;
    }


}
