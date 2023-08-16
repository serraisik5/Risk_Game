package tests;

import domain.object.BuildGame;
import domain.object.Map;
import domain.object.Player;
import domain.object.Territory;
import org.junit.jupiter.api.*;

import java.awt.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import domain.object.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class abstractMapTest {

    Map map ;

    BuildGame buildgame;

    @BeforeAll
    @DisplayName("Map Test Setup")
    void testSetup() {
        Territory.createAllTerritories("csv");

        buildgame = new BuildGame();

        buildgame.setNumberOfPlayers(3);
        buildgame.createPlayer("Berat",Color.black);
        buildgame.createPlayer("Serra",Color.blue);
        buildgame.createPlayer("Eren",Color.green);

        TreeMap<Integer, Player> tmap = new TreeMap<>();
        int i = 1;
        for (Player p : Player.getAllPlayers()){
            tmap.put(i, p);
            i++;
        }
        buildgame.createTurnList(tmap);
        Territory.findFromId(27).setOwner(buildgame.getTurnList().get(1));
        Territory.findFromId(28).setOwner(buildgame.getTurnList().get(1));
        Territory.findFromId(29).setOwner(buildgame.getTurnList().get(1));
        Territory.findFromId(30).setOwner(buildgame.getTurnList().get(2));
        buildgame.start();
        map = Map.getMap();

        System.out.println("Map Test is setup");
    }

    @Test
    void testGetTerritories(){
        // if finalized, this should not be true.
        assertEquals(Territory.getAllTerritories(), map.getTerritories(), "Disabled");
    }

    @Test
    void testDisable(){
        map.disableTerritory(Territory.findFromId(2));
        map.finalizeMap(buildgame.getNumOfPlayers());
        assertEquals(false, Territory.findFromId(2).isEnabled(), "Disabled");
    }


    @Test
    void testFinalizeMap(){
        assertTrue(map.finalizeMap(buildgame.getNumOfPlayers()), "Map created");
    }

    @Test
    void testTerritoriesOfContinent(){
        map.finalizeMap(buildgame.getNumOfPlayers());
        System.out.println(map);
        ArrayList<Territory> correct = new ArrayList<>();
        correct.add(Territory.findFromName("Indonesia"));
        correct.add(Territory.findFromName("New Guinea"));
        correct.add(Territory.findFromName("Western Australia"));
        correct.add(Territory.findFromName("Eastern Australia"));
        Set<Territory> set1 = new HashSet<>(correct);
        Set<Territory> set2 = new HashSet<>(map.getTerritoriesOfAContinent("AUSTRALIA"));

        assertEquals(set1,set2,"Correct territories" );
    }

   @AfterEach
    void validateMap() {
        buildgame.finalizeMap();
        assertTrue(map.repOk(buildgame.getNumOfPlayers()), "Map is invalid");

    }

}
