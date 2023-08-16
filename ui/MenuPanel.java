package ui;

import controller.GameHandler;
import controller.MenuHandler;
import listener.Event;
import listener.MenuListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPanel extends JPanel implements MenuListener {

    private final JButton startButton;
    private final JButton loadButton;
    private final JButton helpButton;
    private MenuHandler menuHandler;

    public MenuPanel(MenuHandler menuHandler, GameHandler gameHandler){

        this.menuHandler = menuHandler;
        // this.menuHandler.subscribe(this);
        this.startButton = new JButton("START");
        this.loadButton = new JButton("LOAD");
        this.helpButton = new JButton("HELP");

        startButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                menuHandler.start();
                // aslında menu 'de start methodunda yapacaklarım oluyor
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameHandler.load();
            }
        });

        helpButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String msg = menuHandler.getHelp();

                HelpFrame newFrame = new HelpFrame();
                newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                newFrame.setTitle("Help");
                newFrame.setSize(800, 700);
                newFrame.setVisible(true);
            }
        });

        add(startButton);
        add(helpButton);
    }

    @Override
    public void onMenuEvent(Event event){
        // menu de starta bastığımda, menu penceresi kapansın,
        // buildgame frame açılacak
        if (event.getFunctionName().equals("start")) setVisible(false);
        // help içinde yapabilirdik ama
        //optionpane çıkardığımız için kimsenin bundan haberi olmasına gerek yok
    }


}
