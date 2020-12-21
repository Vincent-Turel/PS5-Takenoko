package dev.stonks.takenoko.map;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Represents a tile which is in a deck (ie: has not been placed in the map).
 * This class must NOT be used to represent the initial tile, as it is
 * automatically added when the map is created.
 * @author the StonksDev team
 */
public class AbstractTile {
    TileKind kind;
    Improvement improvement;

    public AbstractTile(TileKind tk, Improvement i) {
        kind = tk;
        improvement = i;
    }

    public AbstractTile(TileKind tk) {
        this(tk, Improvement.Empty);
    }

    /**
     * Returns every abstract tile available in the game.
     */
    public static List<AbstractTile> allLegalAbstractTiles() {
        // See http://jeuxstrategieter.free.fr/Takenoko_complet.php for a
        // complete list of every available abstract tile.

        // TODO: once more improvements are added, edit the ranges here.
        Stream<AbstractTile> emptyGreen = IntStream.range(0, 10).mapToObj(i -> new AbstractTile(TileKind.Green));
        Stream<AbstractTile> emptyPink = IntStream.range(0, 6).mapToObj(i -> new AbstractTile(TileKind.Pink));
        Stream<AbstractTile> emptyYellow = IntStream.range(0, 8).mapToObj(i -> new AbstractTile(TileKind.Yellow));

        Stream<AbstractTile> waterSheds = Stream.of(TileKind.Green, TileKind.Pink, TileKind.Yellow)
                .map(kind -> new AbstractTile(kind, Improvement.Watershed));

        List<AbstractTile> ats = Stream.of(emptyGreen, emptyPink, emptyYellow, waterSheds)
                .flatMap(s -> s) // This is good code™️
                .collect(Collectors.toList());


        // We should always return 27 abstract tiles. Let's ensure it.
        if (ats.size() != 27) {
            throw new IllegalStateException("27 AbstractTiles should be created");
        }

        return ats;
    }

    /**
     * Adds an improvement to the abstract tile.
     * @param i the improvement to be added
     * @return an AbstractTile with the correct improvement.
     * @throws IllegalCallerException if there is already an improvement and i
     * is not an empty improvement.
     */
    public AbstractTile withImprovement(Improvement i) {
        if (!improvement.isEmpty() && !i.isEmpty()) {
            throw new IllegalCallerException("Attempt to add an improvement to a tile that already contains one");
        }

        improvement = i;

        return this;
    }

    public Tile withCoordinate(Coordinate c) {
        return new Tile(c, kind, improvement);
    }

    public TileKind getKind() {
        return kind;
    }

    public Improvement getImprovement() {
        return improvement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractTile))
            throw IllegalEqualityExceptionGenerator.create(Coordinate.class, o.getClass());
        AbstractTile that = (AbstractTile) o;
        return getKind() == that.getKind() &&
                getImprovement() == that.getImprovement();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKind(), getImprovement());
    }
}
