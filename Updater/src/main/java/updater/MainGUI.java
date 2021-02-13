/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package updater;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 *
 * @author parke
 */
public class MainGUI extends javax.swing.JFrame {

    /**
     * Creates new form GUI
     */
    public MainGUI() {
        initComponents();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        this.setVisible(true);

    }

    private static boolean hasPrintedError = false;

    public static void logError(String message) {
        hasPrintedError = true;
        System.err.println("[Error] " + message);

        SimpleAttributeSet att = new SimpleAttributeSet();
        StyleConstants.setForeground(att, Color.RED);
        StyleConstants.setBold(att, true);
        appendMessageToVirtualConsoleLog("[Error] " + message, att);
    }

    public static void logWarning(String message) {
        System.out.println("[Warning] " + message);

        SimpleAttributeSet att = new SimpleAttributeSet();
        StyleConstants.setForeground(att, Color.YELLOW);
        StyleConstants.setBold(att, true);
        appendMessageToVirtualConsoleLog("[Warning] " + message, att);
    }

    public static void logMessage(String message) {
        System.out.println("[Message] " + message);

        SimpleAttributeSet att = new SimpleAttributeSet();
        StyleConstants.setForeground(att, Color.LIGHT_GRAY);
        StyleConstants.setBold(att, true);
        appendMessageToVirtualConsoleLog("[Message] " + message, att);
    }

    private static void appendMessageToVirtualConsoleLog(String message, SimpleAttributeSet att) {
        Document doc = jTextPane1.getStyledDocument();
        try {
            doc.insertString(doc.getLength(), message + "\n", att);
        } catch (Exception exc) {
            exc.printStackTrace();
        }

    }

    public void update(File jarFile, URL jarURL) {

        //deleting old file
        logMessage("Removing Old Files");

        try {
            if (!jarFile.exists()) {
                logWarning("Jar file does not exist");
            }
            if (!jarFile.isFile()) {
                logWarning("Jar file is a directory not a file");
            }

            if (jarFile.exists()) {
                if (jarFile.isFile()) {

                    boolean removed = false;

                    for (int i = 0; i < 5; i++) {
                        try {
                            jarFile.delete();
                            removed = true;
                            break;
                        } catch (Exception e) {
                            try {
                                Thread.sleep(100);
                            } catch (Exception ex) {

                            }
                        }
                    }
                    if (removed) {
                        logMessage("Jar File Removed");
                    } else {
                        logError("Jar File could not be removed");
                    }
                }
            }

        } catch (Exception e) {
            logError("Failed to remove File error: " + e.getMessage() + "\n" + e.toString() + "\n");
        }
        try {
            downloadFile(jarURL, jarFile);
        } catch (Exception ex) {
            logError("Failed to download file error: " + ex.getMessage() + "\n" + ex.toString() + "\n");
//Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (hasPrintedError) {

            KeyListener kl = new KeyListener() {
                @Override
                public void keyTyped(KeyEvent ke) {

                }

                @Override
                public void keyPressed(KeyEvent ke) {
                    System.out.println(ke.getKeyCode());
                    if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        System.exit(0);
                    }
                }

                @Override
                public void keyReleased(KeyEvent ke) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            };

            this.addKeyListener(kl);
            //this.jPanel1.addKeyListener(kl);
            logMessage("Press the escape key to exit");
        } else {
            logMessage("Launching Updated Application!");
            //this.Message.setText("Launching Application!");
            launchJar(jarFile.getAbsolutePath());
            System.exit(0);
        }
    }

    public void launchJar(String jarPath) {
        String[] run = {"java", "-jar", jarPath, "Updated"};

        try {
            Runtime.getRuntime().exec(run);
        } catch (Exception e) {

        }
    }

    private void downloadFile(URL url, File file) {

        logMessage("Starting Connection to: " + url.toString());

        URLConnection conn = null;
        InputStream is = null;
        long max = 0;
        try {
            conn = url.openConnection();
            is = conn.getInputStream();
            max = conn.getContentLength();
            logMessage("Started Connection");
        } catch (IOException e) {
            logError("Failed to start Connection error: " + e.getMessage() + "\n" + e.toString() + "\n");
            return;
        }
        BufferedOutputStream fOut = null;
        try {
            fOut = new BufferedOutputStream(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            logError("Cannor create output stream for downloaded File error: " + e.getMessage() + "\n" + e.toString() + "\n");
            return;
        }
        logMessage("Downloding file...\nUpdate Size: " + max + " Bytes");

        byte[] buffer = new byte[32 * 1024];
        int bytesRead = 0;
        int in = 0;

        try {
            while ((bytesRead = is.read(buffer)) != -1) {
                in += bytesRead;
                fOut.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            logError("faild while downloading file error: " + e.getMessage() + "\n" + e.toString() + "\n");
        }
        try {
            fOut.flush();
            fOut.close();
            is.close();
        } catch (IOException e) {
            logError("failed to close output streams error: " + e.getMessage() + "\n" + e.toString() + "\n");
        }

        logMessage("Download Complete!");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        modernProgressBar1 = new updater.ModernProgressBar();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setForeground(new java.awt.Color(51, 51, 51));
        jPanel1.setToolTipText("");

        modernProgressBar1.setValue(50);
        modernProgressBar1.setFocusable(false);

        jLabel1.setFont(new java.awt.Font("Yu Gothic", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(204, 204, 204));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Updating");
        jLabel1.setFocusable(false);

        jTextPane1.setEditable(false);
        jTextPane1.setBackground(new java.awt.Color(51, 51, 51));
        jTextPane1.setBorder(null);
        jTextPane1.setForeground(new java.awt.Color(204, 204, 204));
        jTextPane1.setFocusable(false);
        jScrollPane1.setViewportView(jTextPane1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addComponent(modernProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(modernProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                .addContainerGap())
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
            java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private static javax.swing.JTextPane jTextPane1;
    private updater.ModernProgressBar modernProgressBar1;
    // End of variables declaration//GEN-END:variables
}
