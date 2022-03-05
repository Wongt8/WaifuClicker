package com.example.waifuclicker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NormalClick extends AppCompatActivity {

    private int currentScore;
    private int highestScore;
    private boolean appWasClosedBefore;

    static final String HIGHEST_SCORE = "playerScore";
    static final String CURRENT_SCORE = "playerCurrentScore";

    static final String DATA = "DATA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_click);
        if(savedInstanceState != null) {
            this.highestScore = savedInstanceState.getInt(HIGHEST_SCORE);
            this.currentScore = savedInstanceState.getInt(CURRENT_SCORE);
        }
        else {
            this.currentScore = 0;
            this.loadData();
        }
    }

    private void getHighestScore(View view) {
        if (this.currentScore > this.highestScore) {
            TextView s = findViewById(R.id.highestScoreValue);
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
        TextView currentScoreT = findViewById(R.id.score);

        this.bouncingImage(findViewById(R.id.waifu));

        if(this.currentScore >= 100){
            this.currentScore += Math.random() * (5-1);
        }
        else {
            this.currentScore++;
        }
        currentScoreT.setText(String.valueOf(this.currentScore));
        this.getHighestScore(view);
    }

    public void restData(View view){
        this.currentScore = 0;
        this.highestScore = 0;
        SharedPreferences.Editor editor = getSharedPreferences(DATA,MODE_PRIVATE).edit();
        editor.putInt(HIGHEST_SCORE,highestScore);
        editor.apply();

        TextView currentScoreV = findViewById(R.id.score);
        currentScoreV.setText(String.valueOf(currentScore));
        TextView highestScoreV = findViewById(R.id.highestScoreValue);
        highestScoreV.setText(String.valueOf(highestScore));
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putInt(HIGHEST_SCORE, highestScore);
        savedInstanceState.putInt(CURRENT_SCORE, currentScore);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onStop(){
        super.onStop();
        Leaderboard.addToLeaderboard("\uD83C\uDF86 Normal Mode  : " + currentScore);
        Leaderboard.addToLeaderboard("\uD83C\uDFC6 Normal Mode  : " + highestScore);
        SharedPreferences sharedPreferences = getSharedPreferences(DATA,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(HIGHEST_SCORE,this.highestScore);
        editor.apply();

    }

    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(DATA,MODE_PRIVATE);
        this.highestScore = sharedPreferences.getInt(HIGHEST_SCORE,0);
        this.appWasClosedBefore = true;
        TextView highestScoreV = findViewById(R.id.highestScoreValue);
        highestScoreV.setText(String.valueOf(highestScore));
    }


}