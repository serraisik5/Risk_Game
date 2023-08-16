package domain.object;

public class AttackTillCant extends AttackOption {

    @Override
    public Player attack(Territory from, Territory to){
        boolean canAttack = true;
        boolean isConquered = false;
        while (canAttack && !isConquered){
            super.baseAttack(from, to);
            isConquered = to.getNumOfArmy() == 0;
            canAttack = from.getNumOfArmy() > to.getNumOfArmy() + 1;
        }

        if (isConquered) {return from.getOwner();}
        else {return to.getOwner();}
    }

}
