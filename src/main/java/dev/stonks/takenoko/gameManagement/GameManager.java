package dev.stonks.takenoko.gameManagement;

import dev.stonks.takenoko.bot.Player;
import dev.stonks.takenoko.bot.SmartPlayer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
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
    private final List<Player> players;
    private final boolean fullResult;
    private final boolean ugly;
    private ArrayList<FinalResults> stats;


    /**
     * Initialise a game with different ia level.
     *
     * @param players the players
     */
    public GameManager(List<Player> players, boolean fullResult, boolean ugly) {
        this.players = players;
        this.fullResult = fullResult;
        this.ugly = ugly;
        initialisesStats();
    }

    private void initialisesStats() {
        stats = new ArrayList<>();
        for (Player player : players) {
            stats.add(new FinalResults(player));
        }
    }

    /**
     * Create all the players for the simulation
     *
     * @return an arraysList of all the players
     */
    private ArrayList<Player> getNewPlayers() {
        ArrayList<Player> players = new ArrayList<>();
        this.players.forEach(player -> players.add(player.getNewInstance()));
        return players;
    }

    /**
     * Play n time the same game with the same bots,
     * and display statistics at the end.
     *
     * @param n          the numnber of games that are going to be played
     * @param sequential a boolean which indicates weither or not the game should be played in parallel.
     */
    public void playNTime(int n, boolean sequential) {
        long start = System.currentTimeMillis();
        AtomicInteger count = new AtomicInteger(0);
        if (!sequential) {
            updateProgressBar(n, 0);
            IntStream.range(0, n).parallel().mapToObj(x -> new Game(getNewPlayers())).forEach(game -> {
                simulate(game);
                updateProgressBar(n, count.incrementAndGet());
            });
            System.out.print("\n");
        } else {
            IntStream.range(0, n).sequential().mapToObj(x -> new Game(getNewPlayers())).forEach(game -> {
                LOG.severe("Starting game n°" + count.incrementAndGet());
                simulate(game);
            });
        }
        long end = System.currentTimeMillis();
        System.out.println("\n");
        LOG.severe("Time required to play " + n + " games : " + (end - start) / 1000f + " secondes");
        try {
            Thread.sleep(100);
            System.out.print("\n");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        displayStats(n);
    }

    /**
     * Display the progress bar curent state
     *
     * @param n           the number of games to run
     * @param actualCount the actuel number of games that have already been runned
     */
    private void updateProgressBar(int n, int actualCount) {
        if (LogManager.getLogManager().getLogger("").getHandlers()[0].getLevel().intValue() >= Level.SEVERE.intValue()) {
            float pourcentDone = actualCount / (float) n * 100;
            System.out.print("\r" + "Progression : " + String.format("%4s", (int) pourcentDone + "%")
                    + (ugly ? " [" : " 〈") + "═".repeat((int) (pourcentDone / 100f * 70f))
                    + " ".repeat(70 - (int) (pourcentDone / 100f * 70f)) + (ugly ? "] " : "〉 ") + actualCount + "/" + n);
        }
    }

    /**
     * Play a game and get the result
     *
     * @param game the game to simulate
     */
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
            int score = results.stream().filter(x -> x.getId() == player.getId()).findFirst().get().getScore();
            stats.stream()
                    .filter(result -> result.getId() == player.getId())
                    .forEach(x -> x.change(gameStateOf(player.getId(), results), score));
        }
    }

    /**
     * @param id      the player's id
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
            if (!isDraw && victory.isEmpty()) {
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
        if (fullResult) {
            System.out.println("Score final :");
            for (FinalResults result : stats) {
                displayPlayerStats(result, n);
            }
        } else {
            if (ugly) {
                printResultInArray("┌", "└", "┐", "┘", "─",
                        "│", "├", "┤", "┴", "┬",
                        "┼", 25, 12, new DecimalFormat("0.00"), n);
            } else {
                printResultInArray("╭", "╰", "╮", "╯", "─",
                        "│", "├", "┤", "┴", "┬",
                        "┼", 25, 12, new DecimalFormat("0.00"), n);
            }
        }

    }

    /**
     * Print the result in an array.
     */
    private void printResultInArray(String leftUpAngle, String leftDownAngle, String rightUpAngle,
                                    String rightDownAngle, String hLine, String vLine, String vRightLine,
                                    String vLeftLine, String hUpLine, String hDownLine, String intersection,
                                    int width, int smallWidth, DecimalFormat df, int numberOfGames) {

        System.out.println(" ");
        System.out.println(" ".repeat(smallWidth + 2) + StringUtils.center("FINAL SCORE", players.size() * (width) + players.size() - 1));
        System.out.println(" ");
        System.out.print(" ".repeat(smallWidth + 1) + leftUpAngle);
        for (int i = 1; i < players.size(); i++) {
            System.out.print(hLine.repeat(width) + hDownLine);
        }
        System.out.print(hLine.repeat(width) + rightUpAngle);
        System.out.print("\n" + " ".repeat(smallWidth + 1) + vLine);
        for (FinalResults results : stats) {
            System.out.print(StringUtils.center("Bot n°" + results.getId(), width) + vLine);
        }
        System.out.print("\n" + " ".repeat(smallWidth + 1) + vLine);
        for (FinalResults result : stats) {
            if (result.getPlayerType().equals("SmartPlayer")) {
                SmartPlayer smartPlayer = (SmartPlayer) players.stream().filter(player -> player.getId() == result.getId()).findAny().orElseThrow(NoSuchElementException::new);
                System.out.print(StringUtils.center("IA : " + result.getPlayerType() + " (" + smartPlayer.getDepth() + ")", width) + vLine);
            } else
                System.out.print(StringUtils.center("IA : " + result.getPlayerType(), width) + vLine);
        }
        System.out.print("\n" + leftUpAngle);
        System.out.print(hLine.repeat(smallWidth) + intersection);
        for (int i = 1; i < players.size(); i++) {
            System.out.print(hLine.repeat(width) + intersection);
        }
        System.out.print(hLine.repeat(width) + vLeftLine);
        System.out.print("\n" + vLine);
        System.out.print(StringUtils.center("Victoire", smallWidth) + vLine);
        for (FinalResults result : stats) {
            System.out.print(StringUtils.center(df.format((result.getNbWin() / (float) numberOfGames) * 100) + "%", width) + vLine);
        }
        System.out.print("\n" + leftDownAngle);
        System.out.print(hLine.repeat(smallWidth) + hUpLine);
        for (int i = 1; i < players.size(); i++) {
            System.out.print(hLine.repeat(width) + hUpLine);
        }
        System.out.println(hLine.repeat(width) + rightDownAngle);
        System.out.println(leftUpAngle + hLine.repeat(width) + rightUpAngle);
        System.out.println(vLine + StringUtils.center("Égalité : " + df.format(stats.get(0).getNbDraw()) + "%", width) + vLine);
        System.out.println(leftDownAngle + hLine.repeat(width) + rightDownAngle);
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
    private void displayWhoItIs(int id, String playerType) {
        SmartPlayer smartPlayer = null;
        if (playerType.equals("SmartPlayer"))
            smartPlayer = (SmartPlayer) players.stream().filter(player -> player.getId() == id).findAny().orElseThrow(NoSuchElementException::new);

        System.out.println("Bot n°" + id + " - IA level : " + playerType + (smartPlayer != null ? " - Depth : " + smartPlayer.getDepth() : ""));
    }
}