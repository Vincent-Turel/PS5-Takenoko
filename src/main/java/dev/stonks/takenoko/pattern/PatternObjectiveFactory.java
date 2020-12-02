package dev.stonks.takenoko.pattern;

import dev.stonks.takenoko.map.TileKind;

import java.util.ArrayList;

public class PatternObjectiveFactory {
    /**
     * Returns every valid pattern objective available in the game.
     */
    public static ArrayList<PatternObjective> validPatternObjectives() {
        ArrayList<PatternObjective> ps = new ArrayList();

        ps.add(new PatternObjective( 2, Pattern.triangleShaped(TileKind.Green)));
        ps.add(new PatternObjective( 3, Pattern.diamondShaped(TileKind.Green)));
        ps.add(new PatternObjective( 5, Pattern.diamondShaped(TileKind.Yellow, TileKind.Pink)));
        ps.add(new PatternObjective( 4, Pattern.diamondShaped(TileKind.Pink, TileKind.Green)));
        ps.add(new PatternObjective( 3, Pattern.diamondShaped(TileKind.Yellow, TileKind.Green)));

        ps.add(new PatternObjective( 3, Pattern.iShaped(TileKind.Yellow)));
        ps.add(new PatternObjective( 4, Pattern.diamondShaped(TileKind.Yellow)));
        ps.add(new PatternObjective( 3, Pattern.cShaped(TileKind.Yellow)));
        ps.add(new PatternObjective( 2, Pattern.iShaped(TileKind.Green)));
        ps.add(new PatternObjective( 2, Pattern.cShaped(TileKind.Green)));

        ps.add(new PatternObjective(4, Pattern.cShaped(TileKind.Pink)));
        ps.add(new PatternObjective(4, Pattern.triangleShaped(TileKind.Pink)));
        ps.add(new PatternObjective(5, Pattern.diamondShaped(TileKind.Pink)));
        ps.add(new PatternObjective(4, Pattern.iShaped(TileKind.Pink)));
        ps.add(new PatternObjective(3, Pattern.triangleShaped(TileKind.Yellow)));

        return ps;
    }
}
