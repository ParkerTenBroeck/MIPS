/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.plugin.internal.syscall;

import org.parker.mips.plugin.syscall.SystemCallPluginFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author parke
 */
public class Screen extends SystemCallPluginFrame {

    private static BufferedImage image = new BufferedImage(25, 16, BufferedImage.TYPE_INT_RGB);
    private static BufferedImage imageBuffer = new BufferedImage(25, 16, BufferedImage.TYPE_INT_RGB);

    private static final ArrayList<KeyEvent> keysPressed = new ArrayList();

    public void showScreen() {
        if (!this.isVisible()) {
            this.setVisible(true);
        }
    }

    public static void fillScreen(int val) {
        Graphics2D graphics = image.createGraphics();

        graphics.setPaint(new Color(val));
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
    }

    public Screen() {
        super("Screen");
        this.setTitle("Screen");
        initComponents();
        updateScreen();

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                stopProcessor();
            }
        });

        jPanel1.setDoubleBuffered(true);

        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ke) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void keyPressed(KeyEvent ke) {
                keysPressed.add(ke);
            }

            @Override
            public void keyReleased(KeyEvent ke) {

                for (int i = keysPressed.size() - 1; i >= 0; i--) {
                    if (ke.getKeyCode() == keysPressed.get(i).getKeyCode()) {
                        keysPressed.remove(i);
                    }
                }
            }

        });
    }

    public static boolean isKeyPressed(int keyCode) {
        for (int i = keysPressed.size() - 1; i >= 0; i--) {
            KeyEvent ke = keysPressed.get(i);
            if (ke.getKeyChar() == keyCode) {
                return true;
            }
        }
        return false;
    }

    public static void updateScreen() {
        imageBuffer.getGraphics().drawImage(image, 0, 0, null);
        jPanel1.repaint();
    }

    public static void setScreenSize(int width, int height) {
        try {
            keysPressed.clear();
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            imageBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        } catch (Exception e) {

        }
    }

    public static void setPixelColor(int index, int color) {
        try {
            image.setRGB(index % image.getWidth(), index / image.getWidth(), color);
        } catch (Exception e) {

        }
    }

    public static void setPixelColor(int xPos, int yPos, int color) {
        try {
            image.setRGB(xPos, yPos, color);
        } catch (Exception e) {

        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image i = imageBuffer.getScaledInstance(jPanel1.getSize().width, jPanel1.getSize().height, Image.SCALE_FAST);
                g.drawImage(i, 0, 0, null);
            }
        };
        ;

        setAlwaysOnTop(true);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 437, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 326, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
