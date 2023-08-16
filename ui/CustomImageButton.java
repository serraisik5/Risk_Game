package ui;

import listener.Event;
import listener.TerritoryListener;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;

import static java.awt.Font.BOLD;
import static java.awt.Transparency.*;

public class CustomImageButton extends JButton implements TerritoryListener {
    private Shape imageShape;
    private boolean isselected = false;
    private Timer animationTimer;
    private float opacity = 1.0f;
    private boolean fadeIn = true;




    public CustomImageButton(ImageIcon imageIcon) {
        super(imageIcon);
        this.setFocusPainted(false);
        this.setLayout(null);
        this.setText("E");

        this.setHorizontalTextPosition(SwingConstants.CENTER);
        this.setVerticalTextPosition(SwingConstants.CENTER);



        // Convert the ImageIcon's image to a BufferedImage
        Image image = imageIcon.getImage();
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
       // if (isselected) {
            // Apply shine effect for selected state
        //    g2d.setColor(new Color(255, 255, 0, 50));
        //    g2d.fillRect(0, 0, getWidth(), getHeight());
        //}
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        // Create a shape based on the non-transparent part of the image
        imageShape = createImageShape(bufferedImage);
        animationTimer = new Timer(20, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isselected) {
                    if (fadeIn) {
                        opacity += 0.02f;
                        if (opacity >= 1.0f) {
                            opacity = 1.0f;
                            fadeIn = false;
                        }
                    } else {
                        opacity -= 0.02f;
                        if (opacity <= 0.0f) {
                            opacity = 0.0f;
                            fadeIn = true;
                        }
                    }
                    repaint();
                } else {
                    animationTimer.stop();
                    opacity = 1.0f;
                }
            }
        });
    }
    @Override
    public void setText(String text) {
        Font originalFont = getFont();
        Font boldFont = new Font(originalFont.getName(), Font.BOLD, 16);
        setFont(boldFont);
        super.setText(text);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (isselected && opacity > 0.0f) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setClip(imageShape);

            // Create a copy of the button's background color with reduced alpha
            Color shineColor = new Color(getBackground().getRed(), getBackground().getGreen(),
                    getBackground().getBlue(), (int) (opacity * 255));

            // Apply shine effect using an oval gradient
            int shineRadius = Math.max(getWidth(), getHeight());
            RadialGradientPaint shineGradient = new RadialGradientPaint(getWidth() / 2f, getHeight() / 2f,
                    shineRadius, new float[] {0f,  1f}, new Color[] {shineColor, getBackground()});

            g2d.setPaint(shineGradient);
            g2d.fill(imageShape);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
            //g2d.fillOval(0, 0, getWidth(), getHeight());

            g2d.dispose();
        }
    }
    public void setSelected(boolean t){
        this.isselected = t;
        if (isselected) {
            animationTimer.start();
        } else {
            animationTimer.stop();
            opacity = 1.0f;
        }
        repaint();
    }

    @Override
    public boolean contains(int x, int y) {
        return imageShape.contains(x, y);
    }

    private Shape createImageShape(BufferedImage image) {
        Area area = new Area();
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int alpha = (image.getRGB(x, y) >> 24) & 0xFF;
                if (alpha > 0) {
                    area.add(new Area(new Rectangle(x, y, 1, 1)));
                }
            }
        }
        return area;
    }


    @Override
    public void onTerritoryEvent(Event event) {

        switch(event.getFunctionName()){
            case "setStatus" -> {
                if (event.getIsSuccessful()) {

                    this.setText("E");
                    this.setHorizontalTextPosition(SwingConstants.CENTER);
                    this.setVerticalTextPosition(SwingConstants.CENTER);
                } else {
                    this.setText("D");
                }
            }
            case "setNumOfArmy" -> {
                this.setText(String.valueOf(event.getNewInt()));
                this.setHorizontalTextPosition(SwingConstants.CENTER);
                this.setVerticalTextPosition(SwingConstants.CENTER);
            }
            case "setOwner" -> {
                this.setForeground(event.getColor());
            }
            // to be done later
            // case "addNeighbor" ->;
            // case "removeNeighbor" ->;
        }
    }

}
