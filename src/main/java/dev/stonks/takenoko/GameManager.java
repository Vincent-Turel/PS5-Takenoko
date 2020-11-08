package dev.stonks.takenoko;

import java.util.ArrayList;
import java.util.UnknownFormatConversionException;

/**
 * Represents the game manager. It is responsible to create a game
 * and to calculate  and display statistics.
 *
 *
 * @author the StonksDev team
 */
public class GameManager {
    int[] nbIa;
    Game game;
    int[][] stats;

    /**
     * Initialise a game with different ia level.
     * Here, [1,1], it's 1 RandomPlayer and 1 DumbPlayer.
     * <code> new int[]{1,1}</code>
     *
     */
    public GameManager(int[] numberOfIa) {
        nbIa = numberOfIa;
        game = new Game(nbIa);
        stats = new int[nbIa.length][3];
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
     * stats[0] contains the statistics of the first player, stats[n]  contains the statistics of the player number n
     * @param game
     */
    private void changeStats(Game game) {
        int[] results = game.getResults();
        boolean draw = checkDraw(results);
        for(int i = 0;i < stats.length; i++) {
            if(draw){
                if(results[i]==1){
                    stats[i][2]+=1;
                }
                else{
                    stats[i][1]+=1;
                }
            }
            else{
                if(results[i]==1){
                    stats[i][0]+=1;
                }
                else{
                    stats[i][1]+=1;
                }
            }
        }
    }

    private boolean checkDraw(int[] results) {
        boolean foundOneTime = false;
        for(int i = 0;i < results.length; i++) {
            if (!foundOneTime){
                if (results[i]==1){
                    foundOneTime = true;
                }
            }
            else if (foundOneTime){
                if (results[i]==1){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Display the statistics of victory, equality, loses of each bot
     *
     * The display must include the number and percentage of games won/lost/null,
     * and the average score of each bot.
     *
     */
    private void displayStats() throws UnsupportedOperationException {
        System.out.println("Score final :");
        for (Player player: game.players) {
            displayHisStats(player);
        }
    }
    private void displayHisStats(Player player){

    }
}