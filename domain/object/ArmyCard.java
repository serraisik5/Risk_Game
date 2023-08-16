package domain.object;

import java.util.ArrayList;
import java.util.HashMap;

public class ArmyCard extends Card{

    private ArmyCardType type;
    private static final ArrayList<Card> allArmyCards = new ArrayList<>();

    public ArmyCard(ArmyCardType type){
        this.type = type;
        switch (type) {
            case INFANTRY -> this.name = "Infantry";
            case CAVALRY -> this.name = "Cavalry";
            case ARTILLERY -> this.name = "Artillery";
        }
        ArmyCard.allArmyCards.add(this);
    }

    public static void createArmyCards(int numOfPlayers){
        for (int i = 0; i < numOfPlayers; i++){
            new ArmyCard(ArmyCardType.INFANTRY);
            new ArmyCard(ArmyCardType.INFANTRY);
            new ArmyCard(ArmyCardType.INFANTRY);

            new ArmyCard(ArmyCardType.CAVALRY);
            new ArmyCard(ArmyCardType.CAVALRY);

            new ArmyCard(ArmyCardType.ARTILLERY);
        }
    }

    public ArmyCardType getType(){
        return this.type;
    }

    public static HashMap<ArmyCardType, Integer> extractArmyCardTypes(ArrayList<Card> cardList){
        HashMap<ArmyCardType, Integer> table = new HashMap<>();
        table.put(ArmyCardType.INFANTRY, 0);
        table.put(ArmyCardType.CAVALRY, 0);
        table.put(ArmyCardType.ARTILLERY, 0);
        for (Card c : cardList){
            ArmyCard ac = (ArmyCard) c;
            switch (ac.getType()){
                case INFANTRY -> table.put(ArmyCardType.INFANTRY, 1 + table.get(ArmyCardType.INFANTRY));
                case CAVALRY -> table.put(ArmyCardType.CAVALRY, 1 + table.get(ArmyCardType.CAVALRY));
                case ARTILLERY -> table.put(ArmyCardType.ARTILLERY, 1 + table.get(ArmyCardType.ARTILLERY));
            }
        }
        return table;
    }

    @Override
    public String toString() {
        return "ArmyCard "  + name +  " ";
    }

    public static ArrayList<Card> getAllArmyCards(){
        return new ArrayList<>(ArmyCard.allArmyCards);
    }





}
