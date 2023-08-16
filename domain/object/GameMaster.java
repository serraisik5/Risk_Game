package domain.object;

public class GameMaster {
    private Game game = Game.getGame();
    private static GameMaster master = null;


    // To be used with chance cards and game loading
    private GameMaster(){}

    public static GameMaster getMaster(){
        if (GameMaster.master == null){
            GameMaster.master = new GameMaster();
        }
        return GameMaster.master;
    }

    public void setTerritoryArmy(Territory t, int num){
        t.setNumOfArmy(num);
    }
    public void setTerritoryOwner(Territory t, Player newOwner){
        t.setOwner(newOwner);
    }
    public Integer roll(String explanation){
        return this.game.roll(explanation);
    }
    public void incrementTerritoryArmyNumber(Territory t, Integer num){
        t.incrementNumOfArmy(num);
    }
    public void decrementTerritoryArmyNumber(Territory t, Integer num){
        int curNumOfArmy = t.getNumOfArmy();
        if ((curNumOfArmy - 1 > num)) {
            t.decrementNumOfArmy(num);
        } else {
            t.decrementNumOfArmy(curNumOfArmy - 1);
        }
    }
    public void destroy(Territory t){
        int numOfArmy = t.getNumOfArmy();
        this.decrementTerritoryArmyNumber(t, numOfArmy - 1);
    }
    public Territory BFSPlayerTerritory(Territory start, Player reachTo){
        return this.game.getMap().BFSPlayerTerritory(start, reachTo);
    }





}
