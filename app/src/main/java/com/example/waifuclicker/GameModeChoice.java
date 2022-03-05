package com.example.waifuclicker;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class GameModeChoice extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_mode_choice);
    }


    public void openGameMode(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.normalMode:
                intent = new Intent(this, NormalClick.class);
                startActivity(intent);
                break;
            case R.id.InfernalMode:
                intent = new Intent(this, InfernalClick.class);
                startActivity(intent);
                break;
            case R.id.speedMode:
                intent = new Intent(this, SpeedClick.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }



}