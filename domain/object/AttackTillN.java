package domain.object;

public class AttackTillN extends AttackOption {

    // n being the number of army attacker is ready to loose
    private final int n;
    private int attackerLoss = 0;
    public AttackTillN(int n){
        this.n = n;
    }

    @Override
    public Player attack(Territory from, Territory to){
        boolean canAttack = true;
        boolean isConquered = false;
        boolean maxLossAchieved = false;
        int attackerLoss = 0;
        while (canAttack && !isConquered && !maxLossAchieved){
            if (super.baseAttack(from, to) == to.getOwner()) { attackerLoss += 2; };
            isConquered = to.getNumOfArmy() == 0;
            canAttack = from.getNumOfArmy() > to.getNumOfArmy() + 1;
            maxLossAchieved = attackerLoss >= this.n;
        }

        this.attackerLoss = attackerLoss;
        if (isConquered) { return from.getOwner(); }
        else { return to.getOwner(); }
    }

    public int getAttackerLoss(){
        return this.attackerLoss;
    }


}
