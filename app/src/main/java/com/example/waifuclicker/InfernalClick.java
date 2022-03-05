package com.example.waifuclicker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class InfernalClick extends AppCompatActivity {

    private int currentScore;
    private int highestScore;
    private boolean appWasClosedBefore;
    private boolean threadRunning;

    static final String HIGHEST_SCORE_INF = "playerScoreInf";
    static final String CURRENT_SCORE_INF = "playerCurrentScoreInf";
    static final String IS_THREAD_RUNNING_INF = "threadRunning";

    static final String DATA_INF = "DATA_INF";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infernal_click);
        if(savedInstanceState != null) {
            this.highestScore = savedInstanceState.getInt(HIGHEST_SCORE_INF);
            this.currentScore = savedInstanceState.getInt(CURRENT_SCORE_INF);
            this.threadRunning = savedInstanceState.getBoolean(IS_THREAD_RUNNING_INF);
        }
        else {
            this.currentScore = 0;
            this.threadRunning = false;
            this.loadData();
        }

    }

    private void getHighestScore(View view) {
        if (this.currentScore > this.highestScore) {
            TextView s = findViewById(R.id.highestScoreValueInf);
            s.setText(String.valueOf(currentScore));
            this.highestScore = this.currentScore;

            if(this.appWasClosedBefore){
                Toast.makeText(this,"New highest Score !",Toast.LENGTH_SHORT).show();
                this.appWasClosedBefore = false;
            }
        }
    }

    private void bouncingImage(ImageButton img){
        final Animation animation = AnimationUtils.loadAnimation(this,R.anim.bounce);
        img.startAnimation(animation);
    }

    public void Click(View view){
        TextView currentScoreT = findViewById(R.id.scoreInf);
        this.bouncingImage(findViewById(R.id.waifuInf));

        if(this.currentScore >= 100){
            this.currentScore += Math.random() * (5-1);
        }
        else {
            this.currentScore++;
        }
        currentScoreT.setText(String.valueOf(this.currentScore));
        this.getHighestScore(view);
        if(!this.threadRunning) this.infernalDecrease(view);
    }

    public void infernalDecrease(View v){
        this.threadRunning = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep( Math.abs(2000 - currentScore) + 100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    currentScore--;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView currentScoreT = findViewById(R.id.scoreInf);
                            currentScoreT.setText(String.valueOf(currentScore));
                        }
                    });

                }
            }
        }).start();
    }


    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(DATA_INF,MODE_PRIVATE);
        this.highestScore = sharedPreferences.getInt(HIGHEST_SCORE_INF,0);
        this.appWasClosedBefore = true;
        TextView highestScoreV = findViewById(R.id.highestScoreValueInf);
        highestScoreV.setText(String.valueOf(highestScore));
    }

    public void restData(View view){
        this.currentScore = 0;
        this.highestScore = 0;
        SharedPreferences.Editor editor = getSharedPreferences(DATA_INF,MODE_PRIVATE).edit();
        editor.putInt(HIGHEST_SCORE_INF,highestScore);
        editor.apply();

        TextView currentScoreV = findViewById(R.id.scoreInf);
        currentScoreV.setText(String.valueOf(currentScore));
        TextView highestScoreV = findViewById(R.id.highestScoreValueInf);
        highestScoreV.setText(String.valueOf(highestScore));
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putInt(HIGHEST_SCORE_INF, this.highestScore);
        savedInstanceState.putInt(CURRENT_SCORE_INF, this.currentScore);
        savedInstanceState.putBoolean(IS_THREAD_RUNNING_INF,this.threadRunning);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onStop(){
        Leaderboard.addToLeaderboard("\uD83D\uDD25 Infernal Mode : " + currentScore);
        Leaderboard.addToLeaderboard("\uD83C\uDFC6 Infernal Mode  : " + highestScore);
        super.onStop();
        SharedPreferences sharedPreferences = getSharedPreferences(DATA_INF,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(HIGHEST_SCORE_INF,this.highestScore);
        editor.apply();

    }
}