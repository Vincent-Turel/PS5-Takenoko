package dev.stonks.takenoko;

import java.util.ArrayList;

//[bot1[nbWinGame,nbLoseGame,nbDrawGame,summOfTheScore],...,botN[]]

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
