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
     *
     * @param nbRP is the number of random player
     * @param nbDP is the number of dumb player
     */
    public GameManager(int nbRP, int nbDP) {
        initialisesPlayers(nbRP,nbDP);
        game = new Game(players);
        initialisesStats();
    }

    private void initialisesStats() {
        stats = new ArrayList<FinalResults>();
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
     * stats = [bot1[nbWinGame,nbLoseGame,nbDrawGame,summOfTheScore],...,botN[]]
     * stats[0] contains the statistics of the first player, stats[n]  contains the statistics of the player number n
     */
    private void changeStats() {
        ArrayList<GameResults> results = game.getResults();
        int score = 0;
        for (Player player : players) {
            score = player.getScore();
            stats.stream().filter(result -> result.getId()== player.getId()).change(gameStateOf(player.getId(),results),score);
        }
    }

    /**
     *
     * @param id
     * @param id
     * @return the state of the game for one player :
     * it can be a victory, a loose, or a draw
     *
     */
    public int gameStateOf(int id, ArrayList<GameResults> results){
        int gameState = 0;
        boolean isDraw = false;
        int actualRank = 0;
        for (GameResults result: results) {
            if(result.getId()==id){
                actualRank = result.getRank();
            }
        }
        if(actualRank==1) {
            for (GameResults result : results) {
                if ((result.getId() != id) && (actualRank == result.getRank())) {
                    gameState = 22;//here, the draw is everything else than 0 or 1 (loose or win)
                    isDraw = true;
                }
            }
            if(!isDraw){
                gameState=1;
            }
        }
        return gameState;
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
        for (FinalResults result : stats) {
            displayPlayerStats(result,n);
        }
    }

    /**
     * Display all stats of one player
     *
     * @param result
     * @param nbGames
     */
    private void displayPlayerStats(FinalResults result ,int nbGames){
        displayWhoItIs(result.getId());
        System.out.println("Win games :" + result.getNbWin());
        System.out.println("Percentage of win games :" + (result.getNbWin()/nbGames)*100 + "%");
        System.out.println("Lost games :" + result.getNbLoose());
        System.out.println("Percentage of lost games :" + (result.getNbLoose()/nbGames)*100 + "%");
        System.out.println("Draw :" + result.getNbDraw());
        System.out.println("Percentage of null games :" + (result.getNbDraw()/nbGames)*100 + "%");
        System.out.println("Average score :" + result.getFinalScore()/nbGames);

    }

    /**
     * Display what player it is
     * @param id
     */
    private void displayWhoItIs(int id) {
        //TODO:display of the ia level ?
        System.out.println("Bot n°"+ id + ", ia level : ");
    }
}