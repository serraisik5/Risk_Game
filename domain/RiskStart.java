package domain;

import controller.BuildGameHandler;
import controller.GameHandler;
import controller.MenuHandler;
import domain.object.*;
import ui.MainScreen;

import java.util.ArrayList;

public class RiskStart {

    public static void main(String[] args) {

        Territory.createAllTerritories("csv");

        ArrayList<ChanceCardStrategy> chanceCardStrategyList = new ArrayList<>();
        chanceCardStrategyList.add(new DoNothingChanceStrategy());
        chanceCardStrategyList.add(new CoupChanceStrategy());
        chanceCardStrategyList.add(new ReinforcementChanceStrategy());
        chanceCardStrategyList.add(new SabotageChanceStrategy());
        chanceCardStrategyList.add(new RevoltChanceStrategy());
        chanceCardStrategyList.add(new NuclearStrikeChanceStrategy());

        ChanceCard.createChanceCards(chanceCardStrategyList);

        domain.object.Menu menu = new Menu();
        MenuHandler menuHandler = new MenuHandler(menu);

        BuildGame buildGame = new BuildGame();
        BuildGameHandler buildGameHandler = new BuildGameHandler(buildGame);

        Game game = Game.getGame();
        GameHandler gameHandler = new GameHandler(game);

        new MainScreen(menuHandler, buildGameHandler, gameHandler);
    }
}