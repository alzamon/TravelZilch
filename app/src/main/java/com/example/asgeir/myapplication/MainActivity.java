package com.example.asgeir.myapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Game game = new Game(Arrays.asList(
                (ImageButton) findViewById(R.id.die_one),
                (ImageButton) findViewById(R.id.die_two),
                (ImageButton) findViewById(R.id.die_three),
                (ImageButton) findViewById(R.id.die_four),
                (ImageButton) findViewById(R.id.die_five),
                (ImageButton) findViewById(R.id.die_six)

        ),
                (LinearLayout) findViewById(R.id.hanna_text_space),
                (LinearLayout) findViewById(R.id.asgeir_text_space),
                getApplicationContext()

        );

        FloatingActionButton rollAgain = (FloatingActionButton) findViewById(R.id.roll_again);
        rollAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.calculatePointsAndRoll();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
