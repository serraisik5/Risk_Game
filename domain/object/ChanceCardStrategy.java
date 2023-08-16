package domain.object;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class ChanceCardStrategy {

    protected String name;
    protected String explanation;
    protected ArrayList<ChanceCardRequirement> requirements;
    protected HashMap<ChanceCardRequirement, Object> currentRequirements = null;

    public String getName(){
        return this.name;
    };
    public String getExplanation(){
        return this.explanation;
    }


    public void use(Turn currentTurn){
        this.customisedUse(currentTurn);
        this.currentRequirements = null;
    }

    // return the requirements
    // these aren't the requirements set by the user but what this strategy needs so that it can be used
    public ArrayList<ChanceCardRequirement> getRequirements(){
        return this.requirements;
    }


    protected abstract void  customisedUse(Turn currentTurn);

    // user set these requirements as they want
    public abstract boolean setRequirements(Turn currentTurn, HashMap<ChanceCardRequirement, Object> requirements);
}
