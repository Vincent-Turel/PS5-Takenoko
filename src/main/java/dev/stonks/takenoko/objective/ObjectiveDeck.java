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

    public boolean deckEmpty(){
        return gardenerDeck.size()>0 || patternDeck.size()>0 || pandaDeck.size()>0;
    }

    public int deckSize(ObjectiveKind name){
        if(name.equals(ObjectiveKind.PandaObjective)){
            return pandaDeck.size();
        }
        if(name.equals(ObjectiveKind.PatternObjective)){
            return patternDeck.size();
        }
        if(name.equals(ObjectiveKind.GardenerObjective)){
            return gardenerDeck.size();
        }
        else{
            return 0;
        }
    }

    public PatternObjective getAnPatternObjective(int n){
        PatternObjective obj = patternDeck.get(n);
        patternDeck.remove(n);
        return obj;
    }

    public PandaObjective getAnPandaObjective(int n){
        PandaObjective obj = pandaDeck.get(n);
        pandaDeck.remove(n);
        return obj;
    }

    public GardenerObjective getAnGardenerObjective(int n){
        GardenerObjective obj = gardenerDeck.get(n);
        gardenerDeck.remove(n);
        return obj;
    }

}
