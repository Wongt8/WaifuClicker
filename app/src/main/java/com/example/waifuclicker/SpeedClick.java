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

public class SpeedClick extends AppCompatActivity {

    private int currentScore;
    private int highestScore;
    private boolean appWasClosedBefore;
    private int numberOfSecondForTheGame;
    private int countdown;

    static final String HIGHEST_SCORE_SPE = "playerScoreSpe";
    static final String CURRENT_SCORE_SPE = "playerCurrentScoreSpe";
    static final String COUNTDOWN = "countdown";
    static final String SECOND_OF_THE_GAME = "second";

    static final String DATA_SPE = "DATA_SPE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed_click);
        if(savedInstanceState != null) {
            this.highestScore = savedInstanceState.getInt(HIGHEST_SCORE_SPE);
            this.currentScore = savedInstanceState.getInt(CURRENT_SCORE_SPE);
            this.countdown = savedInstanceState.getInt(COUNTDOWN);
            this.numberOfSecondForTheGame = savedInstanceState.getInt(SECOND_OF_THE_GAME);
        }
        else {
            this.handleClickableWaifu(false);
            this.currentScore = 0;
            this.numberOfSecondForTheGame = 10;
            this.countdown = 5;
            this.loadData();
        }

    }


    public void start(View view) {
        this.currentScore = 0;
        this.countdown = 5;
        this.numberOfSecondForTheGame = 10;
        ThreadRunnable threadRunnable = new ThreadRunnable(this.numberOfSecondForTheGame,this.countdown);
        this.handleVisibilityAndText("START");
        new Thread(threadRunnable).start();
    }


    private void DisplayClickBySec(){
        TextView clickBySec = findViewById(R.id.cpsText);
        float cps = (float) this.currentScore/10.0f;
        clickBySec.setText(cps + " CPS");
    }

    private void getHighestScore(View view) {
        if (this.currentScore > this.highestScore) {
            TextView s = findViewById(R.id.highestScoreValueSpe);
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
        TextView currentScoreT = findViewById(R.id.scoreSpe);
        this.bouncingImage(findViewById(R.id.waifuSpe));
        this.DisplayClickBySec();

        if(this.currentScore >= 100){
            this.currentScore += Math.random() * (5-1);
        }
        else {
            this.currentScore++;
        }
        currentScoreT.setText(String.valueOf(this.currentScore));
        this.getHighestScore(view);
    }

    public void endOfTheGame(){
        Leaderboard.addToLeaderboard("\uD83D\uDCA8 Speed Mode  : " + currentScore);
        Leaderboard.addToLeaderboard("\uD83C\uDFC6 Speed Mode  : " + highestScore);

        this.handleVisibilityAndText("END");
        this.handleClickableWaifu(false);
        this.DisplayClickBySec();
        this.displayMessage("End of the game !\nYour score : " + this.currentScore);
    }

    public void handleVisibilityAndText(String value){
        if(value.equals("START")){

            TextView tl = findViewById(R.id.timeLeft);
            tl.setText("");
            findViewById(R.id.timer).setVisibility(View.VISIBLE);
            findViewById(R.id.startB).setVisibility(View.INVISIBLE);
            findViewById(R.id.cpsText).setVisibility(View.VISIBLE);
            findViewById(R.id.timeLeft).setVisibility(View.VISIBLE);
            findViewById(R.id.restDataS).setVisibility(View.INVISIBLE);
        }
        // Else it is the end of the game
        else {
            findViewById(R.id.startB).setVisibility(View.VISIBLE);
            findViewById(R.id.timer).setVisibility(View.INVISIBLE);
            findViewById(R.id.timeLeft).setVisibility(View.INVISIBLE);
            findViewById(R.id.restDataS).setVisibility(View.VISIBLE);
            TextView ct = findViewById(R.id.countdownId);
            ct.setText("");
            ct.setVisibility(View.VISIBLE);
        }
    }

    private void handleClickableWaifu(boolean value ){
        ImageButton imgB = findViewById(R.id.waifuSpe);
        imgB.setClickable(value);
    }

    private void displayMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(DATA_SPE,MODE_PRIVATE);
        this.highestScore = sharedPreferences.getInt(HIGHEST_SCORE_SPE,0);
        this.appWasClosedBefore = true;
        TextView highestScoreV = findViewById(R.id.highestScoreValueSpe);
        highestScoreV.setText(String.valueOf(highestScore));
    }

    public void restData(View view){
        this.currentScore = 0;
        this.highestScore = 0;
        SharedPreferences.Editor editor = getSharedPreferences(DATA_SPE,MODE_PRIVATE).edit();
        editor.putInt(HIGHEST_SCORE_SPE,highestScore);
        editor.apply();

        TextView currentScoreV = findViewById(R.id.scoreSpe);
        currentScoreV.setText(String.valueOf(currentScore));
        TextView highestScoreV = findViewById(R.id.highestScoreValueSpe);
        highestScoreV.setText(String.valueOf(highestScore));
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putInt(HIGHEST_SCORE_SPE, this.highestScore);
        savedInstanceState.putInt(CURRENT_SCORE_SPE, this.currentScore);
        savedInstanceState.putInt(SECOND_OF_THE_GAME,this.numberOfSecondForTheGame);
        savedInstanceState.putInt(COUNTDOWN,this.countdown);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onStop(){
        super.onStop();
        SharedPreferences sharedPreferences = getSharedPreferences(DATA_SPE,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(HIGHEST_SCORE_SPE,this.highestScore);
        editor.apply();

    }


    class ThreadRunnable implements Runnable {

        private int numberOfSecondForTheGame;
        private int countdown;

        public ThreadRunnable(int secondOfTheGame, int countdown){
            this.numberOfSecondForTheGame = secondOfTheGame;
            this.countdown = countdown;
        }

        @Override
        public void run() {
            while (countdown >= 0){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView countdownT = findViewById(R.id.countdownId);
                        if (countdown == 0) {
                            countdownT.setVisibility(View.INVISIBLE);
                            handleClickableWaifu(true);
                            TextView timeL = findViewById(R.id.timeLeft);
                            timeL.setText(String.valueOf(numberOfSecondForTheGame));
                        }
                        else countdownT.setText(String.valueOf(countdown));
                    }
                });

                try {
                    Thread.sleep( 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                countdown--;
            }

            while (numberOfSecondForTheGame >= 0){

                numberOfSecondForTheGame--;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView timeL = findViewById(R.id.timeLeft);
                        timeL.setText(String.valueOf(numberOfSecondForTheGame));
                        if(numberOfSecondForTheGame == 0) endOfTheGame();
                    }
                });

                try {
                    Thread.sleep( 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}