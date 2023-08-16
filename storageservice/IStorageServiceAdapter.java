package storageservice;

import domain.object.Card;
import domain.object.Player;
import domain.object.Territory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public interface IStorageServiceAdapter {
    public List<String[]> getTerritoryLines();
    public List<String[]> getAdjacentIndicesLines();
    public void saveTerritories(Set<Territory> territories);
    public void savePlayers(LinkedList<Player> players);
    public void saveArmyAndTerritoryCards(ArrayList<Card> cards);
    public void resetSavedFiles();
    public List<String[]> loadPlayers();
    public List<String[]> loadTerritories();
    public List<String[]> loadNeighbors();
    public List<String[]> loadArmyAndTerritoryCards();
}