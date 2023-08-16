package ui;

import controller.BuildGameHandler;
import controller.GameHandler;
import controller.TurnHandler;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GameStarts {

    private JFrame frame = MainScreen.frame;
    private GamePanel panel;
    private CustomImageButton pauseButton;
    private JButton saveButton;
    static ArrayList<CustomImageButton> territoryButtons;
    private BuildGameHandler buildGameHandler;






    public JPanel GameStarts(GameHandler gameHandler, BuildGameHandler buildgamehandler) {
        this.buildGameHandler = buildgamehandler;
        createPanel(gameHandler);
        createButtons(gameHandler);
        return panel;
    }


    private void createPanel(GameHandler gameHandler) {
        panel = new GamePanel(gameHandler, buildGameHandler) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundImage = new ImageIcon("src/resources/Action1.png");
                g.drawImage(backgroundImage.getImage(), 0, 0, frame.getWidth(), frame.getHeight(), null);
                //System.out.println(frame.getHeight());
            }
        };

        panel.setLayout(null);
        panel.setSize(new Dimension(frame.getWidth(), frame.getHeight()));




    }

    public void createButtons(GameHandler gamehandler){
        ImageIcon imageIcon1 = new ImageIcon("src/resources/pause.png");
        this.pauseButton = new CustomImageButton(imageIcon1);
        this.pauseButton.setBounds(10, 20, imageIcon1.getIconWidth(), imageIcon1.getIconHeight());
        // pauseButton.setText("Pause");
        // pauseButton.setBackground(new Color(139, 69, 19));
        // pauseButton.setForeground(Color.black);
        this.pauseButton.setOpaque(false);
        this.pauseButton.setContentAreaFilled(false);
        this.pauseButton.setBorderPainted(false);
        this.pauseButton.setText("");



        this.pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                gamehandler.pause();
                int result = JOptionPane.showOptionDialog(
                        null,
                        "Paused! Do you want to resume?",
                        "Paused",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[] {"Resume"},
                        "Resume");

                if(result==0) {
                    gamehandler.resume();

                }

            }
        });
        panel.add(pauseButton);

        ImageIcon imageIcon2 = new ImageIcon("src/resources/save.png");
        this.saveButton = new CustomImageButton(imageIcon2);
        this.saveButton.setBounds(1320, 20 , imageIcon1.getIconWidth(), imageIcon1.getIconHeight());
        this.saveButton.setText("Save");
        this.saveButton.setOpaque(false);
        this.saveButton.setContentAreaFilled(false);
        this.saveButton.setBorderPainted(false);
        this.saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gamehandler.save();
                System.exit(0);
            }
        });
        panel.add(saveButton);



        territoryButtons = MainScreen.getTerritoryButtons();
        for (CustomImageButton b : territoryButtons){
            panel.add(b);
        }
    }

    public static ArrayList<CustomImageButton> getTerritoryButtons() {
        return territoryButtons;
    }
}