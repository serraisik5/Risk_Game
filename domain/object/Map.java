package domain.object;

import java.util.*;

public class Map {
    //@overview Create the map object with given territories and useful methods for map manipulation
    /**
     * Abstraction Function:
     * A Map map represents a game map containing all the territories in `map.territories`.
     */
    /**
     * Representation Invariant:
     * - territories should be enabled
     * - territories should contain no duplicates
     * - each territory in territories must be a valid Territory
     * - no territory in territories should be null
     */
    private Set<Territory> territories; // the rep
    public static Map map;



    private Map(Set<Territory> allTerritories){
        this.territories = allTerritories;
    }
    public static Map getMap(){
        //@effects return the singleton map object
        if (Map.map != null){
            return Map.map;
        }
        Map.map = new Map(Territory.getAllTerritories());
        return map;
    }

    public void disableTerritory(Territory territory){
        //@requires existing territory
        //@modifies territory
        //@effects enabled = false for the given territory
        territory.disable();
    }
    public void enableTerritory(Territory territory){
        //@requires existing territory
        //@modifies territory
        //@effects enabled = false for the given territory
        territory.enable();
    }

    public void addLink(Territory ter1, Territory ter2){
        //@requires existing 2 territories
        //@modifies ter1, ter2
        //@effects make two territories neighbours.

        ter1.addNeighbor(ter2);
        ter2.addNeighbor(ter1);
    }
    public void removeLink(Territory ter1, Territory ter2){
        //@requires existing 2 territories
        //@modifies ter1, ter2
        //@effects remove territory from others neighbours list.

        ter1.removeNeighbor(ter2);
        ter2.removeNeighbor(ter1);
    }

    public Set<Territory> getTerritories(){
        // @effects return the territories hashset of the map.
        return new HashSet<>(this.territories);
    }



    public Territory findTerFromName(String name){
        //@requires string
        //@modifies name
        //@effects search for a territory in the territories list with the given name
        name = name.replace("i", "ı").replace("İ","I").toUpperCase();
        for (Territory ter : this.territories){
            if (ter.getName().equals(name)){
                return ter;
            }
        }
        return null;
    }

    // for loading the game
    public void finalizeMapMaster(Set<Territory> enabledTerritories){
        this.territories = enabledTerritories;
    }

    // if the map obeys the rules
    // finalizes the territories of the map with enabled territories
    public boolean finalizeMap(int numOfPlayers){
        // @requires integer between 2 and 6
        // @effects update territories list with enabled and disabled information. Create territory cards

        if (this.controlMap(numOfPlayers)) {

            Set<Territory> enabledTerritories = new HashSet<>();
            for (Territory ter : this.territories) {
                if (ter.isEnabled()) {
                    enabledTerritories.add(ter);
                }
            }
            this.territories = enabledTerritories;
            TerritoryCard.createTerritoryCards(new HashSet<>(this.territories));

            return true;
        }
        return false;
    }

    private boolean controlMap(int numOfPlayers){
        // @requires integer between 2 and 6
        // @effects checks if the map created correctly. return false if not.
        Territory startNode = null;
        Set<Territory> reachable = new HashSet<>();

        // controls reachability of all enabled territories
        for (Territory t : this.territories){
            if (t.isEnabled()){
                startNode = t;
            }
            else {
                reachable.add(t);
            }
        }

        if (startNode != null){
            BFSAll(startNode, reachable);
        }
        else {return false;}
        if (reachable.size() != this.territories.size()) { return false; }

        // continents must be composed of at least 3 territories,
        // num of continents is greater or equal to numOfPlayers
        HashMap<String, Integer> continentNumTable = this.extractContinentNumberPairs();
        if (continentNumTable.keySet().size() < numOfPlayers) { return false; }
        for (Integer num : continentNumTable.values()){
            if (num < 3){ return false; }
        }

        return true;
    }

