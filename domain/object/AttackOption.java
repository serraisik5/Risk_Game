package domain.object;

import java.util.Random;

public abstract class AttackOption {

    protected Player baseAttack(Territory from, Territory to){
        Random rand = new Random();
        Player winner;
        int attackerNum = rand.nextInt(6) + 1;
        int defenderNum = rand.nextInt(6) + 1;

        if (attackerNum > defenderNum){
            to.decrementNumOfArmy(1);
            winner = from.getOwner();
        }
        else{
            from.decrementNumOfArmy(2);
            winner = to.getOwner();
        }
        return winner;
    }

    // returns the winner
    public abstract Player attack(Territory from, Territory to);

}
