package dev.stonks.takenoko.objective;

import dev.stonks.takenoko.bot.Player;
import dev.stonks.takenoko.map.Map;

public class EmperorObjective extends Objective {
    public EmperorObjective(){
        super(2);
    }

    @Override
    public void checkObjective(Map map, Player player) {
        throw new IllegalCallerException("This method should not be used on Emperor Objective");
    }
}
