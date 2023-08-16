package domain.object;

import java.util.*;
import java.util.Map;

public class RevoltChanceStrategy extends ChanceCardStrategy{
    public RevoltChanceStrategy(){
        this.name = "Revolt Chance Card";
        this.explanation = "Play this card to remove all armies from " +
                "one of your territories and add them to another.";
        this.requirements = new ArrayList<>(List.of(ChanceCardRequirement.TERRITORY_FROM,
                                                    ChanceCardRequirement.TERRITORY_TO));
    }

    @Override
    protected void customisedUse(Turn currentTurn) {
        GameMaster master = GameMaster.getMaster();
        Territory from = ((Territory) this.currentRequirements.get(ChanceCardRequirement.TERRITORY_FROM));
        Territory to = ((Territory) this.currentRequirements.get(ChanceCardRequirement.TERRITORY_TO));
        int numOfArmy = from.getNumOfArmy() - 1;
        master.decrementTerritoryArmyNumber(from, numOfArmy);
        master.incrementTerritoryArmyNumber(to, numOfArmy);
    }

    @Override
    public boolean setRequirements(Turn currentTurn, HashMap<ChanceCardRequirement, Object> requirements) {
        for (Map.Entry<ChanceCardRequirement, Object> entry : requirements.entrySet()) {
            ChanceCardRequirement key = entry.getKey();
            switch(key){
                case TERRITORY_FROM, TERRITORY_TO -> {
                    if (currentTurn.getPlayer() != ((Territory) entry.getValue()).getOwner()){
                        return false;
                    }
                }
            }
        }
        this.currentRequirements = requirements;
        return true;
    }
}
