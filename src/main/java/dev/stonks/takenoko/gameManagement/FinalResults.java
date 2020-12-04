package dev.stonks.takenoko.gameManagement;

import java.util.ArrayList;

//[bot1[nbWinGame,nbLoseGame,nbDrawGame,summOfTheScore],...,botN[]]

/**
 * Final result of a player with his statistics
 *
 */
public class FinalResults {
    private static int isAWin = 1;
    private static int isALoose = 0;
    private int id;
    private int nbWin;
    private int nbLoose;
    private int nbDraw;
    private int finalScore;

    public FinalResults(int id) {
        this.id = id;
        nbWin = 0;
        nbLoose = 0;
        nbDraw = 0;
        finalScore = 0;
    }

    /**
     * Change the final statistics of a player
     *
     * @param game is the a int, it design if it's a victory, a loose, or a draw
     * @param score
     */
    public void change(int game, int score){
        if(game==isAWin){
            nbWin++;
        }
        else if(game==isALoose){
            nbLoose++;
        }
        else{
            nbDraw++;
        }
        finalScore += score;
    }

    public int getId() {
        return id;
    }

    public int getNbWin() {
        return nbWin;
    }

    public int getNbLoose() {
        return nbLoose;
    }

    public int getNbDraw() {
        return nbDraw;
    }

    public int getFinalScore() {
        return finalScore;
    }

}
