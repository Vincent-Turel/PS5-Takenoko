package dev.stonks.takenoko.bot;

import dev.stonks.takenoko.IllegalEqualityExceptionGenerator;

import java.util.Objects;
import java.util.Optional;

/**
 * This class is a generic class that allows the player
 * to give a multiple answer to the game, whatever are the types of the answer.
 * @param <T> the type of the first element of the answer
 * @param <U> the type of the second element of the answer
 * @param <V> the type of the third element of the answer (optional)
 */
public class MultipleAnswer<T, U, V> {
    private final T t;
    private final U u;
    private final Optional<V> v;

    public MultipleAnswer(T t, U u){
        this.t = t;
        this.u = u;
        this.v = Optional.empty();
    }

    public MultipleAnswer(T t, U u, V v){
        this.t = t;
        this.u = u;
        this.v = Optional.of(v);
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

    /**
     * Get the second element of the answer
     * @return an object of generic type V
     */
    public Optional<V> getV() {
        return v;
    }

   @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MultipleAnswer))
            throw IllegalEqualityExceptionGenerator.create(MultipleAnswer.class, o.getClass());
        MultipleAnswer<?, ?, ?> answer = (MultipleAnswer<?, ?, ?>) o;
        return Objects.equals(getT(), answer.getT()) &&
                Objects.equals(getU(), answer.getU()) &&
                Objects.equals(getV(), answer.getV());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getT(), getU(), getV());
    }
}
