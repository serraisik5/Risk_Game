package ui;

import controller.*;
import domain.object.Player;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class BuildGameConstructor {

    private JFrame frame = MainScreen.frame;
    private BuildGamePanel panel;
    private JButton button1;
    private JButton button2;
    private BuildGameHandler buildGameHandler;


    private String getColorName(Color color) {
        if (Color.RED.equals(color)) {
            return "Red";
        } else if (Color.BLUE.equals(color)) {
            return "Blue";
        } else if (Color.GREEN.equals(color)) {
            return "Green";
        } else if (Color.CYAN.equals(color)) {
            return "Cyan";
        } else if (Color.ORANGE.equals(color)) {
            return "Orange";
        } else if (Color.MAGENTA.equals(color)) {
            return "Purple";
        } else {
            return "Unknown";
        }
    }

    public JPanel BuildGameConstructor(BuildGameHandler buildGameHandler) {
        this.buildGameHandler = buildGameHandler;
        createPanel(buildGameHandler);
        createButtons();
        return this.panel;
    }


    private void createPanel(BuildGameHandler buildGameHandler) {
        this.panel = new BuildGamePanel(buildGameHandler){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundImage = new ImageIcon("src/resources/BuildGame.png");
                g.drawImage(backgroundImage.getImage(), 0, 0, frame.getWidth(), frame.getHeight(), null);
            }
        };
        this.panel.setLayout(null);
        this.panel.setSize(new Dimension(frame.getWidth(), frame.getHeight()));
    }

    private void createButtons() {
        int numOfThePlayers=0;
        button1 = new JButton();
        button2 = new JButton();
        button1.setIcon(new ImageIcon("src/resources/AddPlayer.png"));
        button2.setIcon(new ImageIcon("src/resources/Start.png"));

        this.panel.add(button1);
        this.panel.add(button2);

        // Make button1 transparent and remove the border
        button1.setOpaque(false);
        button1.setContentAreaFilled(false);
        button1.setBorderPainted(false);

        // Make button2 transparent and remove the border
        button2.setOpaque(false);
        button2.setContentAreaFilled(false);
        button2.setBorderPainted(false);


        button1.addActionListener(new ActionListener() {
            private final JComboBox<Color> colorComboBox = new JComboBox<>(new Color[]{Color.RED, Color.MAGENTA, Color.GREEN, Color.CYAN, Color.ORANGE, Color.BLUE});
            private boolean control = false;
            public int numOfPlayers;
            private final ArrayList<String> names = new ArrayList<>();
            private JSlider numOfPlayerSlider = null;
            private JPanel nameAndColorSelectionPanel = null;
            private JTextField nicknameField;
            private int yOfLabels=0;

            private void labelCreator(String name, Color color) {
                panel.setOpaque(false);

                JLabel label = new JLabel(name);
                label.setForeground(Color.WHITE);
                label.setFont(new Font("Times New Roman", Font.ITALIC, 50));
                label.setBounds(320, 70 + yOfLabels, 260, 100);

                String my_path = "";
                switch (getColorName(color)) {
                    case "Cyan" -> my_path = "src/resources/MyBlue.png";
                    case "Blue" -> my_path = "src/resources/MyCyan.png";
                    case "Red" -> my_path = "src/resources/MyRed.png";
                    case "Orange" -> my_path = "src/resources/MyOrange.png";
                    case "Green" -> my_path = "src/resources/MyGreen.png";
                    case "Purple" -> my_path = "src/resources/MyPurple.png";
                    default -> {
                    }
                }

                ImageIcon imageIcon = new ImageIcon(my_path);
                JLabel imageLabel = new JLabel(imageIcon);
                imageLabel.setBounds(592, 84 + yOfLabels, imageIcon.getIconWidth(), imageIcon.getIconHeight()-4);

                panel.add(imageLabel);
                panel.add(label);

                panel.revalidate();
                panel.repaint();

                yOfLabels += 91;
            }


            private JPanel getNameAndColorSelectionPanel(){
                if (this.nameAndColorSelectionPanel == null){
                    JLabel nicknameLabel = new JLabel("Nickname: ");
                    this.nicknameField = new JTextField(10);
                    JLabel colorLabel = new JLabel("Color: ");
                    this.colorComboBox.setRenderer(new DefaultListCellRenderer() {
                        @Override
                        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                            if (value instanceof Color) {
                                c.setBackground((Color) value);
                                c.setForeground(Color.BLACK);
                                setText(getColorName((Color) value));
                            }
                            return c;
                        }
                    });

                    JPanel customPanel = new JPanel();
                    customPanel.setLayout(new GridLayout(2, 2));

                    customPanel.add(nicknameLabel);
                    customPanel.add(this.nicknameField);
                    customPanel.add(colorLabel);
                    customPanel.add(this.colorComboBox);
                    this.nameAndColorSelectionPanel = customPanel;
                }
                return this.nameAndColorSelectionPanel;
            }

            private JSlider getNumOfPlayerSlider(){
                if (this.numOfPlayerSlider == null) {
                    JSlider slider = new JSlider(JSlider.HORIZONTAL, 2, 6, 2);
                    slider.setMinorTickSpacing(1);
                    slider.setSnapToTicks(true);
                    slider.setPaintTicks(true);
                    slider.setPaintLabels(true);

                    Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
                    labelTable.put(2, new JLabel("2"));
                    labelTable.put(3, new JLabel("3"));
                    labelTable.put(4, new JLabel("4"));
                    labelTable.put(5, new JLabel("5"));
                    labelTable.put(6, new JLabel("6"));
                    slider.setLabelTable(labelTable);
                    this.numOfPlayerSlider = slider;
                }
                return this.numOfPlayerSlider;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                if (control) {
                    JPanel nameAndColorSelectionPanel = this.getNameAndColorSelectionPanel();
                    int result = JOptionPane.showConfirmDialog(frame, nameAndColorSelectionPanel, "Select your name and color", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (result == JOptionPane.OK_OPTION) {
                        Color selectedColor = (Color) this.colorComboBox.getSelectedItem();
                        String name = this.nicknameField.getText().strip();

                        // controls
                        boolean isEmpty = name.isEmpty();
                        boolean doesContain = this.names.contains(name);

                        if (!isEmpty && !doesContain){
                            if(panel.getBuildGameHandler().createPlayer(name, selectedColor)){
                                this.names.add(name);
                                this.colorComboBox.removeItem(selectedColor);
                                this.numOfPlayers -= 1;

                                labelCreator(name, selectedColor);

                            }
                        }
                        else if(isEmpty){
                            JOptionPane.showMessageDialog(frame, "Nickname cannot be null!");
                        }
                        else if(doesContain){
                            JOptionPane.showMessageDialog(frame, "Nickname already occupied!");
                        }
                    }

                    // resets the nickname field
                    this.nicknameField.setText("");

                    if (this.numOfPlayers == 0){
                        button1.setEnabled(false);
                    }
                }
                else{
                    JSlider slider = this.getNumOfPlayerSlider();
                    Object[] message = {"Select a value between 2 and 6:", slider};

                    Object[] options = { "OK", "Cancel" };

                    int option = JOptionPane.showOptionDialog(
                            null,
                            message,
                            "Number of Players",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            options,
                            options[0]
                    );

                    if (option == JOptionPane.OK_OPTION){
                        int sliderNumOfPlayers = slider.getValue();
                        if (panel.getBuildGameHandler().setNumberOfPlayers(sliderNumOfPlayers)){
                            this.numOfPlayers = sliderNumOfPlayers;
                            this.control = true;
                        }
                    }
                }

            }
        });

        button2.addActionListener(new ActionListener() {
            private int yOfLabels = 0;
            private String[] imagePaths;
            private JLabel imageLabel;
            private Timer timer;

            private void diceAnimation(int count, Integer[] turn_array) {
                imagePaths = new String[]{"src/resources/DiceOne.png", "src/resources/DiceTwo.png", "src/resources/DiceThree.png", "src/resources/DiceFour.png", "src/resources/DiceFive.png", "src/resources/DiceSix.png"};

                int numberOfImages = new Random().nextInt(11) + 9; // Generate a random number between 10 and 20
                // Initialize the imageLabel
                imageLabel = new JLabel();

                // Initialize the timer
                timer = new Timer(150, new ActionListener() {
                    private int imageCounter = 0;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Update the image on the label
                        int randomIndex = new Random().nextInt(imagePaths.length);
                        updateImage(randomIndex);

                        imageCounter++;
                        if (imageCounter >= numberOfImages) {
                            // Stop the timer after the specified number of image changes
                            timer.stop();

                            if (count>=1) {
                                updateImage(turn_array[0]);
                            }
                            if (count>1) {
                                Integer[] newArray = Arrays.copyOfRange(turn_array, 1, turn_array.length);
                                diceAnimation(count-1, newArray);
                            }
                            yOfLabels+=90;
                        }
                    }
                });
                // Start the timer
                timer.start();

            }

            private void updateImage(int randomIndex) {
                // Set a random image from the imagePaths array
                ImageIcon imageIcon = new ImageIcon(imagePaths[randomIndex]);

                imageLabel.setIcon(imageIcon);
                imageLabel.setBounds(1000, 87 + yOfLabels, imageIcon.getIconWidth(), imageIcon.getIconHeight());

                panel.add(imageLabel);
                panel.revalidate();
                panel.repaint();
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                if (buildGameHandler.goCreateMap()) {
                    TreeMap<Integer, Player> toPassToCreateTurnList = new TreeMap<>();
                    TreeMap<Integer, Integer> toPassToUI = new TreeMap<>();
                    for (Player p : BuildGameConstructor.this.panel.getBuildGameHandler().getAllPlayers()){
                        Integer num = BuildGameConstructor.this.panel.getBuildGameHandler().pickANumberFromBag();
                        toPassToCreateTurnList.put(num, p);
                        toPassToUI.put(p.getPlayerNum(), num-1);
                    }
                    BuildGameConstructor.this.panel.getBuildGameHandler().createTurnList(toPassToCreateTurnList);
                    ArrayList<Integer> a = new ArrayList<Integer>(toPassToUI.values());

                    Integer[] toPassToUIFinal = a.toArray(new Integer[0]);

                    // Animation
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            int num_of_p = buildGameHandler.getNumOfPlayers();
                            diceAnimation(num_of_p, toPassToUIFinal);
                            // Delay before executing the next section of code
                            int delay = 150 * num_of_p * 18; // Adjust the delay time as needed
                            Timer delayTimer = new Timer(delay, new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    // Execute the code after the delay
                                    D_E_Territories d_e_territories = new D_E_Territories();
                                    JPanel newPanel = d_e_territories.D_E_Territories(buildGameHandler);
                                    changePanel(newPanel);
                                }
                            });
                            delayTimer.setRepeats(false);
                            delayTimer.start();
                        }
                    });
                } else {
                    JOptionPane.showMessageDialog(panel, "Please create the players first!", "Missing player name", JOptionPane.INFORMATION_MESSAGE);
                }
            }

        });
        // call updateButtonSizesAndPositions initially to set initial button sizes and positions
        updateButtonSizesAndPositions();
    }


    private void changePanel(JPanel newPanel) {
        frame.getContentPane().removeAll();
        frame.add(newPanel);
        frame.revalidate();
        frame.repaint();
    }



    private void updateButtonSizesAndPositions() {
        double width_multiplier = (double) frame.getWidth() / 1440;
        double height_multiplier = (double) frame.getHeight() / 812;

        // update size and position of button1
        int button1X = (int) (240 * width_multiplier);
        int button1Y = (int) (frame.getHeight() - 144 * height_multiplier);
        int button1Width = (int) (440 * width_multiplier);
        int button1Height = (int) (105 * height_multiplier);
        button1.setBounds(button1X, button1Y, button1Width, button1Height);

        // update size and position of button2
        int button2X = (int) (814 * width_multiplier);
        int button2Y = (int) (frame.getHeight() - 144 * height_multiplier);
        int button2Width = (int) (300 * width_multiplier);
        int button2Height = (int) (105 * height_multiplier);
        button2.setBounds(button2X, button2Y, button2Width, button2Height);
    }
}
