/*
 *    Copyright 2021 ParkerTenBroeck
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.parker.mips.architecture.syscall.internal;

import org.parker.mips.architecture.syscall.SystemCallPluginFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


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

    public static int getPixelColor(int index) {
        try {
            return image.getRGB(index % image.getWidth(), index / image.getWidth()) & 0xFFFFFF;
        } catch (Exception e) {
            return 0;
        }
    }

    public static int getPixelColor(int xPos, int yPos) {
        try {
            return image.getRGB(xPos, yPos) & 0xFFFFFF;
        } catch (Exception e) {
            return 0;
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