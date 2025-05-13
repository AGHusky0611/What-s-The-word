package client_java.controller.player;

import client_java.model.player.LeaderboardModel;
import client_java.view.player.HomeScreenUI;
import client_java.view.player.LeaderboardUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class LeaderboardController {
    private final LeaderboardUI view;
    private final LeaderboardModel model;

    public LeaderboardController(LeaderboardUI view, LeaderboardModel model) {
        this.view = view;
        this.model = model;

        initialize();
    }

    private void initialize() {
        setupBackButton();
        loadLeaderboardData();
    }

    private void setupBackButton() {
        view.getBackButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
                new HomeScreenUI(); // Assuming HomeScreenUI exists
            }
        });
    }

    private void loadLeaderboardData() {
        // Fetch data from model (which would get it from server)
        model.fetchLeaderboardData();

        // Update view with the data
        List<String> players = model.getTopPlayers();
        List<Integer> wins = model.getTopWins();

        view.setLeaderBoardRanking(players, wins);
    }

    // This would be called when we need to refresh the leaderboard
    public void refreshLeaderboard() {
        loadLeaderboardData();
    }
}