package domain.object;

import java.util.*;
import java.util.Map;

public class NuclearStrikeChanceStrategy extends ChanceCardStrategy{
    public NuclearStrikeChanceStrategy(){
        this.name = "Nuclear Strike Chance Card";
        this.explanation = "This card allows you to wipe out all armies in one " +
                "territory, regardless of how many there are, but at the cost of " +
                "destroying one of your closest territory to that territor as well.";
        this.requirements = new ArrayList<>(List.of(ChanceCardRequirement.TERRITORY_TO));
    }

    @Override
    protected void customisedUse(Turn currentTurn) {
        GameMaster master = GameMaster.getMaster();
        Territory to = ((Territory) this.currentRequirements.get(ChanceCardRequirement.TERRITORY_TO));
        master.destroy(to);
        Player cardOwner = currentTurn.getPlayer();
        Territory ownerSideDestroy = master.BFSPlayerTerritory(to, cardOwner);
        master.destroy(ownerSideDestroy);
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
