package dev.stonks.takenoko;

import java.util.ArrayList;

// TODO: move most of this code in `Pattern`.

/**
 * Allows to create the default patterns for the game. The goal is to generate
 * patterns that are listed at the following URL:
 * http://jeuxstrategieter.free.fr/Takenoko_complet.php
 *
 * @author the StonksDev team
 */
public class PatternFactory {
    PatternFactory() {}

    /**
     * Creates a list of all the valid patterns in the whole game.
     */
    static ArrayList<Pattern> createLegalPatterns() {
        ArrayList<Pattern> ps = new ArrayList();

        ps.add(createTriangle(TileKind.Green));
        ps.add(createDiamond(TileKind.Green));
        ps.add(createDiamond(TileKind.Yellow, TileKind.Pink));
        ps.add(createDiamond(TileKind.Pink, TileKind.Green));
        ps.add(createDiamond(TileKind.Yellow, TileKind.Green));

        ps.add(createI(TileKind.Yellow));
        ps.add(createDiamond(TileKind.Yellow));
        ps.add(createC(TileKind.Yellow));
        ps.add(createI(TileKind.Green));
        ps.add(createC(TileKind.Green));

        ps.add(createC(TileKind.Pink));
        ps.add(createTriangle(TileKind.Pink));
        ps.add(createDiamond(TileKind.Pink));
        ps.add(createI(TileKind.Pink));
        ps.add(createTriangle(TileKind.Yellow));

        return ps;
    }

    /**
     * Creates a triangular-shaped pattern with the specified color.
     * @param c the color used for the pattern
     * @return a triangular pattern with the correct color.
     */
    static Pattern createTriangle(TileKind c) {
        return new Pattern()
                .withCenter(c)
                .withNeighbor(Direction.North, c)
                .withNeighbor(Direction.NorthEast, c);
    }

    /**
     * Creates a diamond-shaped pattern with the following color. Adjacent
     * tiles have the same color.
     * @param c the first color to be used.
     * @return a pattern with the correct requirements.
     */
    static Pattern createDiamond(TileKind c) {
        return createDiamond(c, c);
    }

    /**
     * Creates a diamond-shaped pattern with the following two colors. Adjacent
     * tiles have the same color.
     * @param c1 the first color to be used.
     * @param c2 the second color to be used.
     * @return a pattern with the correct requirements.
     */
    static Pattern createDiamond(TileKind c1, TileKind c2) {
        return new Pattern()
                .withCenter(c1)
                .withNeighbor(Direction.North, c1)
                .withNeighbor(Direction.NorthEast, c2)
                .withNeighbor(Direction.SouthEast, c2);
    }

    /**
     * Creates an I-shaped pattern with a single color.
     * @param c the expected tile kind.
     * @return the correct pattern.
     */
    static Pattern createI(TileKind c) {
        return new Pattern()
                .withCenter(c)
                .withNeighbor(Direction.North, c)
                .withNeighbor(Direction.South, c);
    }

    /**
     * Creates an C-shaped pattern with a single color.
     * @param c the expected tile kind.
     * @return the correct pattern.
     */
    static Pattern createC(TileKind c) {
        return new Pattern()
                .withCenter(c)
                .withNeighbor(Direction.North, c)
                .withNeighbor(Direction.SouthEast, c);
    }
}
