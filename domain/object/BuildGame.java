package domain.object;

import java.awt.*;
import java.util.*;

public class BuildGame {

    private int numOfPlayers;
    private final Map map = Map.getMap();
    private LinkedList<Player> turnList = new LinkedList<>();
    private final int MAX_PLAYER = 6;
    private final int MIN_PLAYER = 2;
    private HashMap<Territory,Territory> links = new HashMap<>();

    public BuildGame(){}

    // UI
    public boolean setNumberOfPlayers(int num){
        // @requires num <= 6 && num >= 2
        // @effects return boolean for correct set of numberOfPlayers, create army cards
        if (num <= MAX_PLAYER && num >= MIN_PLAYER){

            this.numOfPlayers = num;
            ArmyCard.createArmyCards(num);
            return true;
        }
        return false;
    }

    public int getNumOfPlayers(){
        return this.numOfPlayers;
    }


    // UI
    public boolean createPlayer(String name, Color color){
        // @requires string and enum type Color
        // @effects add player to players list
        if (Player.getNumOfPlayers() < this.numOfPlayers){
            // all players are stored in static variable of Player class
            Player newPlayer = new Player(name, color);
            return true;
        }
        return false;
    }

    // UI kullanamadık
    public boolean goCreateMap(){
        return (Player.getNumOfPlayers() != 0 && Player.getNumOfPlayers() == this.numOfPlayers);
    }

    public void enableTerritory(Territory territory){
        // @requires Territory object that has been created already
        // @modifies territory
        // @effects set enable = true for the given territory
        map.enableTerritory(territory);
    }

    public void disableTerritory(Territory territory){
        map.disableTerritory(territory);
    }

    // adds or removes the link between two territories
    public void setLink(Territory t1, Territory t2){
        // @requires 2 Territory objects that has been created already
        // @effects make those two territories adjacent or remove the adjacency by deleting it from neighbours list
        if (t1.isAdjacent(t2)){
            this.map.removeLink(t1, t2);
            links.remove(t1, t2);
        }
        else {

            this.map.addLink(t1, t2);
            links.put(t1,t2);
        }
    }
    public HashMap<Territory, Territory> getLinks() {
        return links;
    }

    // UI
    public boolean finalizeMap(){
        return this.map.finalizeMap(this.numOfPlayers);
    }

    // UI
    public Integer pickANumberFromBag(){
        return Bag.pickNumber();
    }

    public Set<Player> getAllPlayers(){
        return Player.getAllPlayers();
    }

    // UI uses this method
    // first it uses the bag object and creates a treemap by matching the picked values with players
    // then calls the controller (düşünülür)
    public void createTurnList(TreeMap<Integer, Player> map){
        // @requires TreeMap with players and their integers.
        // @effects add the players in order to turn list
        for (java.util.Map.Entry<Integer, Player> entry : map.entrySet()){
            this.turnList.add(entry.getValue());
        }
    }
    public LinkedList<Player> getTurnList(){
        return turnList;
    }

    // UI
    public boolean start(){
        try{
            Game game = Game.getGame();

            game.setTurnList(this.turnList, false);
            game.setMap(Map.getMap());

            ArrayList<Card> combinedList = new ArrayList<>();

            combinedList.addAll(TerritoryCard.getAllTerritoryCards());
            combinedList.addAll(ArmyCard.getAllArmyCards());

            Collections.shuffle(combinedList);

            game.setArmyAndTerritoryCards(combinedList);
            game.setChanceCards(ChanceCard.getAllChanceCards());

            game.nextTurn();

            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
