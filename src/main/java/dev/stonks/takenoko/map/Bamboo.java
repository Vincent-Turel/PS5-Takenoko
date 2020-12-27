package dev.stonks.takenoko.map;

import dev.stonks.takenoko.IllegalEqualityExceptionGenerator;

import java.util.Objects;
import java.util.Optional;

public class Bamboo {
    private int size;
    private final TileKind color;

    public Bamboo(TileKind color){
        this.size = 0;
        this.color = color;
    }

    public Bamboo(Bamboo bamboo) {
        this.color = bamboo.color;
        this.size = bamboo.size;
    }

    /**
     * Increase the size of the bamboo
     * Maximal size : 4
     */
    public void grow(){
        if (this.size < 4)
            this.size++;
    }

    /**
     * Decrease the size of the bamboo and returns the cut section, if it
     * exists.
     * Minimal size : 0
     */
    public Optional<TileKind> cut() {
        if (this.size > 0) {
            this.size--;
            return Optional.of(color);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Returns the bamboo length.
     */
    public int getSize() {
        return size;
    }

    /**
     * Get the color of the bamboo
     * @return color
     */
    public TileKind getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Bamboo)) {
            throw IllegalEqualityExceptionGenerator.create(Bamboo.class, o.getClass());
        }

        Bamboo bamboo = (Bamboo) o;
        return getSize() == bamboo.getSize() &&
                getColor() == bamboo.getColor();
    }

    public boolean equalsWithoutSize(Object o) {
        if (this == o) return true;

        if (!(o instanceof Bamboo)) {
            throw IllegalEqualityExceptionGenerator.create(Bamboo.class, o.getClass());
        }

        Bamboo bamboo = (Bamboo) o;
        return getColor() == bamboo.getColor();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSize(), getColor());
    }
}
