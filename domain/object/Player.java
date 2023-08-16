package domain.object;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Player {
    private final String name;
    private final Color color;
    private boolean alive  = true;
    private Set<Card> armyAndTerritoryCards;
    private Set<Territory> territories;
    private Integer playerNum;
    private static Set<Player> allPlayers = new HashSet<>();
    private static Integer playerCount = 0;



    public Player(String name, String color1){
        Color color = this.getColorFromName(color1);
        this.armyAndTerritoryCards = new HashSet<>();
        this.territories = new HashSet<>();
        this.name = name;
        this.color = color;

        Player.playerCount += 1;
        this.playerNum = Player.playerCount;

        Player.allPlayers.add(this);
    }
    public Player(String name, Color color){

        this.armyAndTerritoryCards = new HashSet<>();
        this.territories = new HashSet<>();
        this.name = name;
        this.color = color;

        Player.playerCount += 1;
        this.playerNum = Player.playerCount;

        Player.allPlayers.add(this);
    }


    public void addTerritory(Territory territory){
        this.territories.add(territory);
    }
    public void removeTerritory(Territory territory){
        this.territories.remove(territory);
        if (this.territories.size() == 0){
            this.kill();
        }
    }
    private void kill(){
        this.alive = false;
        Game.getGame().playerDied(this);
    }

    public void addCard(Card c){
        this.armyAndTerritoryCards.add(c);
    }
    public void removeCard(Card c){
        this.armyAndTerritoryCards.remove(c);
    }

    public String getName(){
        return this.name;
    }
    public Color getColor(){
        return this.color;
    }

    public boolean isAlive(){
        return this.alive;
    }

    public static Set<Player> getAllPlayers(){
        return new HashSet<>(Player.allPlayers);
    }
    public static int getNumOfPlayers(){
        return Player.getAllPlayers().size();
    }

    public HashSet<Territory> getTerritories(){
        return new HashSet<>(this.territories);
    }

    public ArrayList<Card> getArmyAndTerritoryCards(){
        return new ArrayList<>(this.armyAndTerritoryCards);
    }
    public Integer getPlayerNum() { return this.playerNum; }
    public void setPlayerNum(Integer num){this.playerNum = num;}

    private Color getColorFromName(String colorName) {
        Color c;
        switch (colorName){
            case "Red" -> c = Color.RED;
            case "Blue" -> c = Color.BLUE;
            case "Green" -> c = Color.GREEN;
            case "Cyan" -> c = Color.CYAN;
            case "Orange" -> c = Color.ORANGE;
            case "Purple" -> c = Color.MAGENTA;
            default -> c = null;
        }
        return c;
    }

    @Override
    public String toString() {
        return "Player name: " + name + '\'';
    }



}
