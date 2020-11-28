package dev.stonks.takenoko;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ObjectivesTest {

    ObjectivesMaker testMake;
    PatternObjective doubleObj;
    List<Integer> deck;

    @BeforeEach
    void setUp() {
        Pattern pattern = new Pattern()
                .withCenter(TileKind.Green)
                .withNeighbor(Direction.North,TileKind.Yellow)
                .withNeighbor(Direction.South,TileKind.Pink);
        testMake = new ObjectivesMaker();
        PatternObjective obj3 = testMake.addAnPatternObjectives(2,6,1,pattern);
        deck = testMake.getlistObjectves();
    }

    @Test
    void testFactoMaker() {
        Pattern pattern = new Pattern()
                .withCenter(TileKind.Green)
                .withNeighbor(Direction.North,TileKind.Yellow)
                .withNeighbor(Direction.South,TileKind.Pink);
        assertEquals(1, deck.size());
        assertEquals(testMake.addAnPatternObjectives(2,3,6,pattern),null);
        deck = testMake.getlistObjectves();
        assertEquals(1, deck.size());
        PatternObjective newObj = new PatternObjective(4,4,7, pattern);
        PatternObjective newFacto = testMake.addAnPatternObjectives(4,7,1,pattern);
        assertEquals(newFacto.getObjID(),newObj.getObjID());
        assertEquals(newFacto.getNbPt(),newObj.getNbPt());
        assertEquals(2, deck.size());
    }

    @Test
    void testObjectives(){

        Pattern pattern = new Pattern()
                .withCenter(TileKind.Green)
                .withNeighbor(Direction.North,TileKind.Yellow)
                .withNeighbor(Direction.South,TileKind.Pink);
        assertEquals(1, deck.size());

        PatternObjective obj1 = testMake.addAnPatternObjectives(1,5,1,pattern);
        PatternObjective obj2 = testMake.addAnPatternObjectives(5,6,1,pattern);

        assertEquals(3, deck.size());

        assertEquals(obj1.getNbPt(),5);
        assertEquals(obj1.getObjID(),1);
        assertEquals(obj2.getNbPt(),6);
        assertEquals(obj2.getObjID(),5);
    }

}