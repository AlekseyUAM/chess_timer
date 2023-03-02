package com.example.chesstimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private Button buttonStart;
    private EditText editTextTime;
    private EditText editTextAdding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int adding = 0;
                String textAdding = editTextAdding.getText().toString();
                if (!textAdding.isEmpty()){
                    adding = Integer.parseInt(textAdding);
                }
                int minutes = 1;
                String textTime = editTextTime.getText().toString();
                if (!textTime.isEmpty()){
                    minutes = Integer.parseInt(textTime);
                }
                Intent intent = TimerActivity.newIntent(MainActivity.this,
                        minutes,
                        adding
                );
                startActivity(intent);
            }
        });
    }

    private void initViews(){
        buttonStart = findViewById(R.id.buttonStart);
        editTextAdding = findViewById(R.id.editTextAdding);
        editTextTime = findViewById(R.id.editTextTime);
    }
}