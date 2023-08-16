package ui;


import controller.*;
import domain.object.*;
import controller.BuildGameHandler;
import controller.GameHandler;
import controller.MenuHandler;
import domain.object.Territory;


import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.Timer;

public class MainScreen {
    double width_multiplier;
    double height_multiplier;
    public static JFrame frame ;
    public static GameHandler gameHandler;
    public static BuildGameHandler buildGameHandler;
    public static MenuHandler menuHandler;
    public JPanel panel;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private static Integer lastTerritoryID = null;
    private static Integer lastSecondTerritoryID = null;
    private static Integer shouldfade1 = null;
    private static Integer shouldfade2 = null;
    private static ArrayList<CustomImageButton> shouldfades = new ArrayList<>();
    private static ArrayList<CustomImageButton> territoryButtons = new ArrayList<>();
    public static boolean loaded;

    public class MusicPlayer {

        private Clip clip;

        public void playMusic(String filePath) {
            try {
                File musicPath = new File(filePath);

                if(musicPath.exists()) {
                    AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                    clip = AudioSystem.getClip();
                    clip.open(audioInput);

                    clip.start();
                    clip.loop(Clip.LOOP_CONTINUOUSLY); // loop continuously
                } else {
                    System.out.println("Can't find file");
                }
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                ex.printStackTrace();
            }
        }

        public void stopMusic() {
            if (clip != null && clip.isRunning()) {
                clip.stop(); // stop the music
            }
        }
    }




