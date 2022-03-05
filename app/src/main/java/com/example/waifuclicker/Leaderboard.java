package com.example.waifuclicker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Leaderboard extends AppCompatActivity {

    static final String LEADERBOARD_KEY = "leaderboard";

    static final String DATAL = "DATAL";


    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leardeboard);

        if(savedInstanceState != null){
            MainActivity.leaderboardList = savedInstanceState.getStringArrayList(LEADERBOARD_KEY);
        }

        listView = findViewById(R.id.listViewId);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, MainActivity.leaderboardList);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener((adapterView, view, i, l) -> {

            String[] message = MainActivity.leaderboardList.get(i).split(":");
            String mode = message[0];
            String score = message[1];

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "I just got " + score + " points in the mode" + mode + "Waifu Clicker! Try it out!");
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);

        });

    }

    public static void addToLeaderboard(String value){
        if(MainActivity.leaderboardList.size() == 20) {
            MainActivity.leaderboardList.remove(0);
        }
        MainActivity.leaderboardList.add(value);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putStringArrayList(LEADERBOARD_KEY,  MainActivity.leaderboardList);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onStop(){
        super.onStop();
        SharedPreferences sharedPreferences = getSharedPreferences(DATAL,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LEADERBOARD_KEY, MainActivity.leaderboardList.toString());
        editor.apply();
    }
}