package ui;

import controller.BuildGameHandler;
import controller.GameHandler;
import domain.object.BuildGame;
import domain.object.Player;
import domain.object.Territory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class D_E_Territories {


    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;

    private JFrame frame = MainScreen.frame;
    private BuildGamePanel panel;
    private  BuildGameHandler buildGameHandler;




    public JPanel D_E_Territories(BuildGameHandler buildGameHandler) {
        this.buildGameHandler = buildGameHandler;
        createPanel(buildGameHandler);
        createButtons();
        return this.panel;
    }

    private void createPanel(BuildGameHandler buildGameHandler) {
        this.panel = new BuildGamePanel(buildGameHandler) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundImage = new ImageIcon("src/resources/EnableDisable.png");
                g.drawImage(backgroundImage.getImage(), 0, 0, frame.getWidth(), frame.getHeight(), null);
            }
        };
        this.panel.setLayout(null);
        this.panel.setSize(new Dimension(frame.getWidth(), frame.getHeight()));
    }

    private void changePanel(JPanel newPanel) {
        frame.getContentPane().removeAll();
        frame.add(newPanel);
        frame.revalidate();
        frame.repaint();
    }

    private void createButtons() {

        ImageIcon imageIconEnable = new ImageIcon("src/resources/Enable.png");
        button1 = new JButton(imageIconEnable);
        button1.setBounds(90, 700,  310, 84);

        ImageIcon imageIconDisable = new ImageIcon("src/resources/Disable.png");
        button2 = new JButton(imageIconDisable);
        button2.setBounds(455, 700,  imageIconDisable.getIconWidth(), imageIconDisable.getIconHeight());

        ImageIcon imageIconSetLink = new ImageIcon("src/resources/SetLink.png");
        button3 = new JButton(imageIconSetLink);
        button3.setBounds(867, 700,  imageIconSetLink.getIconWidth(), imageIconSetLink.getIconHeight());

        ImageIcon imageIconGo = new ImageIcon("src/resources/Go.png");

        button4 = new JButton(imageIconGo);
        button4.setBounds(1256, 649,  130, 126);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Integer temp = MainScreen.getLastTerritoryID();
                if(temp != null){
                    panel.getBuildGameHandler().enableTerritory( temp );
                }



            }
        });
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Integer temp = MainScreen.getLastTerritoryID();
                if(temp != null){
                    panel.getBuildGameHandler().disableTerritory( temp );
                }
            }
        });
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int t1 = MainScreen.getLastTerritoryID();
                int t2 = MainScreen.getLastSecondTerritoryID();

                panel.getBuildGameHandler().setLink(t1,t2 );
            }
        });
        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // finalize map // create turnlist // start
                if (D_E_Territories.this.panel.getBuildGameHandler().finalizeMap()){

                    GameStarts gameStarts = new GameStarts();
                    JPanel newPanel = gameStarts.GameStarts(MainScreen.gameHandler, panel.getBuildGameHandler());
                    changePanel(newPanel);

                    D_E_Territories.this.panel.getBuildGameHandler().start();
                }
                else{
                    JOptionPane.showMessageDialog(frame, "Something is wrong with the map!");
                }
            }
        });


        button4.setOpaque(false);
        button4.setContentAreaFilled(false);
        button4.setBorderPainted(false);


        ArrayList<CustomImageButton> territoryButtons = MainScreen.getTerritoryButtons();
        for (CustomImageButton b : territoryButtons){
            this.panel.add(b);
        }


        this.panel.add(button1);
        this.panel.add(button2);
        this.panel.add(button3);
        this.panel.add(button4);

    }



}
