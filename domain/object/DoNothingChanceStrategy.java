package domain.object;

import java.util.ArrayList;
import java.util.HashMap;

public class DoNothingChanceStrategy extends ChanceCardStrategy{

    public DoNothingChanceStrategy(){
        this.name = "DoNothing ChanceCard";
        this.explanation = "This chance card does nothing!";
        this.requirements = new ArrayList<ChanceCardRequirement>();
    }

    @Override
    public void customisedUse(Turn t){ }


    @Override
    public boolean setRequirements(Turn currentTurn, HashMap<ChanceCardRequirement, Object> requirements) {
        return true;
    }
}
