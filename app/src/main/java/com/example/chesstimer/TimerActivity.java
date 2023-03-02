package com.example.chesstimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class TimerActivity extends AppCompatActivity {

    private static final String EXTRA_MINUTES = "minutes";
    private static final String EXTRA_ADDING = "adding";

    private TextView textViewPlayer1;
    private TextView textViewPlayer2;

    private Button buttonPause;

    private int minutes;
    private int adding;

    long startTimePlayer1 = 0;
    long startTimePlayer2 = 0;
    long millisPlayer1;
    long millisPlayer2;
    int state = 0;

    Handler timerHandler = new Handler();
    Runnable timerRunnablePlayer1 = new Runnable() {
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTimePlayer1;
            millisPlayer1 -= millis;
            if (millisPlayer1 <= 0) {
                textViewPlayer1.setText("00:00.00");
                state = 3;
                return;
            }
            startTimePlayer1 += millis;
            textViewPlayer1.setText(getTextViewPlayer(millisPlayer1));
            timerHandler.postDelayed(this, 100);
        }
    };

    Runnable timerRunnablePlayer2 = new Runnable() {
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTimePlayer2;
            millisPlayer2 -= millis;
            if (millisPlayer2 <= 0) {
                textViewPlayer2.setText("00:00.00");
                state = 3;
                return;
            }
            startTimePlayer2 += millis;
            textViewPlayer2.setText(getTextViewPlayer(millisPlayer2));
            timerHandler.postDelayed(this, 100);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        minutes = getIntent().getIntExtra(EXTRA_MINUTES, 0);
        adding = getIntent().getIntExtra(EXTRA_ADDING, 0);

        if (savedInstanceState != null) {
            millisPlayer1 = savedInstanceState.getLong("millisPlayer1");
            millisPlayer2 = savedInstanceState.getLong("millisPlayer2");
        } else {
            millisPlayer1 = (long) minutes * 60 * 1000;
            millisPlayer2 = (long) minutes * 60 * 1000;
        }

        initViews();

        textViewPlayer1.setText(getTextViewPlayer(millisPlayer1));
        textViewPlayer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (state == 0) {
                    startTimePlayer2 = System.currentTimeMillis();
                    timerHandler.postDelayed(timerRunnablePlayer2, 0);
                    state = 2;
                } else if (state == 1) {
                    timerHandler.removeCallbacks(timerRunnablePlayer1);
                    startTimePlayer2 = System.currentTimeMillis();
                    millisPlayer1 += (long) adding * 1000;
                    textViewPlayer1.setText(getTextViewPlayer(millisPlayer1));
                    timerHandler.postDelayed(timerRunnablePlayer2, 0);
                    state = 2;
                }
            }
        });

        textViewPlayer2.setText(getTextViewPlayer(millisPlayer2));
        textViewPlayer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (state == 0) {
                    startTimePlayer1 = System.currentTimeMillis();
                    timerHandler.postDelayed(timerRunnablePlayer1, 0);
                    state = 1;
                } else if (state == 2) {
                    timerHandler.removeCallbacks(timerRunnablePlayer2);
                    startTimePlayer1 = System.currentTimeMillis();
                    millisPlayer2 += (long) adding * 1000;
                    textViewPlayer2.setText(getTextViewPlayer(millisPlayer2));
                    timerHandler.postDelayed(timerRunnablePlayer1, 0);
                    state = 1;
                }
            }
        });

        buttonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (state == 1) {
                    timerHandler.removeCallbacks(timerRunnablePlayer1);
                } else if (state == 2) {
                    timerHandler.removeCallbacks(timerRunnablePlayer2);
                }
                state = 0;
            }
        });

    }

    private String getTextViewPlayer(long millisPlayer) {
        int currSeconds = (int) (millisPlayer / 1000);
        int currMinutes = currSeconds / 60;
        int currMillis = (int)  (millisPlayer - currSeconds * 1000) / 10;
        currSeconds = currSeconds % 60;
        return String.format(Locale.ENGLISH,
                "%d:%02d.%02d", currMinutes, currSeconds, currMillis);
    }
    private void initViews(){
        textViewPlayer1 = findViewById(R.id.textViewPlayer1);
        textViewPlayer2 = findViewById(R.id.textViewPlayer2);
        buttonPause = findViewById(R.id.buttonPause);
    }

    public static Intent newIntent(Context context, int minutes, int adding) {
        Intent intent = new Intent(context, TimerActivity.class);
        intent.putExtra(EXTRA_MINUTES, minutes);
        intent.putExtra(EXTRA_ADDING, adding);
        return intent;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("millisPlayer1", millisPlayer1);
        outState.putLong("millisPlayer2", millisPlayer2);
    }

}