package domain.object;

public enum ArmyCardType{
    INFANTRY(1), CAVALRY(5), ARTILLERY(10);
    private final int value;
    private ArmyCardType(int value){
        this.value = value;
    }
    public int getValue(){
        return this.value;
    }

    public static ArmyCardType getTypeFromName(String name){
        ArmyCardType type;
        switch (name){
            case "Infantry" -> type = INFANTRY;
            case "Cavalry" -> type = CAVALRY;
            case "Artillery" -> type = ARTILLERY;
            default -> type = null;
        }
        return type;
    }

}
