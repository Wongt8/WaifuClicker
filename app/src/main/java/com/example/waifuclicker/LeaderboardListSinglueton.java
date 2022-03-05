package com.example.waifuclicker;

import java.util.ArrayList;

public class LeaderboardListSinglueton {

    private static ArrayList<String> leaderboardList = null;

    public static ArrayList<String> getInstance() {
        if (leaderboardList == null) {
            leaderboardList = new ArrayList<>(20);
        }
        return leaderboardList;
    }

}
