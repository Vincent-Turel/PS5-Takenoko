package dev.stonks.takenoko;

import java.util.ArrayList;

/**
 * Represents the game manager. It is responsible to create a game
 * and to calculate  and display statistics.
 *
 *
 * @author the StonksDev team
 */
public class GameManager {
    Game game;
    ArrayList<int[]> stats;

    /**
     * Initialise a game with different ia level.
     * Here, it's 1 DumbPlayer and 1 DumbPlayer.
     *
     */
    public GameManager() {
        game = new Game(new int[]{1,1});
    }

    /**
     * Play n time the same game with the same bot,
     * and display statistics at the end.
     *
     * @param n
     */
    void playNTime(int n) {
        for (int i = 0; i < n; i++) {
            game.play();
            changeStats(game);
            game.resetGame();
        }
        displayStats();
    }

    /**
     * Add the statistics of the game in the stats.
     *stats = [bot1[nbWinGame,nbLoseGame,nbDrawGame],...,botN[]]
     * @param game
     */
    private void changeStats(Game game) {
        stats[0]+=;
    }

    /**
     * Display the statistics of victory, equality, loses of each bot
     *
     * The display must include the number and percentage of games won/lost/null,
     * and the average score of each bot.
     *
     * @param stats
     */
    private void displayStats() {
        System.out.println("Score final :");
        for (Player player: game.players) {
            displayHisStats(player);
        }
    }
    private void displayHisStats(Player player){

    }
}