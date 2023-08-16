package ui;

import com.sun.tools.javac.Main;
import controller.BuildGameHandler;
import controller.GameHandler;

import java.util.Map.Entry;
import controller.HandlerWrapper;
import controller.TurnHandler;
import listener.*;
import domain.object.*;
import listener.Event;


import javax.swing.*;
import javax.swing.border.Border;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import static domain.object.TurnPhase.VIEWMODE;

public class GamePanel extends JPanel implements GameListener {

    private final GameHandler gameHandler;

    private final HashMap<String, TurnPanel> panelsHashMap;
    private TurnPanel lastOpenPanel;

    private JPanel gameOverPanel;
    private PlayerInfoPanel curplayer;
    private CardsPanel cardspanel;
    private JLabel imagelabel;

    JLabel explosionLabel = new JLabel();
    private JPanel GameOverPanel = new JPanel();
    ImageIcon[] explosionImages = new ImageIcon[6];
    private HashMap<Territory,Territory> linkslist = new HashMap<>();







    public GamePanel(GameHandler gameHandler, BuildGameHandler buildgamehandler){

        this.gameHandler = gameHandler;
        this.gameHandler.subscribeToGame(this);
        this.panelsHashMap = new HashMap<>();
        this.gameOverPanel = new JPanel();
        ImageIcon ima= new ImageIcon("src/resources/border-design.png");
        imagelabel = new JLabel();
        imagelabel.setIcon(ima);
        imagelabel.setBounds(1245,110, ima.getIconWidth(),ima.getIconHeight());
        this.add(imagelabel);
        curplayer = new PlayerInfoPanel(ima);
        cardspanel = new CardsPanel();
        //curplayer.setBounds(1250, 0, ima.getIconWidth(), ima.getIconHeight());

        this.add(curplayer);
        this.add(gameOverPanel);
        this.curplayer.setPlayerInfo();
        this.add(cardspanel);
        cardspanel.updateCardsPanel();
        this.addBomb();
        gameOverPanel.setBounds(0,0,1440,812);
        gameOverPanel.setVisible(false);
        ImageIcon imagee= new ImageIcon("src/resources/End Game.png");
        JLabel EndGame = new JLabel();
        EndGame.setIcon(imagee);
        EndGame.setBounds(0,0, 1440,812);
        gameOverPanel.add(EndGame);
        linkslist = buildgamehandler.getLinks();
        LinksInfoPanel links = new LinksInfoPanel();
        this.add(links);



    }

    private void addBomb(){

        for (int i = 0; i < 6; i++) {
            explosionImages[i] = new ImageIcon("src/resources/bomb" + (i+1) + ".png");
        }

        explosionLabel.setSize(explosionImages[5].getIconWidth(),explosionImages[5].getIconHeight());
        this.add(explosionLabel);
    }
    public void playExplosionAnimation(int x, int y) {
        // Position the label at the given location
        //explosionLabel.setOpaque(true);
        //explosionLabel.setVisible(true);
        explosionLabel.setLocation(x, y);
        new Thread() {
            public void run() {
                int repeatCount = 3;  // Change this to increase/decrease the repeat count

                for (int repeat = 0; repeat < repeatCount; repeat++) {
                    for (int i = 0; i < explosionImages.length; i++) {
                        // Update the label with the next image
                        explosionLabel.setIcon(explosionImages[i]);

                        // Pause for a short time (e.g., 100ms)
                        try { Thread.sleep(100); } catch (InterruptedException e) {}
                    }
                }

                // Clear the explosion image when the animation is done
                explosionLabel.setIcon(null);
            }
        }.start();
    }


    /////////////////////// CONSTRUCTOR FUNCTIONS ////////////////////////////




    private void constructPanels(LinkedList<Turn> turnsList){
        for (Turn t: turnsList){
            // panel needs to save this HANDLER
            // also using this HANDLER subscribe to the related turn object (this.turnHandler.subscribe(this))
            TurnHandler curTurnHandler = (TurnHandler) HandlerWrapper.getHandler(String.format("%sTurnHandler",
                    t.getPlayer().getName()));
            TurnPanel curPanel = new TurnPanel(curTurnHandler, cardspanel, curplayer,this);
            if (MainScreen.isLoaded()){
                curTurnHandler.setPhase(VIEWMODE);
            }
            this.add(curPanel);
            this.panelsHashMap.put(t.getPlayer().getName(), curPanel);
            this.curplayer.setPlayerInfo();
            this.cardspanel.updateCardsPanel();
        }



    }


    private void constructMap(Map map){
        ArrayList<CustomImageButton> territoryButtons = MainScreen.getTerritoryButtons();

        ArrayList<Integer> numList = new ArrayList<>();
        for (int i = 1; i <= 42; i++) {
            numList.add(i);
        }

        // remove the idx of enabled territories (territories in the map)
        for (Territory t : map.getTerritories()){
            int id = t.getId();
            if (numList.contains(id)){
                numList.remove(Integer.valueOf(id));
            }
        }

        // disable the disabled territories
        for (int k : numList){
            territoryButtons.get(k-1).setEnabled(false);
        }


    }

