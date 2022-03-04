package com.example.jeuvaisseaux;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button button_play_Joy = findViewById(R.id.button_play_joy);
        button_play_Joy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startGame = new Intent(MainMenu.this, MainGameJoy.class);
                startActivity(startGame);
            }
        });

        Button button_play_Acc = findViewById(R.id.button_play_acc);
        button_play_Acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startGame = new Intent(MainMenu.this, MainGameAcc.class);
                startActivity(startGame);
            }
        });

        Button button_exit = findViewById(R.id.button_exit);
        button_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}