package controller;

import domain.object.Menu;

public class MenuHandler implements Handler{

    private Menu menu;

    public MenuHandler(Menu menu){
        HandlerWrapper.addHandler("MenuHandler", this);
        this.menu = menu;
    }

    public void start(){
        menu.start();
    }

    public String getHelp(){
        return menu.getHelpMessage();
    }


    /*

    public void subscribe(MenuListener listener){
        this.menu.subscribe(listener);
    }

     */

}
