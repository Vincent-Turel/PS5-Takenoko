package dev.stonks.takenoko;

import java.util.*;

public class ObjectivesMaker {

    /**
     * ObjectivesMaker : it's all function to make new objective
     *
     * @param nbPT nbPT is the number of points give when success
     * @return return objective or null if the new objective already exist, it has the null value.
     *
     * @author the StonksDev team
     *
     * objType -> Pattern, Gardener, Panda, Emperor
     */

    /**
     * Make a pattern objective :
     * @param nbPT -> n° of point
     * @param pattern -> tile pattern to complete objective
     * @return
     */
    public static PatternObjective newPatternObjectives(int nbPT,Pattern pattern) {
        PatternObjective newObj = new PatternObjective(ObjectiveKind.Pattern, nbPT, pattern);
        return newObj;
        }

    /**
     * Make a panda objective :
     * @param nbPT -> n° of point
     * @param bamboo -> bamboo pattern to complete objective
     * @return
     */
    public static PandaObjective newPandaObjectives(int nbPT,BambooPattern bamboo) {
        PandaObjective newObj = new PandaObjective(ObjectiveKind.Panda, nbPT, bamboo);
        return newObj;
    }

    /**
     * Make a gardener objective :
     * @param nbPT -> n° of point
     * @param bamboo -> bamboo pattern to complete objective
     * @return
     */
    public static GardenerObjective newGardenerObjectives(int nbPT,BambooPattern bamboo) {
        GardenerObjective newObj = new GardenerObjective(ObjectiveKind.Gardener, nbPT, bamboo);
        return newObj;
    }

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
