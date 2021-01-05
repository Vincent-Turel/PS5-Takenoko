package dev.stonks.takenoko.pattern;

import dev.stonks.takenoko.IllegalEqualityExceptionGenerator;
import dev.stonks.takenoko.map.TileKind;

import java.util.Objects;
import java.util.Optional;

public class BambooPattern {

    private final TileKind color;
    private final Optional<TileKind> optionalColor1;
    private final Optional<TileKind> optionalColor2;
    private final int height;
    private final int nbBamboo;

    /**
     * Make a bamboo pattern :
     *
     * @param color  color of the bamboo
     * @param height the height of the bamboo (int between 1-4)
     */
    public BambooPattern(TileKind color, int height) {
        this.color = color;
        this.height = height;
        this.nbBamboo = 1;
        this.optionalColor1 = Optional.empty();
        this.optionalColor2 = Optional.empty();
    }

    /**
     * Make a bamboo pattern :
     *
     * @param color    color of the bamboo
     * @param height   the height of the bamboo (int between 1-4)
     * @param nbBamboo the number of different bamboo
     */
    public BambooPattern(TileKind color, int height, int nbBamboo) {
        this.color = color;
        this.height = height;
        this.nbBamboo = nbBamboo;
        this.optionalColor1 = Optional.empty();
        this.optionalColor2 = Optional.empty();
    }

    public BambooPattern(TileKind color, TileKind optionalColor1, TileKind optionalColor2, int height, int nbBamboo) {
        this.color = color;
        this.optionalColor1 = Optional.of(optionalColor1);
        this.optionalColor2 = Optional.of(optionalColor2);
        this.height = height;
        this.nbBamboo = nbBamboo;
    }

    /**
     * GETTER FOR ALL @param :
     */

    public int getHeight() {
        return height;
    }

    public int getNbBamboo() {
        return nbBamboo;
    }

    public TileKind getColor() {
        return color;
    }

    public Optional<TileKind> getOptionalColor1() {
        return optionalColor1;
    }

    public Optional<TileKind> getOptionalColor2() {
        return optionalColor2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BambooPattern))
            throw IllegalEqualityExceptionGenerator.create(BambooPattern.class, o.getClass());
        BambooPattern that = (BambooPattern) o;
        return getHeight() == that.getHeight() &&
                getNbBamboo() == that.getNbBamboo() &&
                getColor() == that.getColor() &&
                Objects.equals(getOptionalColor1(), that.getOptionalColor1()) &&
                Objects.equals(getOptionalColor2(), that.getOptionalColor2());
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, optionalColor1, optionalColor2, height, nbBamboo);
    }
}
