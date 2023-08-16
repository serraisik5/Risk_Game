package domain.object;

import java.util.*;
import listener.*;
import storageservice.IStorageServiceAdapter;
import storageservice.StorageServiceFactory;

import static domain.object.ArmyCardType.*;
import static domain.object.TurnPhase.SINGLEARMYDEPLOYMENT;

public class Game {
    private LinkedList<Turn> turnList;
    private LinkedList<Turn> originalTurnList;
    private ArrayList<Card> armyAndTerritoryCards;
    private ArrayList<Card> usedArmyAndTerritoryCards = new ArrayList<>();
    private ArrayList<ChanceCard> chanceCards;
    private Map map;
    private Turn turn;
    private boolean isPaused = false;
    private boolean isGameOver = false;
    private Player winner;
    private static Game game;
    private ArrayList<GameListener> listeners = new ArrayList<>();


    private Game(){
        Game.game = this;
    }

    public static Game getGame(){
        if (Game.game == null){
            new Game();
        }
        return Game.game;
    }

    // called initially
    // UI considers the Event only if the panels are not created
    // once the panels are created, no need for considering it
    // UI subscribes each JPanel with related turn object
    public void setTurnList(LinkedList<Player> turnList, boolean isLoaded) {
        int numOfPlayer = turnList.size();
        LinkedList<Turn> t = new LinkedList<>();
        for (Player p : turnList){
            t.add(new Turn(p, numOfPlayer, isLoaded));
        }
        this.turnList = t;
        this.originalTurnList = new LinkedList<>(this.turnList);
        this.fire(new Event("setTurnList", new LinkedList<>(this.turnList)));
    }

    public void setArmyAndTerritoryCards(ArrayList<Card> armyAndTerritoryCards) {
        this.armyAndTerritoryCards = armyAndTerritoryCards;
    }

    public void setChanceCards(ArrayList<ChanceCard> chanceCards) {
        this.chanceCards = chanceCards;
    }


    // UI subscribe to these territories
    public void setMap(Map map) {
        this.map = map;
        this.fire(new Event("setMap", map));
    }

    // UI looks at the name and searches the controller with this name and
    // set this as current turn controller
    // set others not visible
    private void setTurn(Turn t){
        this.turn = t;
        this.fire(new Event("setTurn", t.getPlayer().getName()));
    }

    private void setPaused(boolean paused){
        this.isPaused = paused;
        this.fire(new Event("setPaused", paused));
    }

    public void pause(){
        this.setPaused(true);
    }
    public void resume(){
        this.setPaused(false);
    }


    private void checkGameOver(){
        if (this.turnList.size() == 1){
            this.GameOver(this.turnList.get(0).getPlayer());
        }
    }
    private void GameOver(Player winner){
        this.isGameOver = true;
        this.setWinner(winner);
        this.fire(new Event("GameOver", winner.getName()));
    }

    public boolean isGameOver(){
        return this.isGameOver;
    }

    // already fired in GameOver
    private void setWinner(Player winner){
        this.winner = winner;
        System.out.printf("%s won the game!", winner.getName());
    }


    public Map getMap(){
        return this.map;
    }


    // already fired in setTurn
    public void nextTurn(){
        Turn newTurn = this.turnList.pop();
        this.turnList.add(newTurn);
        newTurn.start();
        this.setTurn(newTurn);
    }

    // no controller needed
    public void playerDied(Player deadPlayer){
        // remove from turn list
        for (int i = 0; i < this.turnList.size(); i++){
            Turn t = this.turnList.get(i);
            if (t.getPlayer() == deadPlayer){
                this.turnList.remove(i);
                break;
            }
        }
        // remove from original turn list
        for (int k = 0; k < this.originalTurnList.size(); k++){
            Turn t = this.originalTurnList.get(k);
            if (t.getPlayer() == deadPlayer){
                this.originalTurnList.remove(k);
                break;
            }
        }
        this.checkGameOver();
    }

    public void controlTerritoryOwners(){
        if (!map.isLeftWithoutOwner()){
            for (Turn t : this.turnList){
                t.setPhase(SINGLEARMYDEPLOYMENT);
            }
            // UI informs that OneArmyDeployment starting
            this.fire(new Event("controlTerritoryOwners"));
        }
    }

