package domain.object;

import controller.TurnHandler;

import java.util.*;

import listener.*;

import static domain.object.TurnPhase.*;

public class Turn {

    private TurnPhase phase = null;
    private final Player player;
    private static final Game game = Game.getGame();
    private static Map map = Turn.game.getMap();
    private Attack attack = null;
    private int armyToDeploy = 0;
    private boolean didConquered;
    private final ArrayList<TurnListener> listeners = new ArrayList<>();
    private ChanceCard currentChanceCard = null;


    public Turn(Player p, int numOfPlayers, boolean isLoaded){
        this.player = p;
        if (isLoaded) {
            this.setArmyToDeploy(0);
        }
        else {
            this.setArmyToDeploy((40 - (numOfPlayers - 2) * 5));
        }
        new TurnHandler(this);
    }

    // SELECT AN INITIAL TERRITORY
    // UI uses this boolean to decide whether the territory selection was successful
    // maybe to show error message
    public boolean selectInitialTerritory(Territory territory){
        if (territory.isEnabled() && territory.getOwner()==null){
            territory.setOwner(this.player);
            territory.incrementNumOfArmy(1);
            this.decrementArmyToDeploy(1);
            this.controlPhase();
            Turn.game.nextTurn();
            return true;
        }
        return false;
    }

    // DEPLOY ONE ARMY TO YOUR TERRITORY
    // called in TERRITORYSEELCTION phase
    // UI checks for error message or not
    public boolean deployOneArmy(Territory t){
        if (this.player.getTerritories().contains(t)){
            t.incrementNumOfArmy(1);
            this.decrementArmyToDeploy(1);
            this.controlPhase();
            Turn.game.nextTurn();
            return true;
        }
        return false;
    }

    // USE CHANCE CARD
    // called in CHANCECARD phase
    // firs the chance card name, to be displayed
    // UI may use the returned chance card or use the fired chance card name to display
    // UI FIRE EKLE
    public ChanceCard pickAChanceCard(){
        System.out.println(map);
        if (this.currentChanceCard == null) {
            ChanceCard chanceCard = Turn.game.pickAChanceCard();
            this.currentChanceCard = chanceCard;
            String cardName = chanceCard.getName();
            String explanation = chanceCard.getExplanation();
            ArrayList<ChanceCardRequirement> requirements = chanceCard.getRequirements();
            this.fire(new Event("pickAChanceCard", cardName, explanation, requirements));
        }
        return this.currentChanceCard;
    }

    //////// FİREI DÜZELT UIDA
    public boolean useChanceCard(HashMap<ChanceCardRequirement, Object> requirements){
        ChanceCard chanceCard = this.currentChanceCard;
        boolean isSet = chanceCard.setRequirements(this, requirements);
        if (isSet) {
            chanceCard.use(this);
            this.currentChanceCard = null;
            this.controlPhase();
        }
        return isSet;
    }

