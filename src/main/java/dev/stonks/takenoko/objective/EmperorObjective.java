package dev.stonks.takenoko.objective;

import dev.stonks.takenoko.IllegalEqualityExceptionGenerator;
import dev.stonks.takenoko.bot.Player;
import dev.stonks.takenoko.map.Map;

public class EmperorObjective extends Objective {
    public EmperorObjective() {
        super(2);
    }

    @Override
    public void checkObjectiveValid(Map map, Player player) {
        throw new IllegalCallerException("This method should not be used on Emperor Objective");
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Objective)) throw IllegalEqualityExceptionGenerator.create(EmperorObjective.class, o);

        return o instanceof EmperorObjective;
    }

    public ObjectiveKind getKind() {
        throw new IllegalCallerException("Emperor objective should not be adapted.");
    }
}
