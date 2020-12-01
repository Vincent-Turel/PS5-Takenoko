package dev.stonks.takenoko;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ObjectivesTest {

    @Test
    void objectiveClassTest(){
        Objective pattern = new Objective(ObjectiveKind.Pattern, 5);
        Objective gardener = new Objective(ObjectiveKind.Gardener, 6);
        Objective panda = new Objective(ObjectiveKind.Panda, 7);
        //nbPt test :
        assertEquals(5,pattern.getNbPt());
        assertEquals(6,gardener.getNbPt());
        assertEquals(7,panda.getNbPt());
        //type test :
        assertEquals(ObjectiveKind.Pattern,pattern.getObjType());
        assertEquals(ObjectiveKind.Gardener,gardener.getObjType());
        assertEquals(ObjectiveKind.Panda,panda.getObjType());
        //objectif value test :
        assertEquals(false,pattern.getStates());
        assertEquals(false,gardener.getStates());
        assertEquals(false,panda.getStates());
        //updateStates test :
        pattern.UpdtateStates();
        gardener.UpdtateStates();
        panda.UpdtateStates();
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
        PatternObjective pattern = ObjectivesMaker.newPatternObjectives(5,aPattern);
        GardenerObjective gardener = ObjectivesMaker.newGardenerObjectives(6, aBambooPattern);
        PandaObjective panda = ObjectivesMaker.newPandaObjectives(7, aBambooPattern);

        //test nbPt :
        assertEquals(5,pattern.getNbPt());
        assertEquals(6,gardener.getNbPt());
        assertEquals(7,panda.getNbPt());

        //test objType :
        assertEquals(ObjectiveKind.Pattern,pattern.getObjType());
        assertEquals(ObjectiveKind.Gardener,gardener.getObjType());
        assertEquals(ObjectiveKind.Panda,panda.getObjType());

        //test objPattern :
        assertEquals(aBambooPattern,panda.getBambooPattern());
        assertEquals(aBambooPattern,gardener.getBambooPattern());
        assertEquals(aPattern,pattern.getLocalPattern());
    }

}