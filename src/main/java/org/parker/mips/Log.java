/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips;

import java.awt.Color;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import javax.swing.BoundedRangeModel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import org.parker.mips.GUI.lookandfeel.ModernScrollPane;

/**
 *
 * @author parke
 */
public class Log extends javax.swing.JPanel {

    private static boolean logMessages = true;
    private static boolean logSystemMessages = true;
    private static boolean logWarnings = true;
    private static boolean logErrors = true;

    static {
        Log.initComponents();
    }

    public static void clearDisplay() {
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n");
        //Log.appendMessageToVirtualConsoleLog("\n\n\n\n\n\n\n\n\n\n\n\n\n", null);
        Log.jTextPane1.setText("");
    }

    public static void logError(String message) {
        if (!logErrors) {
            return;
        }
        System.err.println("[Error] " + message);

        SimpleAttributeSet att = new SimpleAttributeSet();
        StyleConstants.setForeground(att, Color.RED);
        StyleConstants.setBold(att, true);
        Log.appendMessageToVirtualConsoleLog("[Error] " + message, att);
    }

    public static void logWarning(String message) {
        if (!logWarnings) {
            return;
        }
        System.out.println("[Warning] " + message);

        SimpleAttributeSet att = new SimpleAttributeSet();
        StyleConstants.setForeground(att, Color.YELLOW);
        StyleConstants.setBold(att, true);
        Log.appendMessageToVirtualConsoleLog("[Warning] " + message, att);
    }

    public static void logSystemMessage(String message) {
        if (!logSystemMessages) {
            return;
        }
        System.out.println("[System Message] " + message);

        SimpleAttributeSet att = new SimpleAttributeSet();
        StyleConstants.setForeground(att, Color.BLACK);
        StyleConstants.setBold(att, true);
        Log.appendMessageToVirtualConsoleLog("[System Message] " + message, att);
    }

    public static void logMessage(String message) {
        if (!logMessages) {
            return;
        }
        System.out.println("[Message] " + message);

        SimpleAttributeSet att = new SimpleAttributeSet();
        StyleConstants.setForeground(att, Color.BLACK);
        StyleConstants.setBold(att, true);
        Log.appendMessageToVirtualConsoleLog("[Message] " + message, att);
    }

    public static void logCustomMessage(String message, SimpleAttributeSet att) {
        if (!logMessages) {
            return;
        }
        System.out.println("[Message] " + message);

        Log.appendMessageToVirtualConsoleLog("[Message] " + message, att);
    }

    public static void logCustomMessage(String message, boolean bold, boolean italic, boolean underline, Color color, String font) {
        if (!logMessages) {
            return;
        }
        System.out.println("[Message] " + message);

        SimpleAttributeSet att = new SimpleAttributeSet();
        StyleConstants.setForeground(att, color);
        StyleConstants.setBold(att, bold);
        StyleConstants.setItalic(att, italic);
        StyleConstants.setUnderline(att, underline);
        if (font != null) {
            StyleConstants.setFontFamily(att, font);
        }
        Log.appendMessageToVirtualConsoleLog("[Message] " + message, att);
    }

    private static void appendMessageToVirtualConsoleLog(String message, SimpleAttributeSet att) {
        Document doc = Log.jTextPane1.getStyledDocument();
        try {
            doc.insertString(doc.getLength(), message + "\n", att);
        } catch (Exception exc) {
            exc.printStackTrace();
        }

    }

    public Log() {
        initLayout();
        this.setVisible(true);
    }

    private void initLayout() {

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {

        }

        initComponents();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }

    private static void initComponents() {

        if (jTextPane1 == null) {

        } else {
            return;
        }

        jScrollPane1 = new ModernScrollPane(Color.LIGHT_GRAY);
        jTextPane1 = new javax.swing.JTextPane();

        jTextPane1.setEditable(false);
        jTextPane1.setBackground(new java.awt.Color(153, 153, 153));
        jTextPane1.setContentType("HTML/plain"); // NOI18N
        jTextPane1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jScrollPane1.setViewportView(jTextPane1);

        // Get the text area's scroll pane:
        final JScrollPane scrollPane = (JScrollPane) (jTextPane1.getParent().getParent());

        // Disable the auto scroll :
        ((DefaultCaret) jTextPane1.getCaret()).setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

        // Add a listener to the vertical scroll bar :
        scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {

            private int _val = 0;
            private int _ext = 0;
            private int _max = 0;

            private final BoundedRangeModel _model = scrollPane.getVerticalScrollBar().getModel();

            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {

                // Get the new max :
                int newMax = _model.getMaximum();

                // If the new max has changed and if we were scrolled to bottom :
                if (newMax != _max && (_val + _ext == _max)) {

                    // Scroll to bottom :
                    _model.setValue(_model.getMaximum() - _model.getExtent());
                }

                // Save the new values :
                _val = _model.getValue();
                _ext = _model.getExtent();
                _max = _model.getMaximum();
            }
        });

    }// </editor-fold>                        

    // Variables declaration - do not modify                     
    private static javax.swing.JScrollPane jScrollPane1;
    private static javax.swing.JTextPane jTextPane1;
    // End of variables declaration                   
}