    //////////////////////////////////////////////////////////////////////////

    ///////////////////// GAME RELATED FUNCTIONS /////////////////////////////


    private void changeTurn(String playerName){
        //JOptionPane.showMessageDialog(this, String.format("%s, it's your turn!", playerName));
        for (String name : this.panelsHashMap.keySet()){
            if (name.equals(playerName)){
                this.panelsHashMap.get(name).setVisible(true);
                this.setLastOpenPanel(this.panelsHashMap.get(name));



            }
            else{
                this.panelsHashMap.get(name).setVisible(false);

            }
        }
        this.curplayer.setPlayerInfo();
        this.cardspanel.updateCardsPanel();
    }

    private void pauseOrResume(boolean isPaused){
        if (isPaused){

            this.getLastOpenPanel().setVisible(false);

        }
        else{
            this.getLastOpenPanel().setVisible(true);

        }

    }


    private void gameOver(String winner){
        // GameOverPanela winner infosu gönder sonra visible yap
        this.getGameOverPanel().setVisible(true);
    }

    private void displaySingleArmyDeploymentStarts(){
        JOptionPane.showMessageDialog(GamePanel.this, "SINGLE ARMY DEPLOYMENT STARTS!");
    }


    /////////////////////////////////////////////////////////////////////////////////

    //////////////////////// GAME RELATED GETTER/SETTERS ////////////////////////////

    private TurnPanel getLastOpenPanel(){
        return this.lastOpenPanel;
    }
    private void setLastOpenPanel(TurnPanel lastOpenPanel){
        this.lastOpenPanel = lastOpenPanel;
    }


    private JPanel getGameOverPanel(){
        return this.gameOverPanel;
    }

