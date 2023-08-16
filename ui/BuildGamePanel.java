package ui;

import listener.Event;
import listener.MenuListener;
import controller.BuildGameHandler;

import javax.swing.*;

// bu frame hem BuilGamelistenerı dinliyor hemde Menulistenerı
// ikisinde de değişim olduğunda bir şey yapacak.
public class BuildGamePanel extends JPanel implements MenuListener {

    private final BuildGameHandler buildGameHandler;
    
    public BuildGamePanel(BuildGameHandler buildGameHandler){
    	
        this.buildGameHandler = buildGameHandler;

        add(new JButton("aButton"));

    }

    public BuildGameHandler getBuildGameHandler(){
        return this.buildGameHandler;
    }

    @Override
    public void onMenuEvent(Event event) {
    	// menude starta bastığımda, build game framei açılsın
        if (event.getFunctionName().equals("start")) setVisible(true);
    }
}
