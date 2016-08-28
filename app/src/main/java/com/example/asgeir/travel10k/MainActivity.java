package com.example.asgeir.travel10k;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Game game = new Game(Arrays.asList(
                (ImageButton) findViewById(R.id.die_one),
                (ImageButton) findViewById(R.id.die_two),
                (ImageButton) findViewById(R.id.die_three),
                (ImageButton) findViewById(R.id.die_four),
                (ImageButton) findViewById(R.id.die_five),
                (ImageButton) findViewById(R.id.die_six)

        ),
                (LinearLayout) findViewById(R.id.player1_text_space),
                (LinearLayout) findViewById(R.id.player2_text_space),
                getApplicationContext(),
                (ImageView)findViewById(R.id.player1_victory),
                (ImageView)findViewById(R.id.player2_victory),
                (ScrollView)findViewById(R.id.scroll_view),
                (FloatingActionButton)findViewById(R.id.roll_again),
                (FloatingActionButton)findViewById(R.id.stop)
        );

        FloatingActionButton rollAgain = (FloatingActionButton) findViewById(R.id.roll_again);
        rollAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.calculatePointsAndRoll();
            }
        });
        FloatingActionButton stop = (FloatingActionButton) findViewById(R.id.stop);
        stop.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        game.stop();
                                    }
                                }
        );
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