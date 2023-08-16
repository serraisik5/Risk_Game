package ui;

import controller.TurnHandler;
import domain.object.*;
import listener.Event;
import listener.TurnListener;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.text.NumberFormatter;

import controller.TurnHandler;
import listener.Event;
import listener.TurnListener;
import domain.object.ChanceCard;
import domain.object.TurnPhase;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;


///////////////////////////// SET ARMY TO DEPLOY UNUTMA ////////////////////////////
public class TurnPanel extends JPanel implements TurnListener{

    private TurnHandler turnHandler;

    private final JButton initialTerritorySelect;

    private final JButton singleDeploySelect;


    private final JButton pickchanceCard;
    private final JLabel showchanceCard;
    private final JButton usechanceCard;
    private JLabel labelForExp;

    private final JFormattedTextField multipleDeployNumber;

    private final JButton multipleDeploySelect;

    private final JButton mergeCards;

    private final JButton attackSelect;

    private final JRadioButton attackOption1;
    private final JRadioButton attackOption2;
    private final JRadioButton attackOption3;

    //private final JFormattedTextField attackLoseNumber;

    private final JButton attackFinish;
    private final JButton fortifySelect;
    private final JFormattedTextField fortifyNumber;
    private final JButton fortifyFinish;
    private final JPanel panel1;
    private final JPanel panel2;
    private final JPanel panel3;
    private final JPanel panel4;
    private final JPanel panel5;
    private final JPanel panel6;
    private JLabel label;
    private int attackOption;
    private int attackNumber;
    private JLabel numOfArmyLabel;
    public ArrayList<ChanceCardRequirement> currentChanceRequirements = null;
    private String diceAnimationExplanation = null;
    private int diceAnimationNumber = 0;
    private GamePanel.PlayerInfoPanel playerInfoPanel;
    private GamePanel gamePanel;
    int from;
    int to;
    private ArrayList<CustomImageButton> territorybuttons;



    NumberFormat format = NumberFormat.getInstance();
    NumberFormatter formatter = new NumberFormatter(format) {
        @Override
        public Object stringToValue(String string) throws ParseException {
            if (string.isEmpty() || string.trim().isEmpty()) {
                return null; // or return some default value
            } else {
                return super.stringToValue(string);
            }
        }
    };

    public void flashLabel(JPanel panel, JLabel label, int flashDelay, int flashDuration) {
        new Timer(flashDelay , new ActionListener() {
            long startTime = System.currentTimeMillis();
            boolean isLabelVisible = true;

            @Override
            public void actionPerformed(ActionEvent e) {
                // Check if flashDuration have passed
                if(System.currentTimeMillis() - startTime > flashDuration){
                    ((Timer)e.getSource()).stop(); // Stop the timer
                    panel.remove(label); // Remove the label from the panel
                    panel.revalidate();
                    panel.repaint();
                    return;
                }

                // Flash the label
                isLabelVisible = !isLabelVisible;
                label.setVisible(isLabelVisible);
            }
        }).start();
    }





