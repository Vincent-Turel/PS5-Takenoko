package dev.stonks.takenoko.gameManagement;

import dev.stonks.takenoko.bot.DumbPlayer;
import dev.stonks.takenoko.bot.Player;
import dev.stonks.takenoko.bot.RandomPlayer;
import dev.stonks.takenoko.bot.SmartPlayer;

import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Represents the game manager. It is responsible to create a game
 * and to calculate  and display statistics.
 *
 *
 * @author the StonksDev team
 */
public class GameManager {
    private final static Logger LOG = Logger.getLogger(GameManager.class.getSimpleName());
    ArrayList<Player> players;
    public Game game;
    ArrayList<FinalResults> stats;

    /**
     * Initialise a game with different ia level.
     *
     * @param nbRP is the number of random player
     * @param nbDP is the number of dumb player
     */
    public GameManager(int nbRP, int nbDP, int nbSP) {
        initialisesPlayers(nbRP, nbDP, nbSP);
        game = new Game(players);
        initialisesStats();
    }

    private void initialisesStats() {
        stats = new ArrayList<FinalResults>();
        for (Player player : players) {
            stats.add(new FinalResults(player.getId(),player.getPlayerType()));
        }
    }

    private void initialisesPlayers(int nbRandomPlayer, int nbDumbPlayer, int nbSmartPlayer) {
            players = new ArrayList<>();
            for(int i = 0;i<nbRandomPlayer;i++) {
                players.add(new RandomPlayer(i));
            }
            for(int i = 0;i<nbDumbPlayer;i++) {
                players.add(new DumbPlayer(i+nbRandomPlayer));
            }
            for(int i = 0;i<nbSmartPlayer;i++) {
            players.add(new SmartPlayer(i + nbRandomPlayer + nbDumbPlayer));
            }
    }

    /**
     * Play n time the same game with the same bot,
     * and display statistics at the end.
     *
     * @param n the numnber of games that are going to be played
     */
    public void playNTime(int n) {
        for (int i = 1; i <= n; i++) {
            LOG.severe("Starting game n°" + i);
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
        for (Player player : players) {
            stats.stream()
                 .filter(result -> result.getId()== player.getId())
                 .forEach(x -> x.change(gameStateOf(player.getId(),results),player.getScore()));
        }
    }

    /**
     *
     * @param id
     * @param results
     * @return an optional boolean for the victory
     * if it's epty, it's a draw
     */
    private Optional<Boolean> gameStateOf(int id, ArrayList<GameResults> results){
        Optional<Boolean> victory = Optional.empty();
        boolean isDraw = false;
        int actualRank = 0;
        int nbPandaObjectivesAchieved = 0;
        for (GameResults result: results) {
            if(result.getId()==id){
                actualRank = result.getRank();
                nbPandaObjectivesAchieved = result.getNbPandaObjectives();
            }
        }
        if(actualRank==1) {
            for (GameResults result : results) {
                if ((result.getId() != id) && (actualRank == result.getRank())) {
                    if(nbPandaObjectivesAchieved==result.getNbPandaObjectives()) {
                        isDraw = true;
                    }
                    else if(nbPandaObjectivesAchieved<result.getNbPandaObjectives()){
                        victory = Optional.of(Boolean.FALSE);
                    }
                    else{
                        victory = Optional.of(Boolean.TRUE);
                    }
                }
            }
            if(!isDraw){
                victory = Optional.of(Boolean.TRUE);
            }
        }
        else{
            victory = Optional.of(Boolean.FALSE);
        }
        return victory;
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
        displayWhoItIs(result.getId(),result.getPlayerType());
        System.out.println("Win games :" + result.getNbWin());
        System.out.println("Percentage of win games :" + (result.getNbWin()/(float)nbGames)*100 + "%");
        System.out.println("Lost games :" + result.getNbLoose());
        System.out.println("Percentage of lost games :" + (result.getNbLoose()/(float)nbGames)*100 + "%");
        System.out.println("Draw :" + result.getNbDraw());
        System.out.println("Percentage of null games :" + (result.getNbDraw()/(float)nbGames)*100 + "%");
        System.out.println("Average score :" + result.getFinalScore()/(float)nbGames);
        System.out.println();
    }

    /**
     * Display what player it is
     * @param id
     * @param playerType
     */
    private void displayWhoItIs(int id, Player.PlayerType playerType) {
        System.out.println("Bot n°"+ id + ", ia level : "+playerType);
    }
}