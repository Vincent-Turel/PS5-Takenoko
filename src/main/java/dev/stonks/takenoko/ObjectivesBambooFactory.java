package dev.stonks.takenoko;

import java.util.List;

public class ObjectivesBambooFactory {

    /**
     * The function to make all panda objectives ;
     * @return the list of the objectives (refer to the game doc for detail)
     */
    public static List<PandaObjective> pandaObjectiveList(){
        List<PandaObjective> newList = null;
        BambooPattern greenPattern = new BambooPattern(TileKind.Green,1,2);
        BambooPattern yellowPattern = new BambooPattern(TileKind.Yellow,1,2);
        BambooPattern pinkPattern = new BambooPattern(TileKind.Pink,1,2);
        BambooPattern multiColorPatern = new BambooPattern(TileKind.Green,TileKind.Yellow,TileKind.Pink,1,3);
        for(int i=0;i<5;i++){
            newList.add(new PandaObjective(ObjectiveKind.Panda,3,greenPattern));
            if(i>0){
                newList.add(new PandaObjective(ObjectiveKind.Panda,4,yellowPattern));
            }
            if(i>1) {
                newList.add(new PandaObjective(ObjectiveKind.Panda, 5, pinkPattern));
                newList.add(new PandaObjective(ObjectiveKind.Panda,6,multiColorPatern));
            }
        }
        return newList;
    }

    public static List<GardenerObjective> gardenerObjectiveList(){
        List <GardenerObjective> newList = null;
        BambooPattern greenPattern = new BambooPattern(TileKind.Green,4,1);
        BambooPattern maxGreenPattern = new BambooPattern(TileKind.Green,4,4);
        BambooPattern yellowPattern = new BambooPattern(TileKind.Yellow,4,1);
        BambooPattern maxYellowPattern = new BambooPattern(TileKind.Yellow,4,3);
        BambooPattern pinkPattern = new BambooPattern(TileKind.Pink,4,1);
        BambooPattern maxPinkPattern = new BambooPattern(TileKind.Pink,4,2);

        newList.add(new GardenerObjective(ObjectiveKind.Gardener,3,greenPattern));
        newList.add(new GardenerObjective(ObjectiveKind.Gardener,4,greenPattern));
        newList.add(new GardenerObjective(ObjectiveKind.Gardener,4,greenPattern));
        newList.add(new GardenerObjective(ObjectiveKind.Gardener,5,greenPattern));
        newList.add(new GardenerObjective(ObjectiveKind.Gardener,8,maxGreenPattern));

        newList.add(new GardenerObjective(ObjectiveKind.Gardener,4,yellowPattern));
        newList.add(new GardenerObjective(ObjectiveKind.Gardener,5,yellowPattern));
        newList.add(new GardenerObjective(ObjectiveKind.Gardener,5,yellowPattern));
        newList.add(new GardenerObjective(ObjectiveKind.Gardener,6,yellowPattern));
        newList.add(new GardenerObjective(ObjectiveKind.Gardener,7,maxYellowPattern));

        newList.add(new GardenerObjective(ObjectiveKind.Gardener,5,pinkPattern));
        newList.add(new GardenerObjective(ObjectiveKind.Gardener,6,pinkPattern));
        newList.add(new GardenerObjective(ObjectiveKind.Gardener,6,pinkPattern));
        newList.add(new GardenerObjective(ObjectiveKind.Gardener,7,pinkPattern));
        newList.add(new GardenerObjective(ObjectiveKind.Gardener,6,maxPinkPattern));

        return newList;
    }
}
