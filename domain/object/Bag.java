package domain.object;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Bag {

    private final ArrayList<Integer> bag = new ArrayList<>(Arrays.asList(1,2,3,4,5,6));
    private static Bag theBag = null;

    private Bag(){
        Bag.theBag = this;
    }

    public static int pickNumber(){
        if (Bag.theBag == null){
            new Bag();
        }
        Random rand = new Random();
        int index = rand.nextInt(Bag.theBag.bag.size());
        return Bag.theBag.bag.remove(index);
    }

}
