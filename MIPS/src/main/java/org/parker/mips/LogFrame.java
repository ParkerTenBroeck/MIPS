/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips;


import org.parker.mips.gui.theme.lookandfeel.ModernScrollPane;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 *
 * @author parke
 */
public class LogFrame extends javax.swing.JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//private static final Logger LOGGER = Logger.getLogger(LogFrame.class.getName());
	
	static {
        LogFrame.initComponents();
    }

    private static void clearDisplay() {
        //System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n");
        //Log.appendMessageToVirtualConsoleLog("\n\n\n\n\n\n\n\n\n\n\n\n\n", null);
        LogFrame.jTextPane1.setText("");
    }

    private static void logError(String message) {
        if (!OptionsHandler.logErrors.val()) {
            return;
        }
        //System.err.println("[Error] " + message);

        SimpleAttributeSet att = new SimpleAttributeSet();
        StyleConstants.setForeground(att, Color.RED);
        StyleConstants.setBold(att, false);
        LogFrame.appendMessageToVirtualConsoleLog("[Error] " + message, att);
    }

    private static void logWarning(String message) {
        if (!OptionsHandler.logWarnings.val()) {
            return;
        }
        //System.out.println("[Warning] " + message);

        SimpleAttributeSet att = new SimpleAttributeSet();
        StyleConstants.setForeground(att, Color.YELLOW);
        StyleConstants.setBold(att, false);
        LogFrame.appendMessageToVirtualConsoleLog("[Warning] " + message, att);
    }

    private static void logSystemMessage(String message) {
        if (!OptionsHandler.logSystemMessages.val()) {
            return;
        }
        //
        // System.out.println("[System Message] " + message);

        SimpleAttributeSet att = new SimpleAttributeSet();
        StyleConstants.setForeground(att, Color.LIGHT_GRAY);
        StyleConstants.setBold(att, false);
        LogFrame.appendMessageToVirtualConsoleLog("[System Message] " + message, att);
    }

    private static void logMessage(String message) {
        if (!OptionsHandler.logMessages.val()) {
            return;
        }
        //System.out.println("[Message] " + message);

        SimpleAttributeSet att = new SimpleAttributeSet();
        StyleConstants.setForeground(att, Color.LIGHT_GRAY);
        StyleConstants.setBold(att, false);
        LogFrame.appendMessageToVirtualConsoleLog("[Message] " + message, att);
    }

    private static void logCustomMessage(String message, SimpleAttributeSet att) {
        if (!OptionsHandler.logMessages.val()) {
            return;
        }
        //System.out.println("[Message] " + message);

        LogFrame.appendMessageToVirtualConsoleLog("[Message] " + message, att);
    }

    private static void logCustomMessage(String message, boolean bold, boolean italic, boolean underline, Color color, String font) {
        if (!OptionsHandler.logMessages.val()) {
            return;
        }
        //System.out.println("[Message] " + message);

        SimpleAttributeSet att = new SimpleAttributeSet();
        StyleConstants.setForeground(att, color);
        StyleConstants.setBold(att, bold);
        StyleConstants.setItalic(att, italic);
        StyleConstants.setUnderline(att, underline);
        if (font != null) {
            StyleConstants.setFontFamily(att, font);
        }
        LogFrame.appendMessageToVirtualConsoleLog("[Message] " + message, att);
    }

    private static void appendMessageToVirtualConsoleLog(String message, SimpleAttributeSet att) {
        Document doc = LogFrame.jTextPane1.getStyledDocument();
        try {
            doc.insertString(doc.getLength(), message + "\n", att);
        } catch (Exception exc) {
            //LogFrame.logError(LogFrame.getFullExceptionMessage(exc));
        }

    }

    public LogFrame() {
        initLayout();
        this.setVisible(true);
    }

    private void initLayout() {

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

        jScrollPane1 = new ModernScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        //jTextPane1.setBackground((Color) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.TEXT_AREA_BACKGROUND_2_PROPERTY_NAME));
        //jTextPane1.setFont((Font) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.GENERAL_TEXT_FONT_PROPERTY_NAME)); // NOI18N
        jTextPane1.setEditable(false);
        jTextPane1.setContentType("HTML/plain"); // NOI18N
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

    public static class LogFrameHandler extends Handler {

        @Override
        public void publish(LogRecord record) {

            String message = "[" + String.join("] [", record.getSourceClassName().replaceFirst("org.parker.mips.", "").split("\\.")) + "] " + record.getMessage();

            SimpleAttributeSet sas = new SimpleAttributeSet();

            if(record.getLevel() == Level.INFO){
                //LogFrame.logMessage(message);
            }else if(record.getLevel() == Level.WARNING){
                //LogFrame.logWarning(message);
            }else if(record.getLevel() == Level.SEVERE){
                //LogFrame.logError(message);
            }else{

            }record.getSourceClassName();

            LogFrame.appendMessageToVirtualConsoleLog("[" + record.getLevel().getName() + "]" + message,sas);

            return;

        }

        @Override
        public void flush() {

        }

        @Override
        public void close() throws SecurityException {

        }
    }
}