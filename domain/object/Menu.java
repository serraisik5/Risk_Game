package domain.object;

import listener.Event;
import listener.MenuListener;

import java.util.ArrayList;

public class Menu {

    private final String helpMessage = "THIS IS THE RISK";
    //when there is change in menu, all listeners will hear it and do smth
    private final ArrayList<MenuListener> listeners;

    public Menu(){
        this.listeners = new ArrayList<>();
    }

    public String getHelpMessage(){
        return helpMessage;
    }

    public void start(){
        // when press to start, start event fired and
        //listeners will do what we want when there is change
        this.fire(new Event("start", null, null));
    }

    public void subscribe(MenuListener listener){
        this.listeners.add(listener);
    }

    private void fire(Event event){
        // for each listener, do what we want when there is change
        for(MenuListener l: this.listeners)
            l.onMenuEvent(event);
        // help e basınca da bir event oluşturabilirdik fakat
        //değişmesini istediğimiz bir şey yok
    }



}

