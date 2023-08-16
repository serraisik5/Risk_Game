package storageservice;

import domain.object.*;


import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class CSVStorageServiceAdapter implements IStorageServiceAdapter {

    private CSVStorageService csvService;
    private final String territoriesPath = "src/data/territories.csv";
    private final String territoryLinksPath = "src/data/territoryLinks.csv";
    private final String savedTerritoriesPath = "src/data/savedGame/territories.csv";
    private final String savedTerritoryLinksPath = "src/data/savedGame/adjacents.csv";
    private final String savedPlayersPath = "src/data/savedGame/players.csv";
    private final String savedArmyAndTerritoryCardsPath = "src/data/savedGame/a_t_cards.csv";

    public CSVStorageServiceAdapter(){
        this.csvService = new CSVStorageService();
    }
    @Override
    public List<String[]> getTerritoryLines() {
        return this.csvService.readFile(this.territoriesPath, ",");
    }
    @Override
    public List<String[]> getAdjacentIndicesLines() {
        return this.csvService.readFile(this.territoryLinksPath, ",");
    }

    @Override
    public void saveTerritories(Set<Territory> territories) {
        String ter_path = this.savedTerritoriesPath;
        String adj_path = this.savedTerritoryLinksPath;

        for (Territory t : territories){
            String entry = this.constructTerritoryEntry(t);
            String adjs = this.constructAdjacencyEntry(t);
            this.csvService.writeFile(entry, ter_path);
            this.csvService.writeFile(adjs, adj_path);
        }
        System.out.println("File write operation completed.");

    }

    @Override
    public void savePlayers(LinkedList<Player> players) {
        String p_path = this.savedPlayersPath;
        String a_t_path = this.savedArmyAndTerritoryCardsPath;
        for (Player p : players){
            String entry = this.constructPlayerEntry(p);
            String a_t_entry = this.constructArmyTerritoryCardEntries(p);
            this.csvService.writeFile(entry, p_path);

            this.csvService.writeFile(a_t_entry, a_t_path);
        }
    }

    @Override
    public void saveArmyAndTerritoryCards(ArrayList<Card> cards) {
        String a_t_path = this.savedArmyAndTerritoryCardsPath;
        for (Card c : cards){
            String entry = constructArmyAndTerritoryEntry(0, c);
            this.csvService.writeFile(entry, a_t_path);
        }
    }

    @Override
    public void resetSavedFiles() {
        this.csvService.resetFile(savedTerritoriesPath);
        this.csvService.resetFile(savedTerritoryLinksPath);
        this.csvService.resetFile(savedPlayersPath);
        this.csvService.resetFile(savedArmyAndTerritoryCardsPath);
    }

    @Override
    public List<String[]> loadPlayers() {
        String savedPlayersPath = this.savedPlayersPath;
        return this.csvService.readFile(savedPlayersPath, ",");
    }

    @Override
    public List<String[]> loadTerritories() {
        String savedTerritoriesPath = this.savedTerritoriesPath;
        return this.csvService.readFile(savedTerritoriesPath, ",");
    }

    @Override
    public List<String[]> loadNeighbors() {
        String savedTerritoryLinksPath = this.savedTerritoryLinksPath;
        return this.csvService.readFile(savedTerritoryLinksPath, ",");
    }

    @Override
    public List<String[]> loadArmyAndTerritoryCards() {
        String savedArmyAndTerritoryCardsPath = this.savedArmyAndTerritoryCardsPath;
        return this.csvService.readFile(savedArmyAndTerritoryCardsPath, ",");
    }

    private String constructTerritoryEntry(Territory t){
        String entry =
                        t.getId() +
                        "," +
                        (t.isEnabled() ? 1 : 0) +
                        "," +
                        t.getNumOfArmy() +
                        "," +
                        (t.isEnabled() ? t.getOwner().getPlayerNum() : "nan");
        return entry;
    }

    private String constructAdjacencyEntry(Territory t){
        StringBuilder adj_entry = new StringBuilder(String.valueOf(t.getId()));
        for (Territory ter : t.getNeighbors()){
            adj_entry.append(",");
            adj_entry.append(ter.getId());
        }
        return adj_entry.toString();
    }

    private String constructPlayerEntry(Player p){

        String p_entry = p.getPlayerNum() +
                "," +
                p.getName() +
                "," +
                this.getColorName(p.getColor());
        return p_entry;
    }

    private String getColorName(Color color) {
        if (Color.RED.equals(color)) {
            return "Red";
        } else if (Color.BLUE.equals(color)) {
            return "Blue";
        } else if (Color.GREEN.equals(color)) {
            return "Green";
        } else if (Color.CYAN.equals(color)) {
            return "Cyan";
        } else if (Color.ORANGE.equals(color)) {
            return "Orange";
        } else if (Color.MAGENTA.equals(color)) {
            return "Purple";
        } else {
            return "Unknown";
        }
    }

    private String constructArmyTerritoryCardEntries(Player p){
        StringBuilder toReturn = new StringBuilder();
        for (Card c : p.getArmyAndTerritoryCards()){
            String entry = constructArmyAndTerritoryEntry(p.getPlayerNum(), c);
            toReturn.append(entry);
            toReturn.append("\n");
        }

        int length = toReturn.length();
        if (length > 0 && toReturn.charAt(length - 1) == '\n') {
            toReturn.setLength(length - 1);
        }
        return toReturn.toString();
    }

    private String constructArmyAndTerritoryEntry(int id, Card c){
        StringBuilder toReturn = new StringBuilder();
        toReturn.append(id);
        toReturn.append(",");
        toReturn.append(c.getName());
        toReturn.append(",");
        if (c instanceof ArmyCard){
            toReturn.append("1");
        }
        else if (c instanceof TerritoryCard) {
            toReturn.append("0");
        }
        return toReturn.toString();
    }
}

