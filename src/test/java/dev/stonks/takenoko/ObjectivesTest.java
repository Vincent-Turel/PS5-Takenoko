package dev.stonks.takenoko;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ObjectivesTest {

    ObjectivesMaker testMake;
    Objective OB01_2_5; //01->ID;2->nb tuille;5->nb point;
    Objective OB02_3_6;
    Objective OB03_4_7;
    Objective doubleObj;
    List<Objective> deck;
    int n = 3; //nb of objectives;

    @BeforeEach
    void setUp() {
        testMake = new ObjectivesMaker();
        testMake.addAnObjectives(1,2,5);
        testMake.addAnObjectives(2,3,6);
        testMake.addAnObjectives(3,4,6);
        Objective doubleObj = testMake.addAnObjectives(2,99,99);
        deck = testMake.getDeck();
    }

    @Test
    void testFactoMaker() {
        assertEquals(n, deck.size());
        assertEquals(testMake.addAnObjectives(2,3,6),null);
        deck = testMake.getDeck();
        assertEquals(n, deck.size());
        Objective newObj;
        assertEquals(newObj = testMake.addAnObjectives(4,7,8),newObj);
        assertEquals(n+1, deck.size());
    }

    @Test
    void testObjectives(){
        assertEquals(doubleObj,null);
        assertEquals(deck.get(0).getNbPt(),5);
        assertEquals(deck.get(0).getNbTuille(), 2);
        assertEquals(deck.get(0).getObjID(),1);
        assertEquals(deck.get(2).getNbPt(),6);
        assertEquals(deck.get(2).getNbTuille(), 4);
        assertEquals(deck.get(2).getObjID(),3);
    }

}