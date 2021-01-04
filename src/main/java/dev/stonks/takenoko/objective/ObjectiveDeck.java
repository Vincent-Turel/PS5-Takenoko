package dev.stonks.takenoko.objective;
import java.util.ArrayList;

public class ObjectiveDeck {

    private ArrayList<GardenerObjective> gardenerDeck;
    private ArrayList<PatternObjective> patternDeck;
    private ArrayList<PandaObjective> pandaDeck;
    private EmperorObjective emperor;

    public ObjectiveDeck(){
        this.gardenerDeck = ObjectivesBambooFactory.gardenerObjectiveList();
        this.patternDeck = PatternObjectiveFactory.validPatternObjectives();
        this.pandaDeck = ObjectivesBambooFactory.pandaObjectiveList();
        this.emperor = new EmperorObjective();
    }

    public boolean deckImpty(){
        return gardenerDeck.size()>0 || patternDeck.size()>0 || pandaDeck.size()>0;
    }

}
