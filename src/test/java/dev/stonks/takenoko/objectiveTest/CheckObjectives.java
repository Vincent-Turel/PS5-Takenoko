package dev.stonks.takenoko.objectiveTest;

import dev.stonks.takenoko.bot.Player;
import dev.stonks.takenoko.map.*;
import dev.stonks.takenoko.objective.GardenerObjective;
import dev.stonks.takenoko.objective.PandaObjective;
import dev.stonks.takenoko.pattern.BambooPattern;
import dev.stonks.takenoko.pattern.Pattern;
import dev.stonks.takenoko.objective.PatternObjective;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CheckObjectives {

    @Test
    public void objPatternTest() throws IllegalPlacementException {
        Player player = mock(Player.class);
        //Some objective :
        Pattern pattern = new Pattern().withCenter(TileKind.Green);
        Pattern patternLose = new Pattern().withCenter(TileKind.Pink);
        PatternObjective objectiveWin = new PatternObjective(5,pattern);
        PatternObjective objectiveLose = new PatternObjective(5,patternLose);
        //Making a map :
        Map map = new Map(42);
        Tile t = map.addNeighborOf(TileKind.Green, map.initialTile().withDirection(Direction.South));
        t.irrigate();
        Coordinate center = map.initialTile().getCoordinate();

        //Test :
        assertEquals(false,objectiveWin.getStates());
        objectiveWin.checkObjective(map,player);
        assertEquals(true,objectiveWin.getStates());

        assertEquals(false,objectiveLose.getStates());
        objectiveLose.checkObjective(map,player);
        assertEquals(false,objectiveLose.getStates());

    }

    @Test
    public void winObjectives(){
        //winner :
        Map map = mock(Map.class);
        Player winPlayer = mock(Player.class);
        when(winPlayer.getCollectedBamboo()).thenReturn(new int[]{4, 3, 5});
        Player winPlayer2 = mock(Player.class);
        when(winPlayer2.getCollectedBamboo()).thenReturn(new int[]{8, 6, 5});

        //Objective simple green
        BambooPattern greenPattern = new BambooPattern(TileKind.Green,4,1);
        PandaObjective objective = new PandaObjective(5,greenPattern);

        //Objective multicolor :
        BambooPattern multicolor = new BambooPattern(TileKind.Green, TileKind.Yellow, TileKind.Pink,2,1);
        PandaObjective objMultiColor = new PandaObjective(5,multicolor);

        //Test isValid Objective simple green
        objective.checkObjective(map,winPlayer);
        assertEquals(true,objective.getStates());
        int[] valExpected = new int[]{0,3,5};
        for(int i=0;i<3;i++){
            assertEquals(valExpected[i],winPlayer.getCollectedBamboo()[i]);
        }
        //Test isValid Objective multicolor
        objMultiColor.checkObjective(map,winPlayer2);
        assertEquals(true,objMultiColor.getStates());
        valExpected = new int[]{6,4,3};
        for(int i=0;i<3;i++){
            assertEquals(valExpected[i],winPlayer2.getCollectedBamboo()[i]);
        }
    }

    @Test
    public void loseObjectives(){
        //loser :
        Map map = mock(Map.class);
        Player losePlayer = mock(Player.class);
        when(losePlayer.getCollectedBamboo()).thenReturn(new int[]{1,1,1});

        //Objective simple green
        BambooPattern greenPattern = new BambooPattern(TileKind.Green,4,1);
        PandaObjective objective = new PandaObjective(5,greenPattern);

        //Objective multicolor :
        BambooPattern multicolor = new BambooPattern(TileKind.Green, TileKind.Yellow, TileKind.Pink,2,3);
        PandaObjective objMultiColor = new PandaObjective(5,multicolor);

        //Test isValid Objective simple green
        objective.checkObjective(map,losePlayer);
        assertEquals(false,objective.getStates());
        int[] valExpected = new int[]{1,1,1};
        for(int i=0;i<3;i++){
            assertEquals(valExpected[i],losePlayer.getCollectedBamboo()[i]);
        }
        //Test isValid Objective multicolor
        objMultiColor.checkObjective(map,losePlayer);
        assertEquals(false,objMultiColor.getStates());
        valExpected = new int[]{1,1,1};
        for(int i=0;i<3;i++){
            assertEquals(valExpected[i],losePlayer.getCollectedBamboo()[i]);
        }
    }

    @Test
    public void winYellowObjective(){
        Map map = mock(Map.class);
        Player winPlayer = mock(Player.class);
        when(winPlayer.getCollectedBamboo()).thenReturn(new int[]{3, 9, 5});

        BambooPattern yellowPattern = new BambooPattern(TileKind.Yellow,4,2);
        PandaObjective objective = new PandaObjective(5,yellowPattern);

        //Test isValid Objective simple yellow
        objective.checkObjective(map,winPlayer);
        assertEquals(true,objective.getStates());
        int[] valExpected = new int[]{3,1,5};
        for(int i=0;i<3;i++){
            assertEquals(valExpected[i],winPlayer.getCollectedBamboo()[i]);
        }
    }

    @Test
    public void winPinkObjective(){
        Map map = mock(Map.class);
        Player winPlayer = mock(Player.class);
        when(winPlayer.getCollectedBamboo()).thenReturn(new int[]{3, 6, 6});

        BambooPattern pinkPattern = new BambooPattern(TileKind.Pink,3,2);
        PandaObjective objective = new PandaObjective(5,pinkPattern);

        //Test isValid Objective simple yellow
        objective.checkObjective(map,winPlayer);
        assertEquals(true,objective.getStates());
        int[] valExpected = new int[]{3,6,0};
        for(int i=0;i<3;i++){
            assertEquals(valExpected[i],winPlayer.getCollectedBamboo()[i]);
        }
    }

    @Test
    public void testForGardenerObjective(){
        Player player = mock(Player.class);
        //Making some bamboos :
        Bamboo greenBamboo = mock(Bamboo.class);
        when(greenBamboo.getSize()).thenReturn(3);
        when(greenBamboo.getColor()).thenReturn(TileKind.Green);

        Bamboo pinkBamboo = mock(Bamboo.class);
        when(pinkBamboo.getSize()).thenReturn(4);
        when(pinkBamboo.getColor()).thenReturn(TileKind.Pink);

        //Making some tiles :
        Tile tileGreen = mock(Tile.class);
        when(tileGreen.getBamboo()).thenReturn(greenBamboo);
        when(tileGreen.getImprovement()).thenReturn(Improvement.Watershed);

        Tile tilePink1 = mock(Tile.class);
        when(tilePink1.getBamboo()).thenReturn(pinkBamboo);
        when(tilePink1.getImprovement()).thenReturn(Improvement.Empty);

        Tile tilePink2 = mock(Tile.class);
        when(tilePink2.getBamboo()).thenReturn(pinkBamboo);
        when(tilePink2.getImprovement()).thenReturn(Improvement.Empty);

        //Making the tab tiles for map :
        Optional<Tile>[] tiles = new Optional[4];
        Optional<Tile> convert1 = Optional.of(tileGreen);
        Optional<Tile> convert2 = Optional.of(tilePink1);
        Optional<Tile> convert3 = Optional.of(tilePink2);
        tiles[0]=convert3;
        tiles[1]=convert1;
        tiles[2]=convert2;
        tiles[3]=Optional.empty();

        //Making the map :
        Map map = mock(Map.class);
        when(map.getTiles()).thenReturn(tiles);

        //Create the objectives :
        BambooPattern winPattern = new BambooPattern(TileKind.Green,3);
        GardenerObjective objectiveWin = new GardenerObjective(5,winPattern);
        BambooPattern losePattern = new BambooPattern(TileKind.Green,5);
        GardenerObjective objectiveLose = new GardenerObjective(5,losePattern);
        BambooPattern pinkPattern = new BambooPattern(TileKind.Pink,4,2);
        GardenerObjective pinkObjective = new GardenerObjective(5,pinkPattern);

        GardenerObjective objectiveWinWithImprovement = new GardenerObjective(5,winPattern,Improvement.Watershed);
        GardenerObjective objectiveLoseWithImprovement = new GardenerObjective(5,winPattern,Improvement.NoImprovementHere);
        GardenerObjective objectiveWinWithNoImprovement = new GardenerObjective(5,pinkPattern,Improvement.NoImprovementHere);
        GardenerObjective objectiveLoseImprovementNoHere = new GardenerObjective(5,pinkPattern,Improvement.Watershed);

        //Test function :
        objectiveWin.checkObjective(map,player);
        objectiveLose.checkObjective(map,player);
        pinkObjective.checkObjective(map,player);
        objectiveWinWithImprovement.checkObjective(map,player);
        objectiveLoseWithImprovement.checkObjective(map,player);
        objectiveWinWithNoImprovement.checkObjective(map,player);
        objectiveLoseImprovementNoHere.checkObjective(map,player);

        assertEquals(true,objectiveWin.getStates());
        assertEquals(false,objectiveLose.getStates());
        assertEquals(true,pinkObjective.getStates());
        assertEquals(true,objectiveWinWithImprovement.getStates());
        assertEquals(false,objectiveLoseWithImprovement.getStates());
        assertEquals(true,objectiveWinWithNoImprovement.getStates());
        assertEquals(false,objectiveLoseImprovementNoHere.getStates());
    }

}