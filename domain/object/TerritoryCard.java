package domain.object;

import java.util.HashSet;
import java.util.Set;

public class TerritoryCard extends Card{
    private Territory territory;
    private static Set<TerritoryCard> allTerritoryCards = new HashSet<>();

    public TerritoryCard(String name, Territory territory){
        this.name = name.replace("i", "ı").replace("İ","I").toUpperCase();
        this.territory = territory;
        TerritoryCard.allTerritoryCards.add(this);
    }

    // use this after initializing the territories
    public static void createTerritoryCards(Set<Territory> territories){
        for (Territory ter : territories){
            new TerritoryCard(ter.getName(), ter);
        }
    }

    public String getContinent(){
        return this.getTerritory().getContinent();
    }

    public Territory getTerritory(){
        return this.territory;
    }

    public static Set<TerritoryCard> getAllTerritoryCards(){
        return new HashSet<TerritoryCard>(TerritoryCard.allTerritoryCards);

    }

    @Override
    public String toString() {
        return "TerritoryCard: " + name +" ";
    }

    public static TerritoryCard getTerritoryCardFromTerritory(Territory t){
        for (TerritoryCard tc: TerritoryCard.getAllTerritoryCards()){
            if (tc.getTerritory() == t){
                return tc;
            }
        }
        return null;
    }
}
