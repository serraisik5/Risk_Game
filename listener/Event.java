package listener;

import domain.object.ChanceCardRequirement;
import domain.object.Map;
import domain.object.Turn;
import domain.object.TurnPhase;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class Event {
	// event type, old value of the change, and the new value
		// for example old val changed to new val informaiton showed
    private final String functionName;
    private boolean isSuccessful;
    private String oldStr;
    private String newStr;
    private int oldInt;
    private int newInt;
    private LinkedList<Turn> turnList;
    private Map map;
    private String winner;
    private boolean aBoolean;
    private TurnPhase newPhase;
    private Color color;
    private ArrayList<ChanceCardRequirement> requirements;
    private String explanation;

    public Event(String functionName, String newStr){
        this.functionName = functionName;
        this.newStr = newStr;
    }
    public Event(String functionName){
        this.functionName = functionName;
    }
    public Event(String functionName, String oldStr, String newStr){
        this.functionName = functionName;
        this.newStr = newStr;
        this.oldStr = oldStr;
    }
    public Event(String functionName, boolean isSuccessful){
        this.functionName = functionName;
        this.isSuccessful = isSuccessful;
    }
    public Event(String functionName, int newInt){
        this.functionName = functionName;
        this.newInt = newInt;
    }
    public Event(String functionName, LinkedList<Turn> turnList){
        this.functionName = functionName;
        this.turnList = turnList;
    }
    public Event(String functionName, Map map){
        this.functionName = functionName;
        this.map = map;
    }
    public Event(String functionName, String winner, boolean aBoolean){
        this.functionName = functionName;
        this.winner = winner;
        this.aBoolean = aBoolean;
    }
    public Event(String functionName, TurnPhase newPhase){
        this.functionName = functionName;
        this.newPhase = newPhase;
    }

    public Event(String functionName, Color color){
        this.functionName = functionName;
        this.color = color;
    }

    public Event(String functionName, String cardName, String explanation, ArrayList<ChanceCardRequirement> requirements){
        this.functionName = functionName;
        this.newStr = cardName;
        this.explanation = explanation;
        this.requirements = requirements;
    }

    public Event(String functionName, String explanation, int number){
        this.functionName = functionName;
        this.explanation = explanation;
        this.newInt = number;
    }




    public String getFunctionName(){
        return this.functionName;
    }

    public String getOldSting(){
        return this.oldStr;
    }

    public String getNewString(){
        return this.newStr;
    }

    public int getOldInt(){
        return this.oldInt;
    }
    public int getNewInt(){
        return this.newInt;
    }

    public boolean getIsSuccessful(){
        return this.isSuccessful;
    }

    public LinkedList<Turn> getTurnList(){
        return this.turnList;
    }

    public Map getMap(){
        return this.map;
    }

    public String getWinner(){
        return this.winner;
    }

    public boolean getABoolean(){
        return this.aBoolean;
    }

    public TurnPhase getNewPhase(){
        return this.newPhase;
    }

    public Color getColor(){
        return this.color;
    }
    public ArrayList<ChanceCardRequirement> getRequirements(){
        return this.requirements;
    }
    public String getExplanation(){
        return this.explanation;
    }



}