    public MainScreen(MenuHandler menuHandler, BuildGameHandler buildGameHandler, GameHandler gameHandler) {
        MainScreen.menuHandler = menuHandler;
        MainScreen.gameHandler = gameHandler;
        MainScreen.buildGameHandler = buildGameHandler;
        this.panel = new MenuPanel(MainScreen.menuHandler, MainScreen.gameHandler) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundImage = new ImageIcon("src/resources/valence1.png");
                g.drawImage(backgroundImage.getImage(), 0, 0, panel.getWidth(), panel.getHeight(), null);
            }
        };
        createFrame();
        createPanel();
        createButtons();
        createTerritoryButtons();
        addComponents();

    }


    public static boolean isLoaded() {
        return MainScreen.loaded;
    }


    private void createFrame() {
        frame = new JFrame("Risk Game");
        // Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(1440,812);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    private void createPanel() {
        panel.setLayout(null);
        panel.setSize(new Dimension(frame.getWidth(), frame.getHeight()));
    }

    public void createButtons() {
        button1 = new JButton();
        button2 = new JButton();
        button3 = new JButton();
        button1.setIcon(new ImageIcon("src/resources/StartNewGame.png"));
        button2.setIcon(new ImageIcon("src/resources/Help.png"));
        button3.setIcon(new ImageIcon("src/resources/Load.png"));
        panel.add(button1);
        panel.add(button2);
        panel.add(button3);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BuildGameConstructor buildGameConstructor = new BuildGameConstructor();
                JPanel newPanel = buildGameConstructor.BuildGameConstructor(MainScreen.buildGameHandler);
                changePanel(newPanel);
            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HelpFrame newFrame = new HelpFrame();
                newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                newFrame.setTitle("Help");
                newFrame.setLocationRelativeTo(null);
                newFrame.setSize(1300, 800);
                newFrame.setVisible(true);
            }
        });
        

        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                MainScreen.loaded = true;
                GameStarts gameStarts = new GameStarts();
                JPanel newPanel = gameStarts.GameStarts(MainScreen.gameHandler,buildGameHandler);
                changePanel(newPanel);
                MainScreen.gameHandler.load();
                MainScreen.gameHandler.startNextTurn();
            }
        });


        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateButtonSizesAndPositions();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                updateButtonSizesAndPositions();
            }

            @Override
            public void componentShown(ComponentEvent e) {
                updateButtonSizesAndPositions();
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                updateButtonSizesAndPositions();
            }
        });

        // call updateButtonSizesAndPositions initially to set initial button sizes and positions
        updateButtonSizesAndPositions();
    }

    private void updateButtonSizesAndPositions() {

        width_multiplier = (double) frame.getWidth() / 1440;
        height_multiplier = (double) frame.getHeight() / 812;

        // update size and position of button1
        int button1X = 222;
        int button1Y = 621;
        int button1Width = 294;
        int button1Height = 92;
        button1.setBounds(button1X, button1Y, button1Width, button1Height);

        // update size and position of button2
        int button2X = 946;
        int button2Y = 621;
        int button2Width = 252;
        int button2Height = 92;
        button2.setBounds(button2X, button2Y, button2Width, button2Height);

        int button3X = 604;
        int button3Y = 631;
        int button3Width = 234;
        int button3Height = 77;
        button3.setBounds(button3X, button3Y, button3Width, button3Height);

    }

    private void addComponents() {
        frame.add(panel);
        frame.setVisible(true);
        MusicPlayer music = new MusicPlayer();
        music.playMusic("src/resources/Opening Credits _ Game of Thrones _ Season 8 (HBO).wav");
    }

    private void changePanel(JPanel newPanel) {
        frame.getContentPane().removeAll();
        frame.add(newPanel);
        frame.revalidate();
        frame.repaint();
    }


    ///////////////////////// STATIC FUNCTIONS //////////////////////////////
    public static Integer getLastTerritoryID(){
        Integer id = MainScreen.lastTerritoryID;
        MainScreen.lastTerritoryID = null;


        if (id != null){
            MainScreen.shouldfade1 = id;
            findTerritoryButton(shouldfade1).setSelected(false);
            if(MainScreen.lastSecondTerritoryID != null){
                findTerritoryButton(MainScreen.lastSecondTerritoryID).setSelected(false);
            }

        }


        return id;
    }
    public static Integer getLastSecondTerritoryID(){
        Integer id = MainScreen.lastSecondTerritoryID;
        MainScreen.lastSecondTerritoryID = null;
        if (id != null){
            MainScreen.shouldfade2 = id;
            findTerritoryButton(shouldfade2).setSelected(false);

        }
        return id;
    }
    public static void territoryClicked(Integer id){

        Integer lastId = MainScreen.getLastTerritoryID();
        findTerritoryButton(id).setSelected(true);
        for(CustomImageButton b : shouldfades){
            b.setSelected(false);
        }
        shouldfades.clear();

        if (lastId != null) {
            findTerritoryButton(lastId).setSelected(true);
            MainScreen.lastSecondTerritoryID = lastId;
            shouldfades.add(findTerritoryButton(lastId));
        }
        MainScreen.lastTerritoryID = id;


    }
    public static ArrayList<CustomImageButton> getTerritoryButtons(){
        return MainScreen.territoryButtons;
    }

    public static CustomImageButton findTerritoryButton(Integer id){
        return getTerritoryButtons().get(id-1);
    }



    //////////////////////////////////////////////////////////////////////////

    private void createTerritoryButtons(){
        // 42 tane buton oluşacak
        ImageIcon imageIcon1 = new ImageIcon("src/resources/Alaska.png");
        CustomImageButton customButton1 = new CustomImageButton(imageIcon1);

        ImageIcon imageIcon2 = new ImageIcon("src/resources/NWTerritory.png");
        CustomImageButton customButton2 = new CustomImageButton(imageIcon2);

        ImageIcon imageIcon3 = new ImageIcon("src/resources/Greenland.png");
        CustomImageButton customButton3 = new CustomImageButton(imageIcon3);

        ImageIcon imageIcon4 = new ImageIcon("src/resources/Alberta.png");
        CustomImageButton customButton4 = new CustomImageButton(imageIcon4);

        ImageIcon imageIcon5 = new ImageIcon("src/resources/Ontario.png");
        CustomImageButton customButton5 = new CustomImageButton(imageIcon5);

        ImageIcon imageIcon6 = new ImageIcon("src/resources/Quebec.png");
        CustomImageButton customButton6 = new CustomImageButton(imageIcon6);

        ImageIcon imageIcon7 = new ImageIcon("src/resources/Western US.png");
        CustomImageButton customButton7 = new CustomImageButton(imageIcon7);

        ImageIcon imageIcon8 = new ImageIcon("src/resources/Eastern US.png");
        CustomImageButton customButton8 = new CustomImageButton(imageIcon8);

        ImageIcon imageIcon9 = new ImageIcon("src/resources/Central America.png");
        CustomImageButton customButton9 = new CustomImageButton(imageIcon9);

        ImageIcon imageIcon10 = new ImageIcon("src/resources/Venezuela.png");
        CustomImageButton customButton10 = new CustomImageButton(imageIcon10);

        ImageIcon imageIcon11 = new ImageIcon("src/resources/Peru.png");
        CustomImageButton customButton11 = new CustomImageButton(imageIcon11);

        ImageIcon imageIcon12 = new ImageIcon("src/resources/Brazil.png");
        CustomImageButton customButton12 = new CustomImageButton(imageIcon12);

        ImageIcon imageIcon13 = new ImageIcon("src/resources/Argentina.png");
        CustomImageButton customButton13 = new CustomImageButton(imageIcon13);

        ImageIcon imageIcon14 = new ImageIcon("src/resources/North Africa.png");
        CustomImageButton customButton14 = new CustomImageButton(imageIcon14);

        ImageIcon imageIcon15 = new ImageIcon("src/resources/Egypt.png");
        CustomImageButton customButton15 = new CustomImageButton(imageIcon15);

        ImageIcon imageIcon16 = new ImageIcon("src/resources/East Africa.png");
        CustomImageButton customButton16 = new CustomImageButton(imageIcon16);

        ImageIcon imageIcon17 = new ImageIcon("src/resources/Congo.png");
        CustomImageButton customButton17 = new CustomImageButton(imageIcon17);

        ImageIcon imageIcon18 = new ImageIcon("src/resources/South Africa.png");
        CustomImageButton customButton18 = new CustomImageButton(imageIcon18);

        ImageIcon imageIcon19 = new ImageIcon("src/resources/Madagascar.png");
        CustomImageButton customButton19 = new CustomImageButton(imageIcon19);

        ImageIcon imageIcon20 = new ImageIcon("src/resources/Iceland.png");
        CustomImageButton customButton20 = new CustomImageButton(imageIcon20);

        ImageIcon imageIcon21 = new ImageIcon("src/resources/Scandinavia.png");
        CustomImageButton customButton21 = new CustomImageButton(imageIcon21);

        ImageIcon imageIcon22 = new ImageIcon("src/resources/Ukraine.png");
        CustomImageButton customButton22 = new CustomImageButton(imageIcon22);

        ImageIcon imageIcon23 = new ImageIcon("src/resources/Great Britain.png");
        CustomImageButton customButton23 = new CustomImageButton(imageIcon23);

        ImageIcon imageIcon24 = new ImageIcon("src/resources/Northern Europe.png");
        CustomImageButton customButton24 = new CustomImageButton(imageIcon24);

        ImageIcon imageIcon25 = new ImageIcon("src/resources/Southern Europe.png");
        CustomImageButton customButton25 = new CustomImageButton(imageIcon25);

        ImageIcon imageIcon26 = new ImageIcon("src/resources/Western Europe.png");
        CustomImageButton customButton26 = new CustomImageButton(imageIcon26);

        ImageIcon imageIcon27 = new ImageIcon("src/resources/Indonesia.png");
        CustomImageButton customButton27 = new CustomImageButton(imageIcon27);

        ImageIcon imageIcon28 = new ImageIcon("src/resources/New Guinea.png");
        CustomImageButton customButton28 = new CustomImageButton(imageIcon28);

        ImageIcon imageIcon29 = new ImageIcon("src/resources/Western Australia.png");
        CustomImageButton customButton29 = new CustomImageButton(imageIcon29);

        ImageIcon imageIcon30 = new ImageIcon("src/resources/Eastern Australia.png");
        CustomImageButton customButton30 = new CustomImageButton(imageIcon30);

        ImageIcon imageIcon31 = new ImageIcon("src/resources/Slam.png");
        CustomImageButton customButton31 = new CustomImageButton(imageIcon31);

        ImageIcon imageIcon32 = new ImageIcon("src/resources/India.png");
        CustomImageButton customButton32 = new CustomImageButton(imageIcon32);

        ImageIcon imageIcon33 = new ImageIcon("src/resources/China.png");
        CustomImageButton customButton33 = new CustomImageButton(imageIcon33);

        ImageIcon imageIcon34 = new ImageIcon("src/resources/Mongolia.png");
        CustomImageButton customButton34 = new CustomImageButton(imageIcon34);

        ImageIcon imageIcon35 = new ImageIcon("src/resources/Japan.png");
        CustomImageButton customButton35 = new CustomImageButton(imageIcon35);

        ImageIcon imageIcon36 = new ImageIcon("src/resources/Irkutsk.png");
        CustomImageButton customButton36 = new CustomImageButton(imageIcon36);

        ImageIcon imageIcon37 = new ImageIcon("src/resources/Yakutsk.png");
        CustomImageButton customButton37 = new CustomImageButton(imageIcon37);

        ImageIcon imageIcon38 = new ImageIcon("src/resources/Kamchatka.png");
        CustomImageButton customButton38 = new CustomImageButton(imageIcon38);

        ImageIcon imageIcon39 = new ImageIcon("src/resources/Siberia.png");
        CustomImageButton customButton39 = new CustomImageButton(imageIcon39);

        ImageIcon imageIcon40 = new ImageIcon("src/resources/Afghanistan.png");
        CustomImageButton customButton40 = new CustomImageButton(imageIcon40);

        ImageIcon imageIcon41 = new ImageIcon("src/resources/Ural.png");
        CustomImageButton customButton41 = new CustomImageButton(imageIcon41);

        ImageIcon imageIcon42 = new ImageIcon("src/resources/Middle East.png");
        CustomImageButton customButton42 = new CustomImageButton(imageIcon42);

        customButton1.setBounds(226, 115, imageIcon1.getIconWidth(), imageIcon1.getIconHeight());
        opaque(customButton1);
        customButton2.setBounds(303, 99, imageIcon2.getIconWidth(), imageIcon2.getIconHeight());
        opaque(customButton2);
        customButton3.setBounds(480, 71, imageIcon3.getIconWidth(), imageIcon3.getIconHeight());
        opaque(customButton3);
        customButton4.setBounds(311, 170, imageIcon4.getIconWidth(), imageIcon4.getIconHeight());
        opaque(customButton4);
        customButton5.setBounds(390, 170, imageIcon5.getIconWidth(), imageIcon5.getIconHeight());
        opaque(customButton5);
        customButton6.setBounds(450, 166, imageIcon6.getIconWidth(), imageIcon6.getIconHeight());
        opaque(customButton6);
        customButton7.setBounds(314, 233, imageIcon7.getIconWidth(), imageIcon7.getIconHeight());
        opaque(customButton7);
        customButton8.setBounds(373, 233, imageIcon8.getIconWidth(), imageIcon8.getIconHeight());
        opaque(customButton8);
        customButton9.setBounds(322, 298, imageIcon9.getIconWidth(), imageIcon9.getIconHeight());
        opaque(customButton9);
        customButton10.setBounds(394, 370, imageIcon10.getIconWidth(), imageIcon10.getIconHeight());
        opaque(customButton10);
        customButton11.setBounds(382, 416, imageIcon11.getIconWidth(), imageIcon11.getIconHeight());
        opaque(customButton11);
        customButton12.setBounds(415, 402, imageIcon12.getIconWidth(), imageIcon12.getIconHeight());
        opaque(customButton12);
        customButton13.setBounds(425, 495, imageIcon13.getIconWidth(), imageIcon13.getIconHeight());
        opaque(customButton13);
        customButton14.setBounds(605, 360, imageIcon14.getIconWidth(), imageIcon14.getIconHeight());
        opaque(customButton14);
        customButton15.setBounds(705, 385, imageIcon15.getIconWidth(), imageIcon15.getIconHeight());
        opaque(customButton15);
        customButton16.setBounds(750, 430, imageIcon16.getIconWidth(), imageIcon16.getIconHeight());
        opaque(customButton16);
        customButton17.setBounds(700, 470, imageIcon17.getIconWidth(), imageIcon17.getIconHeight());
        opaque(customButton17);
        customButton18.setBounds(710, 529, imageIcon18.getIconWidth(), imageIcon18.getIconHeight());
        opaque(customButton18);
        customButton19.setBounds(824, 550, imageIcon19.getIconWidth(), imageIcon19.getIconHeight());
        opaque(customButton19);
        customButton20.setBounds(605, 160, imageIcon20.getIconWidth(), imageIcon20.getIconHeight());
        opaque(customButton20);
        customButton21.setBounds(672, 130, imageIcon21.getIconWidth(), imageIcon21.getIconHeight());
        opaque(customButton21);
        customButton22.setBounds(733, 121, imageIcon22.getIconWidth(), imageIcon22.getIconHeight());
        opaque(customButton22);
        customButton23.setBounds(564, 201, imageIcon23.getIconWidth(), imageIcon23.getIconHeight());
        opaque(customButton23);
        customButton24.setBounds(663, 215, imageIcon24.getIconWidth(), imageIcon24.getIconHeight());
        opaque(customButton24);
        customButton25.setBounds(670, 275, imageIcon25.getIconWidth(), imageIcon25.getIconHeight());
        opaque(customButton25);
        customButton26.setBounds(595, 275, imageIcon26.getIconWidth(), imageIcon26.getIconHeight());
        opaque(customButton26);
        customButton27.setBounds(980, 455, imageIcon27.getIconWidth(), imageIcon27.getIconHeight());
        opaque(customButton27);
        customButton28.setBounds(1086, 442, imageIcon28.getIconWidth(), imageIcon28.getIconHeight());
        opaque(customButton28);
        customButton29.setBounds(1021, 506, imageIcon29.getIconWidth(), imageIcon29.getIconHeight());
        opaque(customButton29);
        customButton30.setBounds(1098, 506, imageIcon30.getIconWidth(), imageIcon30.getIconHeight());
        opaque(customButton30);
        customButton31.setBounds(991, 360, imageIcon31.getIconWidth(), imageIcon31.getIconHeight());
        opaque(customButton31);
        customButton32.setBounds(888, 314, imageIcon32.getIconWidth(), imageIcon32.getIconHeight());
        opaque(customButton32);
        customButton33.setBounds(923, 246, imageIcon33.getIconWidth(), imageIcon33.getIconHeight());
        opaque(customButton33);
        customButton34.setBounds(984, 218, imageIcon34.getIconWidth(), imageIcon34.getIconHeight());
        opaque(customButton34);
        customButton35.setBounds(1102, 218, imageIcon35.getIconWidth(), imageIcon35.getIconHeight());
        opaque(customButton35);
        customButton36.setBounds(973, 165, imageIcon36.getIconWidth(), imageIcon36.getIconHeight());
        opaque(customButton36);
        customButton37.setBounds(992, 90, imageIcon37.getIconWidth(), imageIcon37.getIconHeight());
        opaque(customButton37);
        customButton39.setBounds(897, 70, imageIcon39.getIconWidth(), imageIcon39.getIconHeight());
        opaque(customButton39);
        customButton38.setBounds(1046, 102, imageIcon38.getIconWidth(), imageIcon38.getIconHeight());
        opaque(customButton38);
        customButton40.setBounds(828, 229, imageIcon40.getIconWidth(), imageIcon40.getIconHeight());
        opaque(customButton40);
        customButton41.setBounds(872, 105, imageIcon41.getIconWidth(), imageIcon41.getIconHeight());
        opaque(customButton41);
        customButton42.setBounds(745, 320, imageIcon42.getIconWidth(), imageIcon42.getIconHeight());
        opaque(customButton42);


        MainScreen.getTerritoryButtons().add(customButton1);
        MainScreen.getTerritoryButtons().add(customButton2);
        MainScreen.getTerritoryButtons().add(customButton3);
        MainScreen.getTerritoryButtons().add(customButton4);
        MainScreen.getTerritoryButtons().add(customButton5);
        MainScreen.getTerritoryButtons().add(customButton6);
        MainScreen.getTerritoryButtons().add(customButton7);
        MainScreen.getTerritoryButtons().add(customButton8);
        MainScreen.getTerritoryButtons().add(customButton9);
        MainScreen.getTerritoryButtons().add(customButton10);
        MainScreen.getTerritoryButtons().add(customButton11);
        MainScreen.getTerritoryButtons().add(customButton12);
        MainScreen.getTerritoryButtons().add(customButton13);
        MainScreen.getTerritoryButtons().add(customButton14);
        MainScreen.getTerritoryButtons().add(customButton15);
        MainScreen.getTerritoryButtons().add(customButton16);
        MainScreen.getTerritoryButtons().add(customButton17);
        MainScreen.getTerritoryButtons().add(customButton18);
        MainScreen.getTerritoryButtons().add(customButton19);
        MainScreen.getTerritoryButtons().add(customButton20);
        MainScreen.getTerritoryButtons().add(customButton21);
        MainScreen.getTerritoryButtons().add(customButton22);
        MainScreen.getTerritoryButtons().add(customButton23);
        MainScreen.getTerritoryButtons().add(customButton24);
        MainScreen.getTerritoryButtons().add(customButton25);
        MainScreen.getTerritoryButtons().add(customButton26);
        MainScreen.getTerritoryButtons().add(customButton27);
        MainScreen.getTerritoryButtons().add(customButton28);
        MainScreen.getTerritoryButtons().add(customButton29);
        MainScreen.getTerritoryButtons().add(customButton30);
        MainScreen.getTerritoryButtons().add(customButton31);
        MainScreen.getTerritoryButtons().add(customButton32);
        MainScreen.getTerritoryButtons().add(customButton33);
        MainScreen.getTerritoryButtons().add(customButton34);
        MainScreen.getTerritoryButtons().add(customButton35);
        MainScreen.getTerritoryButtons().add(customButton36);
        MainScreen.getTerritoryButtons().add(customButton37);
        MainScreen.getTerritoryButtons().add(customButton38);
        MainScreen.getTerritoryButtons().add(customButton39);
        MainScreen.getTerritoryButtons().add(customButton40);
        MainScreen.getTerritoryButtons().add(customButton41);
        MainScreen.getTerritoryButtons().add(customButton42);

        for (int idx = 0 ; idx < MainScreen.getTerritoryButtons().size(); idx++){

            CustomImageButton territoryButton = MainScreen.getTerritoryButtons().get(idx);
            Territory relatedTerritory = Territory.findFromId(idx + 1);
            relatedTerritory.subscribe(territoryButton);
            int sameIdx = idx;
            territoryButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    MainScreen.territoryClicked(sameIdx + 1);
                }
            });
        }

    }
    private void opaque(CustomImageButton customButton) {
        customButton.setOpaque(false);
        customButton.setContentAreaFilled(false);
        customButton.setBorderPainted(false);
    }

}



