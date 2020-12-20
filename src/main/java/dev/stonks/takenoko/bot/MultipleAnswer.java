package dev.stonks.takenoko.bot;

import java.util.Objects;
import java.util.Optional;

public class MultipleAnswer<T, U> {
    private final T t;
    private final U u;

    public MultipleAnswer(T t, U u){
        this.t = t;
        this.u = u;
    }

    public T getT() {
        return t;
    }

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
