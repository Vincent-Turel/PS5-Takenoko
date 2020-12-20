package dev.stonks.takenoko.bot;

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
}
