package dev.stonks.takenoko.bot;

import java.util.Objects;

/**
 * This class is a generic class that allows the player
 * to give a multiple answer to the game, whatever are the types of the answer.
 * @param <T> the type of the first element of the answer
 * @param <U> the type of the second element of the answer
 */
public class MultipleAnswer<T, U> {
    private final T t;
    private final U u;

    public MultipleAnswer(T t, U u){
        this.t = t;
        this.u = u;
    }

    /**
     * Get the fist element of the answer
     * @return an object of generic type T
     */
    public T getT() {
        return t;
    }

    /**
     * Get the second element of the answer
     * @return an object of generic type U
     */
    public U getU() {
        return u;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MultipleAnswer)) return false;
        MultipleAnswer<?, ?> answer = (MultipleAnswer<?, ?>) o;
        return Objects.equals(getT(), answer.getT()) &&
                Objects.equals(getU(), answer.getU());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getT(), getU());
    }
}
