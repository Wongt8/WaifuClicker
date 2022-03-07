package com.example.waifuclicker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    public static ArrayList<String> leaderboardList;

    static final String SINGLE_LEADER = "leaderboardList";
    static final String DATA_MAIN = "DATAM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null){
            leaderboardList = savedInstanceState.getStringArrayList(SINGLE_LEADER);
        }
        else if (getSharedPreferences(DATA_MAIN,MODE_PRIVATE).contains(SINGLE_LEADER)){
            String temp = getSharedPreferences(DATA_MAIN,MODE_PRIVATE).getString(SINGLE_LEADER,"");
            leaderboardList = new ArrayList<>(Arrays.asList(temp.split(",")));
            if(leaderboardList.size() == 0){
                leaderboardList.add("No data yet go play some games ! \uD83C\uDFAE");
            }
            else{
                for (int i = 0; i < leaderboardList.size(); i++){
                    if (leaderboardList.get(i).equals("")){
                        leaderboardList.remove(i);
                    }
                }
            }
        }
        else leaderboardList = LeaderboardListSingleton.getInstance();

        this.removeDoubles();
        this.shortLeaderboard();
    }


    private void bouncingImage(ImageButton img){
        final Animation animation = AnimationUtils.loadAnimation(this,R.anim.bounce);
        img.startAnimation(animation);
    }

    public void clickWaifu(View view){
        ImageButton img = (ImageButton) view;
        this.bouncingImage(img);
    }

    public void choiceGameMode(View view){
        Intent intent = new Intent(this, GameModeChoice.class);
        startActivity(intent);
    }

    public void GoToLeaderboard(View view){
        Intent intent = new Intent(this, Leaderboard.class);
        startActivity(intent);
    }

    public void resetLeaderboard(View view){
        leaderboardList.clear();
        SharedPreferences.Editor editor = getSharedPreferences(DATA_MAIN,MODE_PRIVATE).edit();
        editor.putString(SINGLE_LEADER,leaderboardList.toString());
        editor.apply();
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        saveInstanceState.putStringArrayList(SINGLE_LEADER, leaderboardList);
        super.onSaveInstanceState(saveInstanceState);
    }


    @Override
    public void onStop(){
        super.onStop();
        SharedPreferences sharedPreferences = getSharedPreferences(DATA_MAIN,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SINGLE_LEADER,formatLeaderboard(leaderboardList));
        editor.apply();

    }

    private String formatLeaderboard(ArrayList<String> list){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < list.size(); i++){
            if (!list.get(i).equals("")){
                sb.append(list.get(i));
                if (i != list.size() - 1) {
                    sb.append(",");
            }
        }
        }
        return sb.toString();
    }

    private void removeDoubles(){
        for (int i = 0; i < leaderboardList.size(); i++){
            for (int j = i+1; j < leaderboardList.size(); j++){
                System.out.println(leaderboardList.get(i) + " " + leaderboardList.get(j) + " " + leaderboardList.get(i).equals(leaderboardList.get(j)));
                if (leaderboardList.get(i).equals(leaderboardList.get(j))){
                    leaderboardList.remove(j);
                }
            }
        }
    }

    private void shortLeaderboard(){
        for (int i = 0; i < leaderboardList.size(); i++){
            if (leaderboardList.get(i).contains("\uD83C\uDFC6")){
                String temp = leaderboardList.get(i);
                leaderboardList.remove(i);
                leaderboardList.add(0,temp);
            }
        }
    }
}
