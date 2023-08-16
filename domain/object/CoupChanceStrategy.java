package domain.object;

import java.util.*;
import java.util.Map;

public class CoupChanceStrategy extends ChanceCardStrategy {


    public CoupChanceStrategy(){
        this.name = "Coup Chance Card";
        this.explanation = "This card allows a player to take control of one " +
                "of their opponent's territories without a fight. The opponent " +
                "loses all armies on that territory, and the player gains control of it.";
        this.requirements = new ArrayList<>(List.of(ChanceCardRequirement.TERRITORY_TO));
    }

    @Override
    protected void customisedUse(Turn currentTurn) {
        GameMaster master = GameMaster.getMaster();
        Territory to = ((Territory) this.currentRequirements.get(ChanceCardRequirement.TERRITORY_TO));
        master.setTerritoryArmy(to, 1);
        master.setTerritoryOwner(to, currentTurn.getPlayer());
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
