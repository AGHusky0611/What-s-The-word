package client_java.model.player;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardModel {
    private List<String> topPlayers;
    private List<Integer> topWins;

    public LeaderboardModel() {
        topPlayers = new ArrayList<>();
        topWins = new ArrayList<>();
    }

    public void fetchLeaderboardData() {
        //todo - get the data
        // Clear current data
        topPlayers.clear();
        topWins.clear();

        // Mock data - replace with actual server call
        topPlayers.add("Player1");
        topPlayers.add("Player2");
        topPlayers.add("Player3");
        topPlayers.add("Player4");
        topPlayers.add("Player5");

        topWins.add(15);
        topWins.add(12);
        topWins.add(10);
        topWins.add(8);
        topWins.add(5);
    }

    public List<String> getTopPlayers() {
        return new ArrayList<>(topPlayers);
    }

    public List<Integer> getTopWins() {
        return new ArrayList<>(topWins);
    }

    // This would be called when we have real server connection
    public void updateFromServer(List<String> players, List<Integer> wins) {
        topPlayers = new ArrayList<>(players);
        topWins = new ArrayList<>(wins);
    }
}