package dev.stonks.takenoko;

import java.util.*;

public class ObjectivesMaker {

    private List<Integer> listObjectves;

    ObjectivesMaker(){listObjectves = new ArrayList<>();
    }
    /**
     * addAnObjectives it's the factory to make new objective
     *
     * @param nbPT nbPT is the number of points give when success
     * @return return objective or null if the new objective already exist, it has the null value.
     *
     * @author the StonksDev team
     */
    PatternObjective addAnPatternObjectives(int nbPT, int objType, Pattern pattern) {
        int objID = nextObjectiveId();
        for (int value : this.listObjectves) {
            if (value == objID) {
                return null;
            }
        }

        listObjectves.add(objID);

        PatternObjective patternObj = new PatternObjective(objID, nbPT, pattern);
        return patternObj;
    }

    public List<Integer> getlistObjectves(){return listObjectves;}

    /**
     * Returns the next available objective ID
     */
    int nextObjectiveId() {
        return listObjectves
                .stream()
                .max(Comparator.naturalOrder())
                .map(x -> x + 1)
                .orElse(0);
    }

    /**
     * Returns every valid pattern objective available in the game.
     */
    ArrayList<PatternObjective> validPatternObjectives() {
        ArrayList<PatternObjective> ps = new ArrayList();

        ps.add(addAnPatternObjectives( 2, 1, Pattern.triangleShaped(TileKind.Green)));
        ps.add(addAnPatternObjectives( 3, 1, Pattern.diamondShaped(TileKind.Green)));
        ps.add(addAnPatternObjectives( 5, 1, Pattern.diamondShaped(TileKind.Yellow, TileKind.Pink)));
        ps.add(addAnPatternObjectives( 4, 1, Pattern.diamondShaped(TileKind.Pink, TileKind.Green)));
        ps.add(addAnPatternObjectives( 3, 1, Pattern.diamondShaped(TileKind.Yellow, TileKind.Green)));

        ps.add(addAnPatternObjectives( 3, 1, Pattern.iShaped(TileKind.Yellow)));
        ps.add(addAnPatternObjectives( 4, 1, Pattern.diamondShaped(TileKind.Yellow)));
        ps.add(addAnPatternObjectives( 3, 1, Pattern.cShaped(TileKind.Yellow)));
        ps.add(addAnPatternObjectives( 2, 1, Pattern.iShaped(TileKind.Green)));
        ps.add(addAnPatternObjectives( 2, 1, Pattern.cShaped(TileKind.Green)));

        ps.add(addAnPatternObjectives(4, 1, Pattern.cShaped(TileKind.Pink)));
        ps.add(addAnPatternObjectives(4, 1, Pattern.triangleShaped(TileKind.Pink)));
        ps.add(addAnPatternObjectives(5, 1, Pattern.diamondShaped(TileKind.Pink)));
        ps.add(addAnPatternObjectives(4, 1, Pattern.iShaped(TileKind.Pink)));
        ps.add(addAnPatternObjectives(3, 1, Pattern.triangleShaped(TileKind.Yellow)));

        return ps;
    }
}