    // to be called in ARMYDEPLOYMENT phase
    // UI checks the boolean to inform user whether the merge was successful or not
    public boolean mergeCards(ArrayList<Card> toMergeList){

        if( !this.phase.equals(ARMYDEPLOYMENT)) {
            this.fire(new Event("mergecard",String.format("Merge operation should be done in ARMYDEPLOYMENT, " +
                    "you are in %s phase", this.phase) ));
        }

        // check size
        if (toMergeList == null){
       this.fire(new Event("mergecard","No cards given for merge" ));
        return false;}


        boolean isArmyCard = false;
        boolean isTerritoryCard = false;

        // control card types: they should be the same!
        for(Card c : toMergeList){
            if (c instanceof ArmyCard) { isArmyCard = true; }
            else if (c instanceof TerritoryCard) { isTerritoryCard = true; }
            else {
            if (!isArmyCard || !isTerritoryCard){
                this.fire(new Event("mergecard","Cannot merge a TerritoryCard with an ArmyCard" ));
                return false;
            }}

        }

        if (isTerritoryCard){
            System.out.println(map);
            // control all are from same continent
            String continent = ((TerritoryCard) toMergeList.get(0)).getContinent();
            for (Card c : toMergeList){
                TerritoryCard tc = (TerritoryCard) c;
                if(!tc.getContinent().equals(continent)){
                    this.fire(new Event("mergecard","Given territory cards' continents must be the same" ));
                    return false;
                }

            }
            // control if all TerritoryCards of a given continent is provided
            map = Map.getMap();
            for (Territory t : Turn.map.getTerritories()){
                if (t.getContinent().equals(continent)){
                    if(!toMergeList.contains(TerritoryCard.getTerritoryCardFromTerritory(t))){
                        this.fire(new Event("mergecard","You should have all Cards of a continent" ));
                        return false;
                    }


                }
            }
            Turn.game.mergeTerritoryCards(this.player, toMergeList);
            this.fire(new Event("mergecard","Merged Successfully!" ));
            return true;
        }

        if (isArmyCard){
            // control size

            if((toMergeList.size() == 3)){
                this.fire(new Event("check 3","If you merge army cards you should provide 3 of them" ));
            }
            return Turn.game.mergeArmyCards(this.player, toMergeList);
        }
        return false;
    }

    // DEPLOY ARMY
    // UI uses this to inform the player
    public boolean deployMultipleArmies(Territory t, int numOfArmies){
        if (this.player.getTerritories().contains(t) && this.armyToDeploy >= numOfArmies){
            t.incrementNumOfArmy(numOfArmies);
            this.decrementArmyToDeploy(numOfArmies);
            this.controlPhase();
            return true;
        }
        return false;
    }

    // ATTACK
    // forth argument is only for type 3 otherwise null
    // UI inform
    public boolean constructAttack(Territory from, Territory to, int type, Integer n){

        boolean control1 = (from.getOwner() == this.getPlayer() && to.getOwner() != this.getPlayer());
        boolean control2 = from.getNeighbors().contains(to);
        boolean control3 = (from.getNumOfArmy() > to.getNumOfArmy());


        if (control1 && control2 && control3) {
            AttackOption attackOption = null;
            Attack curAttack;
            switch (type) {
                case 1 -> attackOption = new AttackOnce();
                case 2 -> attackOption = new AttackTillCant();
                case 3 -> attackOption = new AttackTillN(n);
            }
            curAttack = new Attack(attackOption, from, to);
            this.attack = curAttack;
            return true;
        }
        return false;
    }

    // returns true if a territory is conquered
    public boolean attack(){
        Player winner = this.attack.attack();
        boolean isConquered = attack.getTo().getNumOfArmy() == 0;
        if (isConquered){
            attack.getTo().setOwner(this.player);
            // attacker gains a territory / army card
            // handled at finishTurn, based on didConquered
            this.didConquered = true;
            // request attackFortifier FIRE FIRE FIRE !!!!!!!!!!!!!!!!!!!!!!!
            this.fire(new Event("attack", winner.getName(), winner.getName().equals(this.getPlayer().getName())));
            // if game is over return false, else return true
            return !game.isGameOver();
        }
        else{
            this.fire(new Event("attack", winner.getName(), winner.getName().equals(this.getPlayer().getName())));
            this.attack = null;
            return false;
        }
    }
    // UI inform user whether given a valid numOfArmy
    public boolean attackFortifier(int numOfArmy){
        Attack attack = this.attack;
        Territory from = attack.getFrom();
        Territory to = attack.getTo();
        if (from.getNumOfArmy() > numOfArmy){
            to.incrementNumOfArmy(numOfArmy);
            from.decrementNumOfArmy(numOfArmy);
            this.attack = null;
            // means that fortification is successful
            return true;
        }
        // means that invalid number of army is given
        return false;
    }

    // as attack can be done until the player no more want to, the phase change will be done
    // using a NextPhase button in UI
    // so unlike finishTurn for fortify, finishAttack will be called only from UI
    // not from attack itself
    public void finishAttack(){
        this.controlPhase();
    }


