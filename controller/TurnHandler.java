package controller;

import listener.*;
import domain.object.*;

import java.util.ArrayList;
import java.util.HashMap;

public class TurnHandler implements Handler {

    private Turn turn;

    public TurnHandler(Turn turn){
        this.turn = turn;
        HandlerWrapper.addHandler(String.format("%sTurnHandler", turn.getPlayer().getName()), this);
    }

    public boolean selectInitialTerritory(int territoryID){
        return this.turn.selectInitialTerritory(Territory.findFromId(territoryID));
    }

    // DEPLOY ONE ARMY TO YOUR TERRITORY
    // called in TERRITORYSEELCTION phase
    public boolean deployOneArmy(int territoryID){
        return this.turn.deployOneArmy(Territory.findFromId(territoryID));
    }

    public ChanceCard pickAChanceCard(){
        return this.turn.pickAChanceCard();
    }

    // USE CHANCE CARD
    // called in CHANCECARD phase
    // if returned false user will be prompted to provide again the requirements
    public boolean useChanceCard(HashMap<ChanceCardRequirement, Object> requirements){
        HashMap<ChanceCardRequirement, Object> reqsToPass = new HashMap<>();
        for (HashMap.Entry<ChanceCardRequirement, Object> entry : requirements.entrySet()){
            switch(entry.getKey()){
                case TERRITORY_TO, TERRITORY_FROM -> reqsToPass.put(entry.getKey(),
                        Territory.findFromId((Integer) entry.getValue()));
                default -> reqsToPass.put(entry.getKey(), entry.getValue());
            }
        }
        return this.turn.useChanceCard(reqsToPass);
    }

    // to be called in ARMYDEPLOYMENT phase
    public boolean mergeCards(ArrayList<Card> toMergeList){
        return this.turn.mergeCards(toMergeList);
    }

    // DEPLOY ARMY
    public boolean deployMultipleArmies(int territoryID, int numOfArmies){
        return this.turn.deployMultipleArmies(Territory.findFromId(territoryID), numOfArmies);
    }

    public void setPhase(TurnPhase phase){
        this.turn.setPhase(phase);
    }
    // ATTACK
    // forth argument is only for type 3 otherwise null
    public boolean constructAttack(int fromID, int toID, int type, Integer n){
        return this.turn.constructAttack(Territory.findFromId(fromID), Territory.findFromId(toID), type, n);
    }

    public boolean attack(){
        return this.turn.attack();
    }
    public boolean attackFortifier(int numOfArmy){
        return this.turn.attackFortifier(numOfArmy);
    }

    // as attack can be done until the player no more want to, the phase change will be done
    // using a NextPhase button in UI
    // so unlike finishTurn for fortify, finishAttack will be called only from UI
    // not from attack itself
    public void finishAttack(){
        this.turn.finishAttack();
    }

    // FORTIFY
    public boolean fortify(int fromID, int toID, int numOfArmies){
        return this.turn.fortify(Territory.findFromId(fromID), Territory.findFromId(toID), numOfArmies);
    }

    // called directly from UI or called from UI using fortify
    public void finishTurn(){
        this.turn.finishTurn();
    }

    public int getArmyToDeploy(){
        return this.turn.getArmyToDeploy();
    }

    public Player getTurnPlayer(){
        return this.turn.getPlayer();
    }

    public ArrayList<Card> getArmyAndTerritoryCards(){
        return this.turn.getArmyAndTerritoryCards();
    }

    public String turnToString() {
        return this.turn.toString();
    }

    public void subscribe(TurnListener listener){
        this.turn.subscribe(listener);
    }

    ///////////////////////// FOR TESTING PURPOSES //////////////////////////////
    public TurnPhase getPhase(){
        return this.turn.getPhase();
    }
    public Territory getARandomTerritoryFromOwner(){
        return this.turn.getARandomTerritoryFromOwner();
    }

}