    private static void BFSAll(Territory startNode, Set<Territory> reachable) {
            // @requires an initial territory and set of territories
            // @modifies territory
            // @effects add territory to reachable list if it can be reached.
        LinkedList<Territory> queue = new LinkedList<>();
        queue.add(startNode);
        while (queue.size() > 0){
            Territory curTerritory = queue.pop();
            reachable.add(curTerritory);
            for (Territory t : curTerritory.getNeighbors()){
                if (t.isEnabled() && !reachable.contains(t)) {
                    queue.add(t);
                }
            }
        }
    }

    public Territory BFSPlayerTerritory(Territory startNode, Player toFind){
        LinkedList<Territory> queue = new LinkedList<>();
        Set<Territory> checked = new HashSet<>();
        queue.add(startNode);
        while (queue.size() > 0){
            Territory curTerritory = queue.pop();
            if (curTerritory.getOwner() == toFind){
                return curTerritory;
            }
            checked.add(curTerritory);
            for (Territory t : curTerritory.getNeighbors()){
                if (t.isEnabled() && !checked.contains(t)) {
                    queue.add(t);
                }
            }
        }
        return null;
    }

    // the map is not finalized, check enabled status
    private HashMap<String, Integer> extractContinentNumberPairs(){
        // @modifies this
        // @effects create hashmap with the name of the continent and how many territories on it.
        HashMap<String, Integer> continentNumTable = new HashMap<>();
        for (Territory t : this.territories){
            String continent = t.getContinent();
            if (!t.isEnabled()){
                continue;
            }
            if (continentNumTable.containsKey(continent)){
                continentNumTable.put(continent, continentNumTable.get(continent) + 1);
            }
            else{
                continentNumTable.put(continent, 1);
            }
        }
        return continentNumTable;
    }

    public boolean isLeftWithoutOwner(){
        // @effects checks if any territory left without owner in the map. If there is return false.
        for (Territory t: this.territories){
            if (t.getOwner() == null){
                return true;
            }
        }
        return false;
    }

    public ArrayList<Territory> getTerritoriesOfAContinent(String continent){
        // @requires existing continent
        // @effects return a list of territories that is on the given territory
        ArrayList<Territory> toReturn = new ArrayList<>();
        for (Territory t: this.getTerritories()){
            if (t.getContinent().equals(continent)) {toReturn.add(t);}
        }
        return toReturn;
    }

    public static boolean isLinkedWithSameOwner(Territory startNode, Territory to){
        // @requires 2 territories
        // @effects checks if one owners given territories linked.
        Player owner = startNode.getOwner();
        ArrayList<Territory> reachable = new ArrayList<>();
        LinkedList<Territory> queue = new LinkedList<>();
        queue.add(startNode);
        while (queue.size() > 0){
            Territory curTerritory = queue.pop();
            reachable.add(curTerritory);
            for (Territory t : curTerritory.getNeighbors()){
                if (t.isEnabled() && !reachable.contains(t) && t.getOwner()==owner) {
                    queue.add(t);
                }
            }
        }
        return reachable.contains(to);
    }



    @Override
    public String toString() {
        // @effects return a string of the map object
        String toReturn = "Map " + this.territories.size() + " {";
        for (Territory t : this.getTerritories()){
            if (t.getOwner()!= null){
                toReturn += t + "\n";
            }
        }
        toReturn += "}";
        return toReturn;
    }

    ////////////////////// TEST PURPOSE FUNCTIONS /////////////////////////
    public Territory getATerritoryWithoutOwner(){
        //@effects returns a territory without owner
        for (Territory t: this.territories){
            if (t.getOwner() == null){
                return t;
            }
        }
        return null;
    }

    public boolean repOk(int numOfPlayers) {
        // EFFECTS: Returns true if the rep invariant holds for this; otherwise returns false.
        Set<String> territoryNames = new HashSet<>();
        for (Territory t : territories) {
            if (t == null || !t.isEnabled() || territoryNames.contains(t.getName())) {
                return false;
            }
            territoryNames.add(t.getName());
        }
        // use controlMap to check the validity of the map
        return controlMap(numOfPlayers);
    }

}
