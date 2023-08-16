package domain.object;

public class AttackOnce extends AttackOption {

    @Override
    public Player attack(Territory from, Territory to){
        return super.baseAttack(from, to);
    }

}
