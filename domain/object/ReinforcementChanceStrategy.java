package domain.object;

import java.util.*;
import java.util.Map;

public class ReinforcementChanceStrategy extends ChanceCardStrategy {

    public ReinforcementChanceStrategy(){
        this.name = "Reinforcement Chance Card";
        this.explanation = "This card allows a player to add a certain number of " +
                "armies to one of their territories. The number of armies added is " +
                "determined by a roll of the dice.";
        this.requirements = new ArrayList<>(List.of(ChanceCardRequirement.TERRITORY_TO));
    }

    @Override
    protected void customisedUse(Turn currentTurn) {
        GameMaster master = GameMaster.getMaster();
        Integer diceValue = master.roll("The die is cast for the number of army to add to the selected territory!");
        Territory to = ((Territory) this.currentRequirements.get(ChanceCardRequirement.TERRITORY_TO));
        master.incrementTerritoryArmyNumber(to, diceValue);
    }

    @Override
    public boolean setRequirements(Turn currentTurn, HashMap<ChanceCardRequirement, Object> requirements) {
        for (Map.Entry<ChanceCardRequirement, Object> entry : requirements.entrySet()) {
            ChanceCardRequirement key = entry.getKey();
            if (Objects.requireNonNull(key) == ChanceCardRequirement.TERRITORY_TO) {
                if (currentTurn.getPlayer() != ((Territory) entry.getValue()).getOwner())
                    return false;
            }
        }
        this.currentRequirements = requirements;
        return true;
    }
}
