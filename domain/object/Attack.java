package domain.object;

public class Attack {

    private final AttackOption strategy;
    private final Territory from;
    private final Territory to;

    public Attack(AttackOption strategy, Territory from, Territory to){
        this.strategy = strategy;
        this.from = from;
        this.to = to;
    }

    public Player attack(){
        Player winner = strategy.attack(this.from, this.to);
        return winner;
    }

    public Territory getTo(){
        return this.to;
    }

    public Territory getFrom(){
        return this.from;
    }


}
