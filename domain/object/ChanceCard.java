package domain.object;

import java.util.ArrayList;
import java.util.HashMap;

// to modify
public class ChanceCard extends Card{

    private ChanceCardStrategy strategy;
    private static ArrayList<ChanceCard> allChanceCards = new ArrayList<>();

    private ChanceCard(ChanceCardStrategy strategy) {
        this.name = strategy.getName();
        this.strategy = strategy;
        ChanceCard.allChanceCards.add(this);
    }

    // to be called in main
    public static void createChanceCards(ArrayList<ChanceCardStrategy> strategies){
        for (ChanceCardStrategy strategy: strategies){
            new ChanceCard(strategy);
        }
    }

    public static ArrayList<ChanceCard> getAllChanceCards(){
        return ChanceCard.allChanceCards;
    }

    public void use(Turn turn){
        this.strategy.use(turn);
    }

    public String getExplanation(){
        return this.strategy.getExplanation();
    }

    public ArrayList<ChanceCardRequirement> getRequirements(){
        return this.strategy.getRequirements();
    }

    public boolean setRequirements(Turn turn, HashMap<ChanceCardRequirement, Object> requirements){
        return this.strategy.setRequirements(turn, requirements);
    }

}
