package dev.stonks.takenoko;

import java.util.Objects;

public class Bamboo {
    private int size;
    private final TileKind color;

    public Bamboo(TileKind color){
        this.size = 0;
        this.color = color;
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
     * Decrease the size of the bamboo
     * Maximal size : 0
     */
    public void cut(){
        if (this.size > 0)
            this.size--;
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
        if (!(o instanceof Bamboo)) return false;
        Bamboo bamboo = (Bamboo) o;
        return getSize() == bamboo.getSize() &&
                getColor() == bamboo.getColor();
    }

    public boolean equalsWithoutSize(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bamboo)) return false;
        Bamboo bamboo = (Bamboo) o;
        return getColor() == bamboo.getColor();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSize(), getColor());
    }
}