    /////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onGameEvent(Event e){
        switch(e.getFunctionName()){
            case "setTurnList" -> {
                this.constructPanels(e.getTurnList());
            }
            case "setMap" -> this.constructMap(e.getMap());
            case "setTurn" -> {
                this.changeTurn(e.getNewString());

            }
            case "setPaused" -> this.pauseOrResume(e.getIsSuccessful());
            case "GameOver" -> {
                this.remove(curplayer);
                this.remove(cardspanel);
                this.remove(imagelabel);

                gameOverPanel.setVisible(true);
                JLabel win = new JLabel(e.getNewString());
                gameOverPanel.add(win);
                this.gameOver(e.getNewString());
            }
            case "controlTerritoryOwners" -> this.displaySingleArmyDeploymentStarts();
            case "roll" -> {
                this.lastOpenPanel.setDiceAnimationExplanation(e.getExplanation());
                this.lastOpenPanel.setDiceAnimationNumber(e.getNewInt());
            }
            case "merge army card" -> JOptionPane.showMessageDialog(this,
                    String.format("Merge Error: %s", e.getNewString()));
            default -> {
                assert false : "given function name undefined in GameFrame";
            }
        }
    }

    public class LinksInfoPanel extends JPanel{
        JLabel title;
        public LinksInfoPanel(){
            this.setBounds(20, 730, 1000, 70);
            this.setOpaque(false);
            this.setVisible(true);
            this.setBackground(Color.red);
            title = new JLabel(" Links added between: ");
            title.setFont(new Font("Serif", Font.BOLD, 16));
            title.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 2));
            add(title);
            //setLayout(new FlowLayout(FlowLayout.LEFT));
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            StringBuilder combinedString = new StringBuilder();

            int i = 0;
            for (Entry<Territory, Territory> entry : linkslist.entrySet()) {
                i++;
                String lll = entry.getKey().getName() + " and " + entry.getValue().getName();
                combinedString.append(lll);
                if (i<linkslist.size()){
                    combinedString.append(", ");
                }

                //JLabel linklabel = new JLabel(lll);
                //linklabel.setBorder(BorderFactory.createEmptyBorder(1, 5, 1, 5));
                //add(linklabel);

            }
            JLabel territoryLabel = new JLabel(combinedString.toString());
            territoryLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 2));
            add(territoryLabel);

        }
    }

    public class PlayerInfoPanel extends JPanel {
         JLabel playerNameLabel;
        JLabel caption;
         JLabel numOfarm;
        JLabel phaselabel;
         JLabel phaselabel2;
        JLabel order;


        ImageIcon ima;
        /**
         @Override
         protected void paintComponent(Graphics g) {
         super.paintComponent(g);
         if (ima != null) {
         Image image = ima.getImage();
         g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
         }
         }**/

        public PlayerInfoPanel(ImageIcon ima) {


            this.ima = ima;

            playerNameLabel = new JLabel("Player: ");
            playerNameLabel.setFont(playerNameLabel.getFont().deriveFont(Font.BOLD, 14f));
            playerNameLabel.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
            numOfarm = new JLabel("Army: ");
            numOfarm.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            phaselabel = new JLabel("Phase: ");
            phaselabel.setFont(playerNameLabel.getFont().deriveFont(Font.BOLD, 14f));
            phaselabel.setBorder(BorderFactory.createEmptyBorder(10, 5, 1, 5));
            phaselabel2 = new JLabel();
            phaselabel2.setFont(playerNameLabel.getFont().deriveFont(Font.BOLD, 14f));
            phaselabel2.setBorder(BorderFactory.createEmptyBorder(0, 5, 10, 5));
            order=new JLabel("Next Turn: ");
            order.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));


            this.setBounds(1255, 120, 150, 160);
            this.setOpaque(true);

            Border border = BorderFactory.createLineBorder(Color.darkGray, 3);
            this.setBorder(border);
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));


            if (gameHandler.getTurn()!= null){
                Color myco = gameHandler.getTurn().getPlayer().getColor();
                Color fadedColor = fadeColor(myco, 0.6f);
                this.setBackground(fadedColor);
                playerNameLabel.setText("Player: " + gameHandler.getTurn().getPlayer().getName());
                playerNameLabel.setOpaque(false);
                numOfarm.setText("Total Army: "+ gameHandler.getTurn().getArmyToDeploy());

                phaselabel2.setText(String.valueOf(gameHandler.getTurn().getPhase()));
                order.setText("Next Turn: "+gameHandler.getSecondTurn().getPlayer().getName());
            }
            // Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
            //playerNameLabel.setPreferredSize(new Dimension(190, 150));
            //playerNameLabel.setBorder(border);



            this.add(playerNameLabel);

            this.add(numOfarm);
            this.add(phaselabel);
            this.add(phaselabel2);
            this.add(order);
            this.setVisible(true);

        }
        public void setPlayerInfo() {
            if (gameHandler.getTurn()!= null){
                Color myco = gameHandler.getTurn().getPlayer().getColor();
                Color fadedColor = fadeColor(myco, 0.6f);
                this.setBackground(fadedColor);
            playerNameLabel.setText("Player: " + gameHandler.getTurn().getPlayer().getName());
            numOfarm.setText("Total Army: "+ gameHandler.getTurn().getArmyToDeploy());
            phaselabel2.setText(String.valueOf(gameHandler.getTurn().getPhase()));
            order.setText("Next Turn: "+gameHandler.getSecondTurn().getPlayer().getName());

            this.revalidate();
            this.repaint();}
        }
        public void updatePlayerInfo() {
            if (gameHandler.getTurn()!= null){
                Color myco = gameHandler.getTurn().getPlayer().getColor();
                Color fadedColor = fadeColor(myco, 0.6f);
                this.setBackground(fadedColor);
                numOfarm.setText("Total Army: "+ gameHandler.getTurn().getArmyToDeploy());
                phaselabel2.setText(String.valueOf(gameHandler.getTurn().getPhase()));
                this.revalidate();
                this.repaint();

            }
        }
        }

    private static Color fadeColor(Color color, float fadeLevel) {
        int alpha = Math.round(color.getAlpha() * fadeLevel);
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public class CardsPanel extends JPanel {
        private ArrayList<Card> selectedcards;
        private JLabel label ;
        private JList<Card> cardList;
        private DefaultListModel<Card> listModel;
        private JButton selectButton;
        JScrollPane listScroller;
        private ArrayList<Card> cards;

        private JLabel info ;
        public CardsPanel(){
            selectedcards =  new ArrayList<>();
            this.setBounds(1255, 300, 150, 190);
            this.setOpaque(true);
            Border border = BorderFactory.createLineBorder(Color.darkGray, 3);
            this.setBorder(border);
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            this.setBackground(Color.GRAY);



            cards = new ArrayList<>();

            if (gameHandler.getTurn()!= null) {
                cards = gameHandler.getTurn().getArmyAndTerritoryCards();
            }
            System.out.println(cards);
            label = new JLabel("My Cards");

            listModel = new DefaultListModel<>();
            listModel.addAll(cards);

            cardList = new JList<>(listModel);
            cardList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            cardList.setLayoutOrientation(JList.VERTICAL);
            cardList.setVisibleRowCount(-1);


            listScroller = new JScrollPane(cardList);
            listScroller.setPreferredSize(new Dimension(250, 80));

            selectButton = new JButton("Select");
            info = new JLabel("Press Cmd");

            selectButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedcards.clear();
                    java.util.List<Card> selectedCards = cardList.getSelectedValuesList();
                    System.out.println(selectedCards);
                    /**
                    if (selectedCards.size() > 3) {
                        JOptionPane.showMessageDialog(cardspanel,"You can only select 3 cards.");
                    } else {
                        for (Card card : selectedCards) {
                            selectedcards.add(card);
                        }
                    }**/
                    for (Card card : selectedCards) {
                        selectedcards.add(card);
                    }
                }
            });

            this.add(label);
            this.add(listScroller);
            this.add(info);
            this.add(selectButton);



        }
        public ArrayList<Card> getSelectedcards(){
            return this.selectedcards;
        }
        public void updateCardsPanel() {

            if (gameHandler.getTurn()!= null) {
                cards = gameHandler.getTurn().getArmyAndTerritoryCards();

                listModel.clear();
                listModel.addAll(cards);
                cardList.setModel(listModel);

                this.revalidate();
                this.repaint();

            }
        }




    }

}