    public TurnPanel(TurnHandler turnHandler, GamePanel.CardsPanel cardsPanel, GamePanel.PlayerInfoPanel playerInfoPanel, GamePanel gamepanel) {
        this.setVisible(false);
        this.playerInfoPanel = playerInfoPanel;
        this.gamePanel = gamepanel;
        this.territorybuttons = GameStarts.getTerritoryButtons();
        //cardsPanel.setVisible(false);

        NumberFormat longFormat = NumberFormat.getIntegerInstance();
        NumberFormatter numberFormatter = new NumberFormatter(longFormat);
        numberFormatter.setValueClass(Long.class);
        numberFormatter.setAllowsInvalid(false);
        numberFormatter.setMinimum(0l);

        this.setLayout(null);
        this.setBounds(0, 0, 1440, 812);
        this.setOpaque(false);

        this.turnHandler = turnHandler;
        this.turnHandler.subscribe(this);



        panel1 = new JPanel();
        panel1.setBounds(0, 0, 1440, 812);
        panel1.setLayout(null);
        panel1.setOpaque(false);
        this.add(panel1);
        ImageIcon imageIconCapture = new ImageIcon("src/resources/Capture.png");
        initialTerritorySelect = new JButton(imageIconCapture);
        initialTerritorySelect.setBounds(620, 680, 219, 61);
        panel1.add(initialTerritorySelect);

        panel2 = new JPanel();
        panel2.setBounds(0, 0, 1440, 812);
        panel2.setOpaque(false);
        panel2.setLayout(null);
        this.add(panel2);
        ImageIcon imageIconRise= new ImageIcon("src/resources/Rise.png");
        singleDeploySelect = new JButton(imageIconRise);
        singleDeploySelect.setBounds(533, 680, 384, 61);
        panel2.add(singleDeploySelect);

        panel3 = new JPanel();
        panel3.setBounds(0, 0, 1440, 812);
        panel3.setOpaque(false);
        panel3.setLayout(null);
        this.add(panel3);
        ImageIcon imageIconDestiny = new ImageIcon("src/resources/Destiny Cards.png");
        pickchanceCard = new JButton(imageIconDestiny);
        ImageIcon imageIconUseDestiny = new ImageIcon("src/resources/Use Destiny Cards.png");
        usechanceCard = new JButton(imageIconUseDestiny);
        pickchanceCard.setBounds(15,120,190, 301);
        usechanceCard.setBounds(68,435,88,69);
        ImageIcon imageIconshowDestiny = new ImageIcon("src/resources/show Destiny Cards.png");
        showchanceCard = new JLabel(imageIconshowDestiny);
        showchanceCard.setBounds(15,120,190, 301);

        usechanceCard.setEnabled(false);
        pickchanceCard.setEnabled(true);
        panel3.add(pickchanceCard);
        panel3.add(usechanceCard);

        panel4 = new JPanel();
        panel4.setBounds(0, 0, 1440, 812);
        panel4.setOpaque(false);
        panel4.setLayout(null);

        JLabel numOfArmyLabel = new JLabel();
        this.numOfArmyLabel = numOfArmyLabel;
        numOfArmyLabel.setForeground(Color.WHITE);
        numOfArmyLabel.setBounds(798, 720, 70, 30);
        panel4.add(numOfArmyLabel);
        this.add(panel4);

        ImageIcon imageIconMerge= new ImageIcon("src/resources/Merge Cards.png");
        mergeCards = new JButton(imageIconMerge);
        mergeCards.setBounds(1258, 500, 145, 69);
        panel4.add(mergeCards);

        multipleDeployNumber = new JFormattedTextField(formatter);
        multipleDeployNumber.setBounds(778, 680, 100, 40);
        panel4.add(multipleDeployNumber);

        ImageIcon imageDeploy= new ImageIcon("src/resources/Deploy.png");
        multipleDeploySelect = new JButton(imageDeploy);
        multipleDeploySelect.setBounds(587, 670, 163, 65);
        panel4.add(multipleDeploySelect);

        panel5 = new JPanel();
        panel5.setBounds(0, 0, 1440, 812);
        panel5.setOpaque(false);
        panel5.setLayout(null);
        this.add(panel5);

        ImageIcon imageAttack= new ImageIcon("src/resources/Attack.png");
        attackSelect = new JButton(imageAttack);
        attackSelect.setBounds(646, 668, 233, 61);
        panel5.add(attackSelect);

        ImageIcon imageAttackOption= new ImageIcon("src/resources/Attack Options.png");
        JLabel imageAttackOptionLabel = new JLabel(imageAttackOption);
        imageAttackOptionLabel.setBounds(245, 668, 381, 62);
        panel5.add(imageAttackOptionLabel);

        ImageIcon image1= new ImageIcon("src/resources/1.png");
        attackOption1 = new JRadioButton(image1);
        attackOption1.setBounds(273, 728, 47, 54);
        panel5.add(attackOption1);

        ImageIcon image2= new ImageIcon("src/resources/2.png");
        attackOption2 = new JRadioButton(image2);
        attackOption2.setBounds(412, 728, 50, 54);
        panel5.add(attackOption2);

        ImageIcon image3= new ImageIcon("src/resources/3.png");
        attackOption3 = new JRadioButton(image3);
        attackOption3.setBounds(552, 728, 50, 54);
        panel5.add(attackOption3);

        ImageIcon imageEndattack= new ImageIcon("src/resources/End Attack.png");
        attackFinish = new JButton(imageEndattack);
        attackFinish.setBounds(899, 668, 285, 61);
        panel5.add(attackFinish);

        attackOption1.addActionListener(e -> {
            attackOption2.setSelected(false);
            attackOption3.setSelected(false);
            attackOption = 1;
        });
        attackOption2.addActionListener(e -> {
            attackOption1.setSelected(false);
            attackOption3.setSelected(false);
            attackOption = 2;
        });
        attackOption3.addActionListener(e -> {
            attackOption2.setSelected(false);
            attackOption1.setSelected(false);
            attackOption = 3;
        });
        mergeCards.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {

                ArrayList<Card> tomerged = cardsPanel.getSelectedcards();
                System.out.println("in turn panel");
                System.out.println(tomerged);
                turnHandler.mergeCards(tomerged);

                cardsPanel.updateCardsPanel();


            }
        });

        panel6 = new JPanel();
        panel6.setBounds(0, 0, 1440, 812);
        panel6.setOpaque(false);
        panel6.setLayout(null);
        this.add(panel6);


        ImageIcon imageFortify= new ImageIcon("src/resources/Fortify.png");
        fortifySelect = new JButton(imageFortify);
        fortifySelect.setBounds(587, 680, 201, 61);
        panel6.add(fortifySelect);

        fortifyNumber = new JFormattedTextField(formatter);
        fortifyNumber.setValue(1);
        fortifyNumber.setBounds(798, 690, 100, 40);
        panel6.add(fortifyNumber);

        ImageIcon imageEndFortfiy= new ImageIcon("src/resources/End Turn.png");
        fortifyFinish = new JButton(imageEndFortfiy);
        fortifyFinish.setBounds(1250, 690, imageEndFortfiy.getIconWidth(), imageEndFortfiy.getIconHeight());
        panel6.add(fortifyFinish);

        ImageIcon ima= new ImageIcon("src/resources/square.png");

        initialTerritorySelect.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(turnHandler.selectInitialTerritory(MainScreen.getLastTerritoryID())){
                    //yap ve go next
                }
                else {
                    JLabel label = new JLabel("Wrong Territory Selection");
                    label.setFont(new Font("Arial", Font.ITALIC, 40));
                    label.setForeground(Color.RED);
                    label.setBounds(500,0,600,90);
                    panel1.add(label);

                    //---------------- Animation for wrong territory selection ----------------//
                    flashLabel(panel1, label, 500, 5000);
                    //--------------------------------------------------------------------------//
                }
            }
        });

        singleDeploySelect.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(turnHandler.deployOneArmy(MainScreen.getLastTerritoryID())){
                    //yap ve go next
                }
                else {
                    JLabel label = new JLabel("That's not your territory!");
                    label.setFont(new Font("Arial", Font.ITALIC, 40));
                    label.setForeground(Color.RED);
                    label.setBounds(500,0,600,90);
                    panel2.add(label);

                    //---------------- Animation for wrong territory selection ----------------//
                    flashLabel(panel2, label, 500, 5000);
                    //--------------------------------------------------------------------------//
                }
            }
        });
        pickchanceCard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (TurnPanel.this.currentChanceRequirements == null) {
                    turnHandler.pickAChanceCard();
                    usechanceCard.setEnabled(true);
                    pickchanceCard.setEnabled(false);
                    panel3.add(showchanceCard);
                    panel3.remove(pickchanceCard);
                    pickchanceCard.setEnabled(true);

                    panel3.revalidate();
                    panel3.repaint();

                }
            }});
        usechanceCard.addActionListener(new ActionListener() {
            boolean flag = true;
            @Override
            public void actionPerformed(ActionEvent e) {

                if (TurnPanel.this.currentChanceRequirements != null){
                    HashMap<ChanceCardRequirement, Object> reqs = new HashMap<>();
                    for (ChanceCardRequirement req : TurnPanel.this.currentChanceRequirements) {

                        switch (req) {
                            // we will need to check whether they are null or not galiba
                            case TERRITORY_TO -> {
                                reqs.put(ChanceCardRequirement.TERRITORY_TO, MainScreen.getLastTerritoryID());
                            }
                            case TERRITORY_FROM -> reqs.put(ChanceCardRequirement.TERRITORY_FROM, MainScreen.getLastSecondTerritoryID());
                            default -> {
                                flag  = false;
                                JOptionPane.showMessageDialog(TurnPanel.this,
                                        "Territory not selected!");
                            }
                        }
                    }
                    boolean reqsSuccessful = false;
                    if (flag){
                       reqsSuccessful = turnHandler.useChanceCard(reqs);
                    }

                    if (!reqsSuccessful) {
                        JLabel label = new JLabel("Wrong Territory Selection");
                        label.setFont(new Font("Arial", Font.ITALIC, 40));
                        label.setForeground(Color.RED);
                        label.setBounds(500,0,600,90);
                        panel3.add(label);

                        //---------------- Animation for wrong territory selection ----------------//
                        flashLabel(panel3, label, 500, 5000);
                        //--------------------------------------------------------------------------//
                    } else {
                        TurnPanel.this.currentChanceRequirements = null;
                    }
                }
            }
        });

        multipleDeploySelect.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                // this part for re-enable the chance cards //
                panel3.add(pickchanceCard);
                panel3.remove(label);
                panel3.remove(labelForExp);
                panel3.remove(showchanceCard);
                pickchanceCard.setEnabled(true);
                panel3.revalidate();
                panel3.repaint();
                //------------------------------------------//
                Integer terId = MainScreen.getLastTerritoryID();
                if (terId == null) {
                    JLabel deploy_success = new JLabel("You didn't provided a territory!");
                    deploy_success.setFont(new Font("Arial", Font.ITALIC, 35));
                    deploy_success.setForeground(Color.RED);
                    deploy_success.setBounds(500,0,600,90);
                    panel5.add(deploy_success);

                    //---------------- Animation for wrong Deployment successful ----------------//
                    flashLabel(panel5, deploy_success, 500, 5000);
                    //--------------------------------------------------------------------------//
                }
                else{
                    if (turnHandler.deployMultipleArmies(terId,
                            Integer.parseInt(multipleDeployNumber.getValue().toString()))) {

                        JLabel deploy_success = new JLabel("Deployment Successful!");
                        deploy_success.setFont(new Font("Arial", Font.ITALIC, 40));
                        deploy_success.setForeground(Color.white);
                        deploy_success.setBounds(500,0,600,90);
                        panel5.add(deploy_success);

                        //---------------- Animation for wrong Deployment successful ----------------//
                        flashLabel(panel5, deploy_success, 500, 5000);
                        //--------------------------------------------------------------------------//
                    } else {
                        JLabel deploy_unSuccessful = new JLabel("Deployment Not Successful!");
                        deploy_unSuccessful.setFont(new Font("Arial", Font.ITALIC, 40));
                        deploy_unSuccessful.setForeground(Color.RED);
                        deploy_unSuccessful.setBounds(500,0,600,90);
                        panel4.add(deploy_unSuccessful);

                        //---------------- Animation for wrong Deployment successful ----------------//
                        flashLabel(panel4, deploy_unSuccessful, 500, 5000);
                        //--------------------------------------------------------------------------//
                    }
                }
            }
        });

        attackSelect.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {

                if(attackOption==3){
                    boolean validInput = false;
                    while (!validInput) {
                        String input = JOptionPane.showInputDialog("How many armies do you sacrifice???:");
                        try {
                            attackNumber = Integer.parseInt(input);
                            validInput = true;
                        }
                        catch (NumberFormatException e1) {
                            JOptionPane.showMessageDialog(null, "Invalid input. Please enter an integer.");
                        }
                    }
                    from = MainScreen.getLastSecondTerritoryID();
                    to = MainScreen.getLastTerritoryID();
                    if(turnHandler.constructAttack(from ,
                           to , attackOption, attackNumber)){
                        //nas�l boolean
                        boolean isConquered = turnHandler.attack();
                        if (isConquered){

                            boolean validInp = false;
                            boolean validInp2 = false;
                            while (!validInp || !validInp2) {
                                validInp = false;
                                validInp2 = false;
                                String input = JOptionPane.showInputDialog(panel5,"How many armies do you want to fortify?",null);
                                try {
                                    int numberOfArmyToDeploy = Integer.parseInt(input);
                                    validInp = true;

                                    validInp2 = turnHandler.attackFortifier(numberOfArmyToDeploy);
                                    if (!validInp2){
                                        JOptionPane.showMessageDialog(null, "Invalid input!");
                                    }
                                    else{
                                        JLabel label1 = new JLabel("Attack fortify successful!");
                                        label1.setFont(new Font("Arial", Font.ITALIC, 40));
                                        label1.setForeground(Color.white);
                                        label1.setBounds(500,0,600,90);
                                        panel5.add(label1);

                                        //---------------- Animation for wrong territory selection ----------------//
                                        flashLabel(panel5, label1, 500, 5000);
                                        //--------------------------------------------------------------------------//
                                    }
                                }
                                catch (NumberFormatException e1) {
                                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter an integer.");
                                }
                            }
                        }
                    }
                    else{
                        JLabel label1 = new JLabel("Attack Construction Unsuccessful!");
                        label1.setFont(new Font("Arial", Font.ITALIC, 35));
                        label1.setForeground(Color.RED);
                        label1.setBounds(500,0,690,90);
                        panel5.add(label1);

                        //---------------- Animation for wrong territory selection ----------------//
                        flashLabel(panel5, label1, 500, 5000);
                        //--------------------------------------------------------------------------//
                    }
                }
                else if (attackOption == 1 || attackOption == 2) {
                    from = MainScreen.getLastSecondTerritoryID();
                    to = MainScreen.getLastTerritoryID();
                    if(turnHandler.constructAttack(from,to
                            , attackOption,null)){
                        boolean isConquered = turnHandler.attack();
                        if (isConquered){
                            boolean validInp = false;
                            boolean validInp2 = false;
                            while (!validInp || !validInp2) {
                                validInp = false;
                                validInp2 = false;
                                String input = JOptionPane.showInputDialog("How many armies do you want to fortify?");
                                try {
                                    int numberOfArmyToDeploy = Integer.parseInt(input);
                                    validInp = true;

                                    validInp2 = turnHandler.attackFortifier(numberOfArmyToDeploy);
                                    if (!validInp2){
                                        JOptionPane.showMessageDialog(null, "Invalid input!");
                                    }
                                    else{
                                        JLabel label1 = new JLabel("Attack fortify successful!");
                                        label1.setFont(new Font("Arial", Font.ITALIC, 40));
                                        label1.setForeground(Color.white);
                                        label1.setBounds(500,0,600,90);
                                        panel5.add(label1);

                                        //---------------- Animation for wrong territory selection ----------------//
                                        flashLabel(panel5, label1, 500, 5000);
                                        //--------------------------------------------------------------------------//
                                    }
                                }
                                catch (NumberFormatException e1) {
                                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter an integer.");
                                }
                            }
                        }
                    }
                    else{

                        JLabel label1 = new JLabel("Attack Construction Unsuccessful!");
                        label1.setFont(new Font("Arial", Font.ITALIC, 35));
                        label1.setForeground(Color.RED);
                        label1.setBounds(500,0,690,90);
                        panel5.add(label1);

                        //---------------- Animation for wrong territory selection ----------------//
                        flashLabel(panel5, label1, 500, 5000);
                        //--------------------------------------------------------------------------//
                    }
                }
                else {
                    JLabel label1 = new JLabel("Choose attack option!");
                    label1.setFont(new Font("Arial", Font.ITALIC, 35));
                    label1.setForeground(Color.RED);
                    label1.setBounds(500,0,690,90);
                    panel5.add(label1);

                    //---------------- Animation for wrong territory selection ----------------//
                    flashLabel(panel5, label1, 500, 5000);
                    //--------------------------------------------------------------------------//
                }
                //if attack is won fortify
            }
        });

        attackFinish.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                turnHandler.finishAttack();
            }
        });

        fortifySelect.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(turnHandler.fortify(MainScreen.getLastSecondTerritoryID(),
                        MainScreen.getLastTerritoryID(),Integer.parseInt(fortifyNumber.getValue().toString()))){

                    JLabel fortify_successful= new JLabel("Fortify Successful!");
                    fortify_successful.setFont(new Font("Arial", Font.ITALIC, 40));
                    fortify_successful.setForeground(Color.white);
                    fortify_successful.setBounds(600,0,600,90);
                    panel6.add(fortify_successful);

                    //---------------- Animation for wrong Deployment successful ----------------//
                    flashLabel(panel6, fortify_successful, 500, 5000);
                    //--------------------------------------------------------------------------//
                    fortifySelect.setEnabled(false);
                }
                else{
                    JLabel fortify_failed = new JLabel("Fortify Failed!");
                    fortify_failed.setFont(new Font("Arial", Font.ITALIC, 40));
                    fortify_failed.setForeground(Color.RED);
                    fortify_failed.setBounds(650,0,600,90);
                    panel6.add(fortify_failed);

                    //---------------- Animation for wrong Deployment successful ----------------//
                    flashLabel(panel6, fortify_failed, 500, 5000);
                    //--------------------------------------------------------------------------//
                }
            }
        });

        fortifyFinish.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                turnHandler.finishTurn();
            }
        });
    }
    public String insertLineBreaks(String text, int maxLineLength) {
        StringBuilder newText = new StringBuilder("<html>");
        int lineLen = 0;

        String[] words = text.split(" ");
        for (String word : words) {
            if (lineLen + word.length() > maxLineLength) {
                newText.append("<br>");
                lineLen = 0;
            }
            newText.append(word).append(" ");
            lineLen += word.length() + 1; // +1 for the space
        }

        newText.append("</html>");
        return newText.toString().trim(); // Remove trailing spaces
    }




    @Override
    public void onTurnEvent(Event event) {
        playerInfoPanel.updatePlayerInfo();
        switch(event.getFunctionName()){

            case "pickAChanceCard" -> {

                //   this part for edditing the message  //

                String message = event.getNewString().replaceFirst("\\W*\\w+\\W*\\w+$", "");
                int totalLength = 25;
                int startSpace = (totalLength - message.length()) / 2;
                int endSpace = totalLength - startSpace - message.length();
                String newMessage = String.format("%" + startSpace + "s%s%" + endSpace + "s", "", message, "");

                //---------------------------------------//
                //              Card Name               //
                label = new JLabel(newMessage);
                label.setFont(new Font("Arial", Font.ITALIC, 20));
                label.setBounds(15,119,290,90);
                panel3.add(label);
                //---------------------------------------//

                labelForExp = new JLabel(insertLineBreaks(event.getExplanation(), 25));
                labelForExp.setFont(new Font("Arial", Font.ITALIC, 13));
                labelForExp.setBounds(32,149,290,190);
                panel3.add(labelForExp);

                TurnPanel.this.currentChanceRequirements = event.getRequirements();




            }
            case "attack" -> {

                CustomImageButton attacked = territorybuttons.get(to-1);
                gamePanel.playExplosionAnimation(attacked.getX(),attacked.getY());

                if(event.getABoolean()){

                    JLabel fortify_failed = new JLabel("VICTORY!");
                    fortify_failed.setFont(new Font("Arial", Font.ITALIC, 40));
                    fortify_failed.setForeground(Color.white);
                    fortify_failed.setBounds(650,0,600,90);
                    panel5.add(fortify_failed);

                    //---------------- Animation for wrong Deployment successful ----------------//
                    flashLabel(panel5, fortify_failed, 500, 5000);
                    //--------------------------------------------------------------------------//

                }else {
                    JLabel fortify_failed = new JLabel("DEFEAT!");
                    fortify_failed.setFont(new Font("Arial", Font.ITALIC, 40));
                    fortify_failed.setForeground(Color.RED);
                    fortify_failed.setBounds(650,0,600,90);
                    panel5.add(fortify_failed);

                    //---------------- Animation for wrong Deployment successful ----------------//
                    flashLabel(panel5, fortify_failed, 500, 5000);
                    //--------------------------------------------------------------------------//
                }

            }
            case "finishTurn" -> JOptionPane.showMessageDialog(TurnPanel.this,
                    String.format("YOU GAINED %s", event.getNewString()));
            case "setPhase" -> {
                TurnPhase newPhase = event.getNewPhase();
                //JOptionPane.showMessageDialog(TurnPanel.this,
                       // String.format(newPhase.toString()));
                this.closeAllPanels();
                switch(newPhase){
                    case TERRITORYSELECTION -> this.panel1.setVisible(true);
                    case SINGLEARMYDEPLOYMENT -> this.panel2.setVisible(true);
                    case CHANCECARD -> this.panel3.setVisible(true);
                    case ARMYDEPLOYMENT -> this.panel4.setVisible(true);
                    case ATTACK -> this.panel5.setVisible(true);
                    case FORTIFY -> {
                        fortifySelect.setEnabled(true);
                        this.panel6.setVisible(true);
                    }
                    // case VIEWMODE -> Do Nothing
                }
            }
            case "setArmyToDeploy" -> {
                this.numOfArmyLabel.setFont(new Font("Arial", Font.ITALIC, 20));
                this.numOfArmyLabel.setText(String.format("Left: %d", event.getNewInt()));
            }
            case "mergecard" -> {JLabel fortify_failed = new JLabel("Cards Merged Successfully!");
                fortify_failed.setFont(new Font("Arial", Font.ITALIC, 35));
                fortify_failed.setForeground(Color.white);
                fortify_failed.setBounds(550,0,600,90);
                panel4.add(fortify_failed);

                //---------------- Animation for wrong Deployment successful ----------------//
                flashLabel(panel4, fortify_failed, 500, 5000);
                //--------------------------------------------------------------------------//

            }
        }
    }

    public void setDiceAnimationExplanation(String explanation){
        this.diceAnimationExplanation = explanation;
    }
    public String getDiceAnimationExplanation(){
        return this.diceAnimationExplanation;
    }
    public void setDiceAnimationNumber(int num){
        this.diceAnimationNumber = num;
    }
    public int getDiceAnimationNumber(){
        return this.diceAnimationNumber;
    }

    private void closeAllPanels(){
        this.panel1.setVisible(false);
        this.panel2.setVisible(false);
        this.panel3.setVisible(false);
        this.panel4.setVisible(false);
        this.panel5.setVisible(false);
        this.panel6.setVisible(false);
    }







}