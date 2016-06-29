package com.example.asgeir.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Game game = new Game(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myActivity(game);
            }
        });
    }

    private void myActivity(Game game) {
        showDice();
    }

    private void showDice() {
        final ImageButton dieOne = (ImageButton) findViewById(R.id.die_one);
        final ImageButton dieTwo = (ImageButton) findViewById(R.id.die_two);
        final ImageButton dieThree = (ImageButton) findViewById(R.id.die_three);
        final ImageButton dieFour = (ImageButton) findViewById(R.id.die_four);
        final ImageButton dieFive = (ImageButton) findViewById(R.id.die_five);
        final ImageButton dieSix = (ImageButton) findViewById(R.id.die_six);

        dieOne.setVisibility(View.VISIBLE);
        dieTwo.setVisibility(View.VISIBLE);
        dieThree.setVisibility(View.VISIBLE);
        dieFour.setVisibility(View.VISIBLE);
        dieFive.setVisibility(View.VISIBLE);
        dieSix.setVisibility(View.VISIBLE);

        dieOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dieOne.setBackgroundColor(Color.YELLOW);
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
