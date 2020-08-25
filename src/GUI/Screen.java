/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author parke
 */
public class Screen extends javax.swing.JFrame {

    private static BufferedImage image = new BufferedImage(25, 16, BufferedImage.TYPE_INT_RGB);
    private static BufferedImage imageBuffer = new BufferedImage(25, 16, BufferedImage.TYPE_INT_RGB);

    private static final ArrayList<KeyEvent> keysPressed = new ArrayList();

    public static void fillScreen(int val) {
        Graphics2D graphics = image.createGraphics();

        graphics.setPaint(new Color(val));
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
    }

    public Screen() {
        initComponents();
        updateScreen();

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                Main_GUI.stop();
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
        keysPressed.clear();
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        imageBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public static void setPixelColor(int index, int color) {
        image.setRGB(index % image.getWidth(), index / image.getWidth(), color);
    }

    public static void setPixelColor(int xPos, int yPos, int color) {
        image.setRGB(xPos, yPos, color);
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

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Screen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Screen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Screen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Screen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Screen screen = new Screen();
                screen.setVisible(true);
                screen.setPixelColor(0, 0, new Color(255, 0, 0).getRGB());
                screen.updateScreen();

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