    // already fired in Turn finish turn
    public Card pickAnArmyOrTerritoryCard(){
        Random random = new Random();

        // if no cards left in the deck, shuffle the used cards and set it as new deck
        // set usedArmyAndTerritoryCards to empty list;
        if (this.armyAndTerritoryCards.size() == 0){
            Collections.shuffle(this.usedArmyAndTerritoryCards);
            this.setArmyAndTerritoryCards(new ArrayList<>(this.usedArmyAndTerritoryCards));
            this.usedArmyAndTerritoryCards = new ArrayList<>();
        }

        int idx = random.nextInt(this.armyAndTerritoryCards.size());
        Card cardToReturn = this.armyAndTerritoryCards.get(idx);
        this.armyAndTerritoryCards.remove(cardToReturn);
        return cardToReturn;
    }

    // already fired in useChanceCard
    public ChanceCard pickAChanceCard(){
        Random random = new Random();
        int idx = random.nextInt(this.chanceCards.size());
        return this.chanceCards.get(idx);
    }

    // to be fired in Turn
    public void mergeTerritoryCards(Player p, ArrayList<Card> cardList){
        String continent = ((TerritoryCard)cardList.get(0)).getContinent();
        ArrayList<Territory> territoriesOfContinent = this.map.getTerritoriesOfAContinent(continent);
        for (Territory t : territoriesOfContinent){
            if (t.getOwner() != p){
                t.setNumOfArmy(1);
            }
            // handles both player side and territory side
            t.setOwner(p);
        }
        for (Card c : cardList){
            this.usedArmyAndTerritoryCards.add(c);
            p.removeCard(c);
        }
    }

    // to be fired in Turn
    public boolean mergeArmyCards(Player p, ArrayList<Card> cardList){
        boolean mergedSuccessfully = true;
        HashMap<ArmyCardType, Integer> table = ArmyCard.extractArmyCardTypes(cardList);
        String ruleId = String.valueOf(table.get(INFANTRY)*100 +
                table.get(ArmyCardType.CAVALRY)*10 +
                table.get(ArmyCardType.ARTILLERY));
        switch (ruleId){
            case "300" -> this.turn.incrementArmyToDeploy(ArmyCardType.CAVALRY.getValue());
            case "210" -> this.turn.incrementArmyToDeploy(2*ArmyCardType.CAVALRY.getValue());
            case "201" -> this.turn.incrementArmyToDeploy(2*ArmyCardType.ARTILLERY.getValue());
            case "120" -> this.turn.incrementArmyToDeploy(ArmyCardType.CAVALRY.getValue() + ArmyCardType.ARTILLERY.getValue());
            case "21" -> this.turn.incrementArmyToDeploy(3*ArmyCardType.ARTILLERY.getValue());
            default -> {
                mergedSuccessfully = false;
                this.fire(new Event("merge army cards",String.format("invalid combination %s", ruleId) ));
                //assert false : String.format("invalid combination %s", ruleId);
            }
        }
        if (mergedSuccessfully){
            for (Card c : cardList){
                this.usedArmyAndTerritoryCards.add(c);
                p.removeCard(c);
            }
            return true;
        }
        return false;
    }


    // UI FİRE EKLE
    public Integer roll(String purpose){
        Random random = new Random();
        int randomValue = random.nextInt(6 ) + 1;
        this.fire(new Event("roll", purpose, randomValue));
        return randomValue;
    }

    @Override
    public String toString() {
        return "Game: {" +
                "Turn List=" + originalTurnList + "\n" +
                ", map =" + map + "\n"+
                ", current player =" + turn +
                '}';
    }

    private void fire(Event e){
        for (GameListener listener : this.listeners){
            listener.onGameEvent(e);
        }
    }
    public void subscribe(GameListener listener){
        this.listeners.add(listener);
    }

