package dev.stonks.takenoko.map;

import dev.stonks.takenoko.IllegalEqualityExceptionGenerator;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Represents a tile which is in a deck (ie: has not been placed in the map).
 * This class must NOT be used to represent the initial tile, as it is
 * automatically added when the map is created.
 *
 * @author the StonksDev team
 */
public class AbstractTile {
    private final TileKind kind;
    private Improvement improvement;

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
        Stream<AbstractTile> emptyGreen = IntStream.range(0, 6).mapToObj(i -> new AbstractTile(TileKind.Green));
        Stream<AbstractTile> emptyPink = IntStream.range(0, 4).mapToObj(i -> new AbstractTile(TileKind.Pink));
        Stream<AbstractTile> emptyYellow = IntStream.range(0, 6).mapToObj(i -> new AbstractTile(TileKind.Yellow));

        // Green tiles with improvements
        Stream<AbstractTile> watershedGreen = IntStream.range(0, 2)
                .mapToObj(i -> new AbstractTile(TileKind.Green))
                .map(AbstractTile::addWatershed);

        Stream<AbstractTile> enclosureGreen = IntStream.range(0, 2)
                .mapToObj(i -> new AbstractTile(TileKind.Green))
                .map(AbstractTile::addEnclosure);

        Stream<AbstractTile> fertilizerGreen = Stream.of(new AbstractTile(TileKind.Green))
                .map(AbstractTile::addFertilizer);

        // Pink tiles with improvements
        Stream<AbstractTile> watershedPink = Stream.of(new AbstractTile(TileKind.Pink))
                .map(AbstractTile::addWatershed);

        Stream<AbstractTile> enclosurePink = Stream.of(new AbstractTile(TileKind.Pink))
                .map(AbstractTile::addEnclosure);

        Stream<AbstractTile> fertilizerPink = Stream.of(new AbstractTile(TileKind.Pink))
                .map(AbstractTile::addFertilizer);

        // Yellow tiles with improvements
        Stream<AbstractTile> watershedYellow = Stream.of(new AbstractTile(TileKind.Yellow))
                .map(AbstractTile::addWatershed);

        Stream<AbstractTile> enclosureYellow = Stream.of(new AbstractTile(TileKind.Yellow))
                .map(AbstractTile::addEnclosure);

        Stream<AbstractTile> fertilizerYellow = Stream.of(new AbstractTile(TileKind.Yellow))
                .map(AbstractTile::addFertilizer);


        List<AbstractTile> ats = Stream.of(
                emptyGreen,
                emptyPink,
                emptyYellow,
                watershedGreen,
                enclosureGreen,
                fertilizerGreen,
                watershedPink,
                enclosurePink,
                fertilizerPink,
                watershedYellow,
                enclosureYellow,
                fertilizerYellow
        )
                .flatMap(s -> s) // This is good code™️
                .collect(Collectors.toList());


        // We should always return 27 abstract tiles. Let's ensure it.
        if (ats.size() != 27) {
            throw new IllegalStateException("27 AbstractTiles should be created");
        }

        return ats;
    }

    private static AbstractTile addEnclosure(AbstractTile at) {
        return at.withImprovement(Improvement.Enclosure);
    }

    private static AbstractTile addWatershed(AbstractTile at) {
        return at.withImprovement(Improvement.Watershed);
    }

    private static AbstractTile addFertilizer(AbstractTile at) {
        return at.withImprovement(Improvement.Fertilizer);
    }

    /**
     * Adds an improvement to the abstract tile.
     *
     * @param i the improvement to be added
     * @return an AbstractTile with the correct improvement.
     * @throws IllegalCallerException if there is already an improvement and i
     *                                is not an empty improvement.
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
            throw IllegalEqualityExceptionGenerator.create(AbstractTile.class, o);
        AbstractTile that = (AbstractTile) o;
        return getKind() == that.getKind() &&
                getImprovement() == that.getImprovement();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKind(), getImprovement());
    }
}
