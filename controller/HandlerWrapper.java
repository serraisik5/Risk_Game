package controller;

import java.util.*;

public class HandlerWrapper {
    private Map<String, Handler> handlers;
    private static HandlerWrapper WRAPPER;

    private HandlerWrapper(){
        this.handlers = new HashMap<>();
    }

    private Map<String, Handler> getHandlerHashMap(){
        return this.handlers;
    }

    public static void addHandler(String name, Handler handler){
        if (HandlerWrapper.WRAPPER == null) HandlerWrapper.WRAPPER = new HandlerWrapper();
        HandlerWrapper.WRAPPER.getHandlerHashMap().put(name, handler);
    }

    public static Handler getHandler(String name){
        if (HandlerWrapper.WRAPPER == null) HandlerWrapper.WRAPPER = new HandlerWrapper();
        return HandlerWrapper.WRAPPER.getHandlerHashMap().getOrDefault(name, null);
    }




}
