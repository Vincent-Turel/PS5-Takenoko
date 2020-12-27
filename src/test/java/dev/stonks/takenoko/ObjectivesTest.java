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
        Objective pattern = new Objective(ObjectiveKind.ObjectivePattern, 5);
        Objective gardener = new Objective(ObjectiveKind.ObjectiveGardener, 6);
        Objective panda = new Objective(ObjectiveKind.ObjectivePanda, 7);
        //nbPt test :
        assertEquals(5,pattern.getNbPt());
        assertEquals(6,gardener.getNbPt());
        assertEquals(7,panda.getNbPt());
        //type test :
        assertEquals(ObjectiveKind.ObjectivePattern,pattern.getObjType());
        assertEquals(ObjectiveKind.ObjectiveGardener,gardener.getObjType());
        assertEquals(ObjectiveKind.ObjectivePanda,panda.getObjType());
        //objective value test :
        assertEquals(false,pattern.getStates());
        assertEquals(false,gardener.getStates());
        assertEquals(false,panda.getStates());
        //updateStates test :
        pattern.updateStates();
        gardener.updateStates();
        panda.updateStates();
        assertEquals(true,pattern.getStates());
        assertEquals(true,gardener.getStates());
        assertEquals(true,panda.getStates());
        //reset test :
        pattern.resetObj();
        gardener.resetObj();
        panda.resetObj();
        assertEquals(false,pattern.getStates());
        assertEquals(false,gardener.getStates());
        assertEquals(false,panda.getStates());
    }

    @Test
    void objectiveCreationTest(){
        Pattern aPattern = mock(Pattern.class);
        BambooPattern aBambooPattern = mock(BambooPattern.class);
        PatternObjective pattern = new PatternObjective(5,aPattern);
        GardenerObjective gardener = new GardenerObjective(6, aBambooPattern);
        PandaObjective panda = new PandaObjective(7, aBambooPattern);

        //test nbPt :
        assertEquals(5,pattern.getNbPt());
        assertEquals(6,gardener.getNbPt());
        assertEquals(7,panda.getNbPt());

        //test objType :
        assertEquals(ObjectiveKind.ObjectivePattern,pattern.getObjType());
        assertEquals(ObjectiveKind.ObjectiveGardener,gardener.getObjType());
        assertEquals(ObjectiveKind.ObjectivePanda,panda.getObjType());

        //test objPattern :
        assertEquals(aBambooPattern,panda.getBambooPattern());
        assertEquals(aBambooPattern,gardener.getBambooPattern());
        assertEquals(aPattern,pattern.getLocalPattern());

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