package dev.stonks.takenoko;

import dev.stonks.takenoko.map.Improvement;
import dev.stonks.takenoko.objective.GardenerObjective;
import dev.stonks.takenoko.objective.Objective;
import dev.stonks.takenoko.objective.ObjectiveKind;
import dev.stonks.takenoko.objective.PandaObjective;
import dev.stonks.takenoko.pattern.BambooPattern;
import dev.stonks.takenoko.pattern.Pattern;
import dev.stonks.takenoko.objective.PatternObjective;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ObjectivesTest {
  
    @Test
    void objectiveClassTest(){
        Objective objective = new Objective(5);
        //nbPt test :
        assertEquals(5,objective.getNbPt());
        //objective value test :
        assertEquals(false,objective.getStates());
        //updateStates test :
        objective.updateStates();
        assertEquals(true,objective.getStates());
        //reset test :
        objective.resetObj();
        assertEquals(false,objective.getStates());
    }

    @Test
    void objectivePatternCreationTest(){
        Pattern aPattern = mock(Pattern.class);
        PatternObjective pattern = new PatternObjective(5,aPattern);
        assertEquals(ObjectiveKind.PatternObjective,pattern.getObjType());
        assertEquals(5,pattern.getNbPt());
        assertEquals(ObjectiveKind.PatternObjective,pattern.getObjType());
        assertEquals(aPattern,pattern.getLocalPattern());
    }

    @Test
    void objectivePandaCreationTest(){
        BambooPattern aBambooPattern = mock(BambooPattern.class);
        PandaObjective panda = new PandaObjective(7, aBambooPattern);
        assertEquals(ObjectiveKind.PandaObjective,panda.getObjType());
        assertEquals(7,panda.getNbPt());
        assertEquals(ObjectiveKind.PandaObjective,panda.getObjType());
        assertEquals(aBambooPattern,panda.getBambooPattern());
    }

    @Test
    void objectiveGardenerCreationTest(){
        BambooPattern aBambooPattern = mock(BambooPattern.class);
        GardenerObjective gardener = new GardenerObjective(6, aBambooPattern);
        assertEquals(ObjectiveKind.GardenerObjective,gardener.getObjType());
        assertEquals(6,gardener.getNbPt());
        assertEquals(ObjectiveKind.GardenerObjective,gardener.getObjType());
        assertEquals(aBambooPattern,gardener.getBambooPattern());
    }

    @Test
    void objectiveGardenerTestComplementary(){
        BambooPattern pattern = mock(BambooPattern.class);
        GardenerObjective objectiveWithImprovement = new GardenerObjective(5,pattern, Improvement.Watershed);
        GardenerObjective objectiveWithoutImprovement = new GardenerObjective(5,pattern);
        GardenerObjective objectiveWithNoImprovementForValidation = new GardenerObjective(5,pattern, Improvement.NoImprovementHere);

        assertEquals(Improvement.Watershed,objectiveWithImprovement.getLocalImprovement());
        assertEquals(Improvement.Empty,objectiveWithoutImprovement.getLocalImprovement());
        assertEquals(Improvement.NoImprovementHere,objectiveWithNoImprovementForValidation.getLocalImprovement());
    }

}