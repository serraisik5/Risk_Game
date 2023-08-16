package controller;

import domain.object.*;
import listener.*;

import java.util.HashMap;

public class GameHandler implements Handler {

    private Game game;

    public GameHandler(Game game){
        HandlerWrapper.addHandler("GameHandler", this);
        this.game = game;
    }
    public Game getGame(){
        return Game.getGame();
    }
    public Map getGameMap(){
        return game.getMap();
    }
    public void startNextTurn(){
        game.nextTurn();
    }
    public String gameToString() {
        return game.toString();
    }
    public void pause() { this.game.pause(); }
    public void resume() { this.game.resume(); }
    public void subscribeToGame(GameListener listener){
        this.game.subscribe(listener);
    }
    public void save() { this.game.save(); }
    public void load() {this.game.load();}
    public Turn getTurn(){
        return this.game.getTurn();
    }
    public Turn getSecondTurn(){
        return this.game.getSecondTurn();
    }


    /////////////////// FOR TEST PURPOSES //////////////////////////////
    public String getTurnOwnerName(){
        return this.game.getTurnOwnerName();
    }
}
