package dev.stonks.takenoko;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Represents a game. It is responsible to create Players, a map, objectives.
 *
 * finalState is the state at the end of the game,
 * it contains the rank of each player.
 *
 * @author the StonksDev team
 */
public class Game {
    Map map;
    ArrayList<Player> players;
    ArrayList<Objective> objectives;
    int finalState[];

    public Game(int[] nbIa) {
        map = new Map();
        finalState = new int[]{};
        initialisesObjectives();
        initialisesPlayers(nbIa);
    }

    /**
     * Initialise the objectives (here, it's 10 tile objectives)
     *
     * @param objectives
     */
    private void initialisesObjectives() {
        ObjectivesMaker objectivesMaker = new ObjectivesMaker();
        for(int i = 0;i < 5;i++){
            objectives.add(objectivesMaker.addAnObjectivies(i,2,2));
            objectives.add(objectivesMaker.addAnObjectivies(i+5,3,3));
        }
    }

    /**
     * Initialise n players
     *
     * nbIa is an array of int, it contains the nb of ia
     * for each ia level from the stupidest to the most intelligent.
     *
     * @param nbIa
     */
    private void initialisesPlayers(int[] nbIa) {
        for(int i = 0;i<nbIa.length;i++) {
            switch (i) {
                case 0:
                    for(int j = 0;i<nbIa[i];j++) {
                        players.add(new DumbPlayer);
                    }
                    break;
                case 1:
                    for(int j = 0;i<nbIa[i];j++) {
                        players.add(new RamdomPlayer);
                    }
                    break;
            }
        }
    }

    void play(){
        while(noPlayerWin()){
            for (Player player: players) {
                player.decide(map);
                player.doActions();
                player.checkObjectives();
            }
        }
    }

    void resetGame(){
        resetMap();
        resetPlayers();
        resetObjectives();
    }

    private void resetMap() {

    }

    private void resetPlayers(){

    }

    private void resetObjectives(){

    }
}
