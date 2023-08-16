package controller;

import domain.object.BuildGame;
import domain.object.Player;
import domain.object.Territory;

import java.awt.*;
import java.util.*;

public class BuildGameHandler implements Handler{

    private final BuildGame buildGame;

    public BuildGameHandler(BuildGame buildGame){
        HandlerWrapper.addHandler("BuildGameHandler", this);
        this.buildGame = buildGame;
    }

    public boolean setNumberOfPlayers(int num){
        return this.buildGame.setNumberOfPlayers(num);
    }

    public int getNumOfPlayers(){
        return this.buildGame.getNumOfPlayers();
    }

    public boolean createPlayer(String name, Color color){
        return this.buildGame.createPlayer(name, color);
    }

    public boolean goCreateMap(){
        return this.buildGame.goCreateMap();
    }

    public void enableTerritory(int territoryID){
        this.buildGame.enableTerritory(Territory.findFromId(territoryID));
    }

    public void disableTerritory(int territoryID){
        this.buildGame.disableTerritory(Territory.findFromId(territoryID));
    }

    // adds or removes the link between two territories
    public void setLink(int territoryID1, int territoryID2){
        this.buildGame.setLink(Territory.findFromId(territoryID1), Territory.findFromId(territoryID2));
    }
    public HashMap getLinks(){
        return this.buildGame.getLinks();
    }

    public boolean finalizeMap(){
        return this.buildGame.finalizeMap();
    }

    // UI uses this method
    // first it uses the bag object and creates a treemap by matching the picked values with players
    // then calls the controller (düşünülür)
    public void createTurnList(TreeMap<Integer, Player> map){
        this.buildGame.createTurnList(map);
    }

    public Integer pickANumberFromBag(){ return this.buildGame.pickANumberFromBag();}
    public Set<Player> getAllPlayers(){
        return this.buildGame.getAllPlayers();
    }

    public void start() { this.buildGame.start(); }

}

