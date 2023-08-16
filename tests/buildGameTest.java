package tests;

import domain.object.BuildGame;
import domain.object.Player;
import domain.object.Territory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.awt.*;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import domain.object.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class buildGameTest {
    BuildGame buildgame;
    Player player;



    @BeforeAll
    @DisplayName("build Game Test Setup")
    void testSetup() {
        buildgame = new BuildGame();
        Territory.createAllTerritories("csv");
        player = new Player("Eren",Color.magenta) ;
        System.out.println("Build Game Test is setup");
    }

    @Test
    void testNumberOfPlayers() {
        //glass
        buildgame.setNumberOfPlayers(5);
        assertEquals(5, buildgame.getNumOfPlayers(), "Size must be 2");
    }
    @Test
    void testCreatePlayer() {
        // black
        buildgame.setNumberOfPlayers(4);
        buildgame.createPlayer("Serra", Color.blue);
        buildgame.createPlayer("Fatih", Color.green);
        assertEquals(3, buildgame.getAllPlayers().size(), "2 players created");
    }

    @Test
    void testEnableTerritory() {

        buildgame.enableTerritory(Territory.findFromId(2));
        assertEquals(true, Territory.findFromId(2).isEnabled(), "territory enabled");
    }

    @Test
    void testSetLink() {
        Territory t1 = Territory.findFromId(1);
        Territory t2 = Territory.findFromId(5);
        buildgame.setLink(t1,t2);
        assertEquals(true,t1.isAdjacent(t2) , "set link");
    }

    @Test
    void testTurnList(){
        //glass
        Player p2 = new Player("Berat",Color.black);
        Player p3 = new Player("Serra",Color.blue);
        TreeMap<Integer, Player> map = new TreeMap<>();
        map.put(3,player);
        map.put(2,p2);
        map.put(1,p3);
        buildgame.createTurnList(map);
        assertEquals(p3,buildgame.getTurnList().getFirst() , "turn list created");
    }





}
