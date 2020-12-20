package dev.stonks.takenoko.gameManagement;

import dev.stonks.takenoko.bot.DumbPlayer;
import dev.stonks.takenoko.bot.Player;
import dev.stonks.takenoko.bot.RandomPlayer;
import dev.stonks.takenoko.bot.SmartPlayer;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.IntStream;

/**
 * Represents the game manager. It is responsible to create a game
 * and to calculate  and display statistics.
 *
 * @author the StonksDev team
 */
public class GameManager {
    private final static Logger LOG = Logger.getLogger(GameManager.class.getSimpleName());
    int[] nbPlayers;
    ArrayList<Player> players;
    ArrayList<FinalResults> stats;

    /**
     * Initialise a game with different ia level.
     *
     * @param nbPlayers number of each type of player
     */
    public GameManager(int... nbPlayers) {
        this.nbPlayers = nbPlayers;
        this.players = createPlayers();
        initialisesStats();
    }

    private void initialisesStats() {
        stats = new ArrayList<>();
        for (Player player : players) {
            stats.add(new FinalResults(player.getId(), player.getPlayerType()));
        }
    }

    private ArrayList<Player> createPlayers() {
        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < nbPlayers[0]; i++) {
            players.add(new RandomPlayer(i));
        }
        for (int i = 0; i < nbPlayers[1]; i++) {
            players.add(new DumbPlayer(i + nbPlayers[0]));
        }
        for (int i = 0; i < nbPlayers[2]; i++) {
            players.add(new SmartPlayer(i + nbPlayers[0] + nbPlayers[1]));
        }
        return players;
    }

    /**
     * Play n time the same game with the same bot,
     * and display statistics at the end.
     *
     * @param n the numnber of games that are going to be played
     * @param parallel a boolean which indicates weither or not the game should be played in parallel
     */
    public void playNTime(int n, boolean parallel) {
        long start = System.currentTimeMillis();
        AtomicInteger count = new AtomicInteger(0);
        if (parallel) {
            if (LogManager.getLogManager().getLogger("").getHandlers()[0].getLevel().intValue() >= Level.SEVERE.intValue())
                System.out.print("Progression : ");
            IntStream.range(0, n).parallel().mapToObj(x -> new Game(createPlayers())).forEach(game -> {
                simulate(game);
                var actualCount = count.incrementAndGet();
                if (LogManager.getLogManager().getLogger("").getHandlers()[0].getLevel().intValue() >= Level.SEVERE.intValue()) {
                    float pourcentDone = actualCount / (float) n * 100;
                    String string = "Progression : "+ String.format("%4s", (int)pourcentDone + "%") +  " [" + "=".repeat((int)(pourcentDone/100f*70f)) + ">" + " ".repeat(70 - (int)(pourcentDone/100f*70f)) + "] " + actualCount + "/" + n;
                    System.out.print("\r" + string);
                }
            });
            System.out.print("\n");
        } else {
            IntStream.range(0, n).sequential().mapToObj(x -> new Game(createPlayers())).forEach(game -> {
                LOG.severe("Starting game n°" + count.incrementAndGet());
                simulate(game);
            });
        }
        long end = System.currentTimeMillis();
        LOG.severe("Time required to play " + n + " games : " + (end - start) / 1000f + " secondes");
        try {
            Thread.sleep(100);
            System.out.print("\n");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        displayStats(n);
    }

    private void simulate(Game game) {
        game.play();
        changeStats(game);
    }

    /**
     * Add the statistics of the game in the stats.
     * stats = [bot1[nbWinGame,nbLoseGame,nbDrawGame,summOfTheScore],...,botN[]]
     * stats[0] contains the statistics of the first player, stats[n]  contains the statistics of the player number n
     */
    private void changeStats(Game game) {
        ArrayList<GameResults> results = game.getResults();
        for (Player player : players) {
            stats.stream()
                    .filter(result -> result.getId() == player.getId())
                    .forEach(x -> x.change(gameStateOf(player.getId(), results), player.getScore()));
        }
    }

    /**
     * @param id the player's id
     * @param results the game result
     * @return an optional boolean for the victory
     * if it's epty, it's a draw
     */
    private Optional<Boolean> gameStateOf(int id, ArrayList<GameResults> results) {
        Optional<Boolean> victory = Optional.empty();
        boolean isDraw = false;
        int actualRank = 0;
        int nbPandaObjectivesAchieved = 0;
        for (GameResults result : results) {
            if (result.getId() == id) {
                actualRank = result.getRank();
                nbPandaObjectivesAchieved = result.getNbPandaObjectives();
            }
        }
        if (actualRank == 1) {
            for (GameResults result : results) {
                if ((result.getId() != id) && (actualRank == result.getRank())) {
                    if (nbPandaObjectivesAchieved == result.getNbPandaObjectives()) {
                        isDraw = true;
                    } else if (nbPandaObjectivesAchieved < result.getNbPandaObjectives()) {
                        victory = Optional.of(Boolean.FALSE);
                    } else {
                        victory = Optional.of(Boolean.TRUE);
                    }
                }
            }
            if (!isDraw) {
                victory = Optional.of(Boolean.TRUE);
            }
        } else {
            victory = Optional.of(Boolean.FALSE);
        }
        return victory;
    }

    /**
     * Display the statistics of victory, equality, loses of each bot
     * <p>
     * The display must include the number and percentage of games won/lost/null,
     * and the average score of each bot.
     * [bot1[nbWinGame,nbLoseGame,nbDrawGame,summOfTheScore],...,botN[]]
     */
    private void displayStats(int n) throws UnsupportedOperationException {
        System.out.println("Score final :");
        for (FinalResults result : stats) {
            displayPlayerStats(result, n);
        }
    }

    /**
     * Display all stats of one player
     *
     * @param result  the player's result
     * @param nbGames the number of games played
     */
    private void displayPlayerStats(FinalResults result, int nbGames) {
        displayWhoItIs(result.getId(), result.getPlayerType());
        System.out.println("Win games :" + result.getNbWin());
        System.out.println("Percentage of win games :" + (result.getNbWin() / (float) nbGames) * 100 + "%");
        System.out.println("Lost games :" + result.getNbLoose());
        System.out.println("Percentage of lost games :" + (result.getNbLoose() / (float) nbGames) * 100 + "%");
        System.out.println("Draw :" + result.getNbDraw());
        System.out.println("Percentage of null games :" + (result.getNbDraw() / (float) nbGames) * 100 + "%");
        System.out.println("Average score :" + result.getFinalScore() / (float) nbGames);
        System.out.println();
    }

    /**
     * Display what player it is
     *
     * @param id         the player's id
     * @param playerType the player's type
     */
    private void displayWhoItIs(int id, Player.PlayerType playerType) {
        System.out.println("Bot n°" + id + ", ia level : " + playerType);
    }
}