package domain.object;
import storageservice.*;
import java.util.*;
import listener.*;

public class Territory {

    private final int id;
    private final String name;
    private final String continent;
    private boolean enabled = true;
    private int numOfArmy = 0;
    private Player owner;
    private final Set<Territory> neighbors = new HashSet<>();
    private static Set<Territory> allTerritories = new HashSet<>();
    private final ArrayList<TerritoryListener> listeners = new ArrayList<>();


    public Territory(String name, String continent, int id){
        this.name = name.replace("i", "ı").replace("İ","I").toUpperCase();
        this.continent = continent.replace("i", "ı").replace("İ","I").toUpperCase();
        this.id = id;
        Territory.allTerritories.add(this);
    }

    public String getName(){
        return this.name;
    }
    public String getContinent(){
        return this.continent;
    }

    public boolean isEnabled(){
        return this.enabled;
    }
    private void setStatus(boolean bool){
        this.enabled = bool;
        // UI territoryi karart ya da D yaz üstüne
        this.fire(new Event("setStatus", bool));
    }
    public void enable(){
        this.setStatus(true);
    }
    public void disable(){
        this.setStatus(false);
    }

    public int getNumOfArmy() {
        return numOfArmy;
    }
    public void setNumOfArmy(int numOfArmy) {
        this.numOfArmy = numOfArmy;
        // change the number of army appearing on map
        this.fire(new Event("setNumOfArmy", numOfArmy));
    }
    public void incrementNumOfArmy(int toIncrement) { this.setNumOfArmy(this.getNumOfArmy() + toIncrement); }
    public void decrementNumOfArmy(int toDecrement) { this.setNumOfArmy(this.getNumOfArmy() - toDecrement); }
    public void resetNeighbors() {
        Set<Territory> neighboursCopy = new HashSet<>(this.neighbors);
        for (Territory t : neighboursCopy){
            this.removeNeighbor(t);
        }
    }

    public Player getOwner() {
        return owner;
    }
    public void setOwner(Player owner) {
        if (this.owner != null && this.owner != owner) {

            this.owner.removeTerritory(this);
        }
        this.owner = owner;
        owner.addTerritory(this);
        // kişiye özgü renk varsa onu değiş vs (map)
        this.fire(new Event("setOwner", owner.getColor()));
    }

    public void addNeighbor(Territory toAdd){
        this.neighbors.add(toAdd);
        // construct a link between these two territory
        this.fire(new Event("addNeighbor", toAdd.getName(), this.getName()));
    }
    public void removeNeighbor(Territory toRemove){
        this.neighbors.remove(toRemove);
        // delete the link between these two
        this.fire(new Event("removeNeighbor", toRemove.getName(), this.getName()));
    }
    public Set<Territory> getNeighbors(){
        return new HashSet<>(this.neighbors);
    }
    public int getId(){
        return this.id;
    }

    public boolean isAdjacent(Territory territory){
        for (Territory ter : this.neighbors){
            if (ter == territory){return true;}
        }
        return false;
    }

    public static Set<Territory> getAllTerritories(){
        return new HashSet<>(Territory.allTerritories);
    }
    public static void createAllTerritories(String storageServiceAdapterName){
        IStorageServiceAdapter storageServiceAdapter = StorageServiceFactory.getStorageServiceFactory().
                getStorageServiceAdapter(storageServiceAdapterName);

        List<String[]>  territoryData = storageServiceAdapter.getTerritoryLines();
        int counter = 0;
        for (String[] line: territoryData){
            counter += 1;
            new Territory(line[0], line[1], counter);
        }

        List<String[]>  linkData = storageServiceAdapter.getAdjacentIndicesLines();
        for (String[] line: linkData){
            Territory curTerritory = Territory.findFromName(line[0]);
            for (int k = 1; k < line.length; k++){
                Territory adjTerritory = Territory.findFromId(Integer.parseInt(line[k]));
                assert curTerritory != null;
                curTerritory.addNeighbor(adjTerritory);
                assert adjTerritory != null;
                adjTerritory.addNeighbor(curTerritory);
            }
        }
    }

    public static Territory findFromId(int i){
        for (Territory t : Territory.allTerritories){
            if (t.getId() == i){
                return t;
            }
        }
        return null;
    }

    public static Territory findFromName(String name){
        for (Territory t : Territory.allTerritories){
            if (t.getName().equals(name.replace("i", "ı").replace("İ","I").toUpperCase())){
                return t;
            }
        }
        return null;
    }


    // contains methods uses this
    @Override
    public boolean equals(Object obj) {
        return this.name.equals(((Territory) obj).getName());
    }

     // HashSet add method uses this
    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }

    @Override
    public String toString() {
        return "Territory{" + "\n" +
                "id = " + id + '\n' +
                "name='" + name + '\n' +
                "owner=" + owner + "\n" +
                "numOfArmy=" + numOfArmy + "\n" +
                "continent= " + continent + "\n"+
                '}';
    }


    public void subscribe(TerritoryListener listener){
        this.listeners.add(listener);
    }
    private void fire(Event event){
        for(TerritoryListener l: this.listeners)
            l.onTerritoryEvent(event);
    }
}