    public void save(){
        IStorageServiceAdapter storageService = StorageServiceFactory.getStorageServiceFactory()
                .getStorageServiceAdapter("csv");
        Set<Territory> allTerritories = Territory.getAllTerritories();
        storageService.resetSavedFiles();
        storageService.saveTerritories(allTerritories);

        LinkedList<Player> playerList = new LinkedList<>();
        for (Turn turn : this.turnList){
            playerList.add(turn.getPlayer());
        }
        storageService.savePlayers(playerList);
        storageService.saveArmyAndTerritoryCards(this.armyAndTerritoryCards);
    }

    public void load(){
        IStorageServiceAdapter storageServiceAdapter = StorageServiceFactory.getStorageServiceFactory()
                .getStorageServiceAdapter("csv");
        List<String[]> savedPlayers = storageServiceAdapter.loadPlayers();
        List<String[]> savedTerritories = storageServiceAdapter.loadTerritories();
        List<String[]> savedNeighbors = storageServiceAdapter.loadNeighbors();
        List<String[]> savedATCards = storageServiceAdapter.loadArmyAndTerritoryCards();

        // create players
        HashMap<Integer, Player> idPlayerMap = new HashMap<>();
        LinkedList<Player> turnList = new LinkedList<>();
        for (String[] curP : savedPlayers){
            Integer id = Integer.valueOf(curP[0]);
            Player p = new Player(curP[1], curP[2]);
            p.setPlayerNum(id);
            idPlayerMap.put(id, p);
            turnList.add(p);
        }

        // handle player deck and game deck
        ArrayList<Card> gameATDeck = new ArrayList<>();
        for (String[] card : savedATCards){
            Integer p_id = Integer.valueOf(card[0]);
            String name = card[1];
            String type = card[2];

            Card curCard;
            switch (type){
                case "0" -> {
                    Territory t = Territory.findFromName(name);
                    assert t != null;
                    curCard = new TerritoryCard(t.getName(), t);
                }
                case "1" -> {
                    ArmyCardType theType = ArmyCardType.getTypeFromName(name);
                    curCard = new ArmyCard(theType);
                }
                default -> curCard = null;
            } assert curCard != null;

            if (p_id.equals(0)) { gameATDeck.add(curCard); }
            else { idPlayerMap.get(p_id).addCard(curCard); }
        }

        // handle territory army and owner info and players territory set
        for (String[] terInfo : savedTerritories){
            int terId = Integer.parseInt(terInfo[0]);
            boolean isEnabled = terInfo[1].equals("1");
            int numOfArmy = Integer.parseInt(terInfo[2]);
            Territory t = Territory.findFromId(terId);
            // if territory disabled, owner is "nan". gives error. we added continue to handle this.
            if (isEnabled) {
                t.enable();
            } else {
                t.disable();
                continue;
            }

            Integer pId = Integer.valueOf(terInfo[3]);
            Player p = idPlayerMap.get(pId);

            t.setNumOfArmy(numOfArmy);
            t.setOwner(p);

            p.addTerritory(t);
        }

        // set the links for territories
        for (String[] terLink : savedNeighbors){
            int terId = Integer.parseInt(terLink[0]);
            Territory t = Territory.findFromId(terId);
            assert t != null;
            t.resetNeighbors();
            for (int i = 1; i < terLink.length; i++){
                int curTerId = Integer.parseInt(terLink[i]);
                Territory curTer = Territory.findFromId(curTerId);
                t.addNeighbor(curTer);
            }
        }

        // finalize the map
        Set<Territory> enabledTerritories = new HashSet<>();
        for (Territory t : Territory.getAllTerritories()){
            if (t.isEnabled()){
                enabledTerritories.add(t);
            }
        }
        Map.getMap().finalizeMapMaster(enabledTerritories);


        this.setTurnList(turnList, true);
        this.setMap(Map.getMap());
        this.setArmyAndTerritoryCards(gameATDeck);
        this.setChanceCards(ChanceCard.getAllChanceCards());
    }


    /////////////////// FOR TEST PURPOSES //////////////////////////////
    public String getTurnOwnerName(){
        return this.turn.getPlayer().getName();
    }

    public ArrayList<Card> getArmyAndTerritoryCards() {
        return this.armyAndTerritoryCards;
    }

    ///////// For tests /////////////7
    public Turn getTurn() {
        return turn;
    }
    public  Turn getSecondTurn(){ return turnList.get(0);}
}