    // FORTIFY
    // UI informs the player whether the fortification was successful
    // if not keep accepting fortify info
    public boolean fortify(Territory from, Territory to, int numOfArmies){
        boolean control1 = (this.player == from.getOwner() && this.player == to.getOwner());
        boolean control2 = from.getNumOfArmy() > numOfArmies;
        boolean control3;
        if (control1 && control2) {
            control3 = Map.isLinkedWithSameOwner(from, to);
            if (control3){
                from.decrementNumOfArmy(numOfArmies);
                to.incrementNumOfArmy(numOfArmies);
                return true;
            }
        }
        return false;
    }

    // called directly from UI or called from UI using fortify
    // fires the picked card
    public void finishTurn(){
        if (this.didConquered){
            Card gainedCard = Turn.game.pickAnArmyOrTerritoryCard();
            this.fire(new Event("finishTurn", gainedCard.getName()));
            this.player.addCard(gainedCard);
            this.didConquered = false;
        }
        this.setPhase(VIEWMODE);
        Turn.game.nextTurn();
    }

    // no controller
    // Game uses it once that's why public
    // UI BU ÇOKOMELLİ
    public void setPhase(TurnPhase phase){
        this.phase = phase;
        this.fire(new Event("setPhase", phase));
    }

    private void setArmyToDeploy(int armyToDeploy){
        this.armyToDeploy = armyToDeploy;
        // inform user about how many armies he has to deploy
        this.fire(new Event("setArmyToDeploy", armyToDeploy));
    }
    // don't add controller
    // game obj calls it for merge cards
    public void incrementArmyToDeploy(int numOfArmyToInc){
        this.setArmyToDeploy(this.getArmyToDeploy() + numOfArmyToInc);
    }
    private void decrementArmyToDeploy(int numOfArmyToDec){
        this.setArmyToDeploy(this.getArmyToDeploy() - numOfArmyToDec);
    }

    public int getArmyToDeploy(){
        return this.armyToDeploy;
    }

    public Player getPlayer(){
        return this.player;
    }
    public ArrayList<Card> getArmyAndTerritoryCards(){
        return this.getPlayer().getArmyAndTerritoryCards();
    }


    // calculate number of new armies to deploy and set it
    public void start(){
        if (this.getPhase() == null){
            this.setPhase(TERRITORYSELECTION);
        }
        else if (Objects.requireNonNull(this.phase) == VIEWMODE) {
            this.incrementArmyToDeploy(Math.max((this.player.getTerritories().size() / 3), 3));
            this.setPhase(CHANCECARD);
        }
    }

    private void controlPhase(){
        switch(this.phase){
            case TERRITORYSELECTION -> Turn.game.controlTerritoryOwners();
            case SINGLEARMYDEPLOYMENT -> {
                if ((this.armyToDeploy == 0)) {
                    this.setPhase(VIEWMODE);
                }
            }
            case CHANCECARD -> this.setPhase(ARMYDEPLOYMENT);
            case ARMYDEPLOYMENT -> {
                if ((this.armyToDeploy == 0)) {
                    this.setPhase(ATTACK);
                }
            }
            case ATTACK -> this.setPhase(FORTIFY);
            // case FORTIFY -> this.setPhase(VIEWMODE);
        }
    }

    @Override
    public String toString() {
        return "Turn {" +
                "phase =" + phase +
                ", player =" + player +
                '}';
    }
    private void fire(Event e){
        for (TurnListener listener : this.listeners){
            listener.onTurnEvent(e);
        }
    }
    public void subscribe(TurnListener listener){
        this.listeners.add(listener);
    }

    ////////////////////// FOR TESTING PURPOSES ////////////////////////////

    public TurnPhase getPhase(){
        return this.phase;
    }
    public Territory getARandomTerritoryFromOwner(){
        Territory t;
        HashSet<Territory> terSet = this.getPlayer().getTerritories();
        ArrayList<Territory> terList = new ArrayList<>(terSet);
        Random rand = new Random(1234);
        return terList.get(rand.nextInt(terList.size()));
    }

    public Attack getAttack() {
        return attack;
    }
}
