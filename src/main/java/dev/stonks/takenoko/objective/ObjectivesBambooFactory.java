package dev.stonks.takenoko.objective;

import dev.stonks.takenoko.map.Improvement;
import dev.stonks.takenoko.map.TileKind;
import dev.stonks.takenoko.pattern.BambooPattern;

import java.util.ArrayList;

/**
 * Here there are create all Panda and Gardener objectives
 *
 * @author the StonksDev team
 */

public class ObjectivesBambooFactory {

    /**
     * The function to make all panda objectives;
     *
     * @return the list of the objectives (refer to the game doc for detail)
     */
    public static ArrayList<PandaObjective> pandaObjectiveList() {
        ArrayList<PandaObjective> newList = new ArrayList<>();
        BambooPattern greenPattern = new BambooPattern(TileKind.Green, 1, 2);
        BambooPattern yellowPattern = new BambooPattern(TileKind.Yellow, 1, 2);
        BambooPattern pinkPattern = new BambooPattern(TileKind.Pink, 1, 2);
        BambooPattern multiColorPattern = new BambooPattern(TileKind.Green, TileKind.Yellow, TileKind.Pink, 1, 1);
        for (int i = 0; i < 5; i++) {
            newList.add(new PandaObjective(3, greenPattern));
            if (i > 0) {
                newList.add(new PandaObjective(4, yellowPattern));
            }
            if (i > 1) {
                newList.add(new PandaObjective(5, pinkPattern));
                newList.add(new PandaObjective(6, multiColorPattern));
            }
        }
        return newList;
    }

    /**
     * The function to make all gardener objectives;
     *
     * @return the list of the objectives (refer to game doc for detail)
     */
    public static ArrayList<GardenerObjective> gardenerObjectiveList() {
        ArrayList<GardenerObjective> newList = new ArrayList<>();
        BambooPattern greenPattern = new BambooPattern(TileKind.Green, 4, 1);
        BambooPattern maxGreenPattern = new BambooPattern(TileKind.Green, 4, 4);
        BambooPattern yellowPattern = new BambooPattern(TileKind.Yellow, 4, 1);
        BambooPattern maxYellowPattern = new BambooPattern(TileKind.Yellow, 4, 3);
        BambooPattern pinkPattern = new BambooPattern(TileKind.Pink, 4, 1);
        BambooPattern maxPinkPattern = new BambooPattern(TileKind.Pink, 4, 2);

        newList.add(new GardenerObjective(3, greenPattern, Improvement.Fertilizer));
        newList.add(new GardenerObjective(4, greenPattern, Improvement.Watershed));
        newList.add(new GardenerObjective(4, greenPattern, Improvement.Enclosure));
        newList.add(new GardenerObjective(5, greenPattern, Improvement.NoImprovementHere));
        newList.add(new GardenerObjective(8, maxGreenPattern));

        newList.add(new GardenerObjective(4, yellowPattern, Improvement.Fertilizer));
        newList.add(new GardenerObjective(5, yellowPattern, Improvement.Watershed));
        newList.add(new GardenerObjective(5, yellowPattern, Improvement.Enclosure));
        newList.add(new GardenerObjective(6, yellowPattern, Improvement.NoImprovementHere));
        newList.add(new GardenerObjective(7, maxYellowPattern));

        newList.add(new GardenerObjective(5, pinkPattern, Improvement.Fertilizer));
        newList.add(new GardenerObjective(6, pinkPattern, Improvement.Watershed));
        newList.add(new GardenerObjective(6, pinkPattern, Improvement.Enclosure));
        newList.add(new GardenerObjective(7, pinkPattern, Improvement.NoImprovementHere));
        newList.add(new GardenerObjective(6, maxPinkPattern));

        return newList;
    }
}
