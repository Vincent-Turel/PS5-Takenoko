package dev.stonks.takenoko;

import java.util.ArrayList;

/**
 * Represents the game manager. It is responsible to create a game
 * and to calculate  and display statistics.
 *
 *
 * @author the StonksDev team
 */
public class GameManager() {
    ArrayList<Player> players;
    Game game;
    ArrayList<FinalResults> stats;

    /**
     * Initialise a game with different ia level.
     * Here, [1,1], it's 1 RandomPlayer and 1 DumbPlayer.
     * <code> new int[]{1,1}</code>
     *
     * And here, [0,2], it's 2 DumbPlayer.
     *
     */
    public GameManager(int nbRP, int nbDP) {
        initialisesPlayers(nbRP,nbDP);
        game = new Game(players);
        initialisesStats();
        stats = new ArrayList<FinalResults>();
    }

    private void initialisesStats() {
        for (Player player : players) {
            stats.add(new FinalResults(player.getId()));
        }
    }

    private void initialisesPlayers(int nbRP, int nbDP) {
            players = new ArrayList<Player>();
            for(int i = 0;i<nbRP;i++) {
                players.add(new RamdomPlayer);
            }
            for(int i = 0;i<nbDP;i++) {
                players.add(new DumbPlayer);
            }
        /*for(int i = 0;i<nbIntelligentPlayer;i++) {
            players.add(new IntelligentPlayer);
        }*/
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
            changeStats();
            game.resetGame();
        }
        displayStats(n);
    }

    /**
     * Add the statistics of the game in the stats.
     *stats = [bot1[nbWinGame,nbLoseGame,nbDrawGame,summOfTheScore],...,botN[]]
     * stats[0] contains the statistics of the first player, stats[n]  contains the statistics of the player number n
     */
    private void changeStats() {

        ArrayList<GameResults> results = game.getResults();
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

    private void changeStats2() {
        ArrayList<GameResults> results = game.getResults();
        int score = 0;
        for (Player player : players) {
            score = player.getScore();
            stats.stream().filter(result -> result.getId()== player.getId()).change( gameStateOf(player.getId()),score);
        }
    }

    public int gameStateOf(int id){
        
        for (Player player : players) {
            score = player.getScore();
            stats.stream().filter(result -> result.getId()== player.getId()).change( gameStateOf(player.getId()),score);
        }
        return gameState;
    }

    //players.stream().filter(player -> player.getId()==id).mapToInt(player -> player.getScore())
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
     * [bot1[nbWinGame,nbLoseGame,nbDrawGame,summOfTheScore],...,botN[]]
     *
     */
    private void displayStats(int n) throws UnsupportedOperationException {
        System.out.println("Score final :");
        for (int i = 0;i<stats.length;i++) {
            displayPlayerStats(stats[i],i,n);
        }
    }
    private void displayPlayerStats(int[] stats,int number,int nbGames){
        displayWhoItIs();
        System.out.println("Win games :" + stats[0]);
        System.out.println("Percentage of win games :" + (stats[0]/nbGames)*100 + "%");
        System.out.println("Lost games :" + stats[1]);
        System.out.println("Percentage of lost games :" + (stats[1]/nbGames)*100 + "%");
        System.out.println("Draw :" + stats[2]);
        System.out.println("Percentage of null games :" + (stats[2]/nbGames)*100 + "%");
        int averageScore = (stats[3]/nbGames);
        System.out.println("Average score :" + averageScore);

    }

    private void displayWhoItIs() {
    }
}