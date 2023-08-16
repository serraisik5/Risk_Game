package domain.object;

import java.util.*;
import java.util.Map;

public class SabotageChanceStrategy extends ChanceCardStrategy{
    public SabotageChanceStrategy(){
        this.name = "Sabotage Chance Card";
        this.explanation = "This card allows a player to choose one territory " +
                "belonging to another player and remove a certain number of armies " +
                "from it. The number of armies removed is determined by a roll of " +
                "the dice.";
        this.requirements = new ArrayList<>(List.of(ChanceCardRequirement.TERRITORY_TO));
    }

    @Override
    protected void customisedUse(Turn currentTurn) {
        GameMaster master = GameMaster.getMaster();
        Integer toDecrease = master.roll("The die is cast for the number of army to sabotage on the selected enemy territory!");
        Territory from = ((Territory) this.currentRequirements.get(ChanceCardRequirement.TERRITORY_TO));
        master.decrementTerritoryArmyNumber(from, toDecrease);
    }

    @Override
    public boolean setRequirements(Turn currentTurn, HashMap<ChanceCardRequirement, Object> requirements) {
        for (Map.Entry<ChanceCardRequirement, Object> entry : requirements.entrySet()) {
            ChanceCardRequirement key = entry.getKey();
            if (Objects.requireNonNull(key) == ChanceCardRequirement.TERRITORY_TO) {
                if (currentTurn.getPlayer() == ((Territory) entry.getValue()).getOwner())
                    return false;
            }
        }
        this.currentRequirements = requirements;
        return true;
    }
}
