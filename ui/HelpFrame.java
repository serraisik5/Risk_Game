package ui;

import javax.swing.*;
import java.awt.*;

public class HelpFrame extends JFrame {

    private JLabel label1;
    private JLabel label2;
    private  JLabel label3;
    private Container panel = this.getContentPane();
    public HelpFrame(){
        ImageIcon backgroundImage = new ImageIcon("src/resources/helpmessage.png");
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setLayout(new BorderLayout());

        // Set the content pane of the frame as the background label
        setContentPane(backgroundLabel);
    }
}
