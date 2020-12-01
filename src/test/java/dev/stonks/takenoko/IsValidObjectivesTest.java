package dev.stonks.takenoko;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class IsValidObjectivesTest {

    @Test
    public void winObjectives(){
        //winner :
        Player winPlayer = mock(Player.class);
        when(winPlayer.getCollectedBamboo()).thenReturn(new int[]{4, 3, 5});
        Player winPlayer2 = mock(Player.class);
        when(winPlayer2.getCollectedBamboo()).thenReturn(new int[]{8, 6, 5});

        //Objective simple green
        BambooPattern greenPattern = new BambooPattern(TileKind.Green,4,1);
        PandaObjective objective = new PandaObjective(ObjectiveKind.Panda,5,greenPattern);

        //Objective multicolor :
        BambooPattern multicolor = new BambooPattern(TileKind.Green,TileKind.Yellow,TileKind.Pink,2,1);
        PandaObjective objMultiColor = new PandaObjective(ObjectiveKind.Panda,5,multicolor);

        //Test isValid Objective simple green
        isValidObjectives.isObjectivesPandaValid(objective,winPlayer);
        assertEquals(true,objective.getStates());
        int[] valExpected = new int[]{0,3,5};
        for(int i=0;i<3;i++){
            assertEquals(valExpected[i],winPlayer.getCollectedBamboo()[i]);
        }
        //Test isValid Objective multicolor
        isValidObjectives.isObjectivesPandaValid(objMultiColor,winPlayer2);
        assertEquals(true,objMultiColor.getStates());
        valExpected = new int[]{6,4,3};
        for(int i=0;i<3;i++){
            assertEquals(valExpected[i],winPlayer2.getCollectedBamboo()[i]);
        }
    }

    @Test
    public void loseObjectivies(){
        //loser :
        Player losePlayer = mock(Player.class);
        when(losePlayer.getCollectedBamboo()).thenReturn(new int[]{1,1,1});

        //Objective simple green
        BambooPattern greenPattern = new BambooPattern(TileKind.Green,4,1);
        PandaObjective objective = new PandaObjective(ObjectiveKind.Panda,5,greenPattern);

        //Objective multicolor :
        BambooPattern multicolor = new BambooPattern(TileKind.Green,TileKind.Yellow,TileKind.Pink,2,3);
        PandaObjective objMultiColor = new PandaObjective(ObjectiveKind.Panda,5,multicolor);

        //Test isValid Objective simple green
        isValidObjectives.isObjectivesPandaValid(objective,losePlayer);
        assertEquals(false,objective.getStates());
        int[] valExpected = new int[]{1,1,1};
        for(int i=0;i<3;i++){
            assertEquals(valExpected[i],losePlayer.getCollectedBamboo()[i]);
        }
        //Test isValid Objective multicolor
        isValidObjectives.isObjectivesPandaValid(objMultiColor,losePlayer);
        assertEquals(false,objMultiColor.getStates());
        valExpected = new int[]{1,1,1};
        for(int i=0;i<3;i++){
            assertEquals(valExpected[i],losePlayer.getCollectedBamboo()[i]);
        }
    }

    @Test
    public void winYellowObjective(){
        Player winPlayer = mock(Player.class);
        when(winPlayer.getCollectedBamboo()).thenReturn(new int[]{3, 9, 5});

        BambooPattern yellowPattern = new BambooPattern(TileKind.Yellow,4,2);
        PandaObjective objective = new PandaObjective(ObjectiveKind.Panda,5,yellowPattern);

        //Test isValid Objective simple yellow
        isValidObjectives.isObjectivesPandaValid(objective,winPlayer);
        assertEquals(true,objective.getStates());
        int[] valExpected = new int[]{3,1,5};
        for(int i=0;i<3;i++){
            assertEquals(valExpected[i],winPlayer.getCollectedBamboo()[i]);
        }
    }

    @Test
    public void winPinkObjective(){
        Player winPlayer = mock(Player.class);
        when(winPlayer.getCollectedBamboo()).thenReturn(new int[]{3, 6, 6});

        BambooPattern pinkPattern = new BambooPattern(TileKind.Pink,3,2);
        PandaObjective objective = new PandaObjective(ObjectiveKind.Panda,5,pinkPattern);

        //Test isValid Objective simple yellow
        isValidObjectives.isObjectivesPandaValid(objective,winPlayer);
        assertEquals(true,objective.getStates());
        int[] valExpected = new int[]{3,6,0};
        for(int i=0;i<3;i++){
            assertEquals(valExpected[i],winPlayer.getCollectedBamboo()[i]);
        }
    }

    @Test
    public void futurTestForGardenerObjective(){
        assertEquals(true, isValidObjectives.isObjectivesGardenerValid());
    }

}
