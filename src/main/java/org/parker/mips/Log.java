/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.beans.PropertyChangeEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.swing.BoundedRangeModel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import org.parker.mips.gui.theme.components.ThemableComponent;
import org.parker.mips.gui.theme.ThemeHandler;
import org.parker.mips.gui.theme.lookandfeel.ModernScrollPane;

/**
 *
 * @author parke
 */
public class Log extends javax.swing.JPanel implements ThemableComponent {

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        switch (pce.getPropertyName()) {
            case ThemeHandler.TEXT_AREA_BACKGROUND_2_PROPERTY_NAME:
                jTextPane1.setBackground((Color) pce.getNewValue());
                break;
            case ThemeHandler.GENERAL_TEXT_FONT_PROPERTY_NAME:
                jTextPane1.setFont((Font) pce.getNewValue());
                break;
        }
    }

    static {
        Log.initComponents();

        Log log = new Log();
        ThemeHandler.addPropertyChangeListenerFromName(ThemeHandler.TEXT_AREA_BACKGROUND_2_PROPERTY_NAME, log);
        ThemeHandler.addPropertyChangeListenerFromName(ThemeHandler.GENERAL_TEXT_FONT_PROPERTY_NAME, log);
    }

    public static void clearDisplay() {
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n");
        //Log.appendMessageToVirtualConsoleLog("\n\n\n\n\n\n\n\n\n\n\n\n\n", null);
        Log.jTextPane1.setText("");
    }

    public static void logError(String message) {
        if (!OptionsHandler.logErrors.value) {
            return;
        }
        System.err.println("[Error] " + message);

        SimpleAttributeSet att = new SimpleAttributeSet();
        StyleConstants.setForeground(att, Color.RED);
        StyleConstants.setBold(att, true);
        Log.appendMessageToVirtualConsoleLog("[Error] " + message, att);
    }

    public static void logWarning(String message) {
        if (!OptionsHandler.logWarnings.value) {
            return;
        }
        System.out.println("[Warning] " + message);

        SimpleAttributeSet att = new SimpleAttributeSet();
        StyleConstants.setForeground(att, Color.YELLOW);
        StyleConstants.setBold(att, true);
        Log.appendMessageToVirtualConsoleLog("[Warning] " + message, att);
    }

    public static void logSystemMessage(String message) {
        if (!OptionsHandler.logSystemMessages.value) {
            return;
        }
        System.out.println("[System Message] " + message);

        SimpleAttributeSet att = new SimpleAttributeSet();
        StyleConstants.setForeground(att, Color.BLACK);
        StyleConstants.setBold(att, true);
        Log.appendMessageToVirtualConsoleLog("[System Message] " + message, att);
    }

    public static void logMessage(String message) {
        if (!OptionsHandler.logMessages.value) {
            return;
        }
        System.out.println("[Message] " + message);

        SimpleAttributeSet att = new SimpleAttributeSet();
        StyleConstants.setForeground(att, Color.BLACK);
        StyleConstants.setBold(att, true);
        Log.appendMessageToVirtualConsoleLog("[Message] " + message, att);
    }

    public static void logCustomMessage(String message, SimpleAttributeSet att) {
        if (!OptionsHandler.logMessages.value) {
            return;
        }
        System.out.println("[Message] " + message);

        Log.appendMessageToVirtualConsoleLog("[Message] " + message, att);
    }

    public static void logCustomMessage(String message, boolean bold, boolean italic, boolean underline, Color color, String font) {
        if (!OptionsHandler.logMessages.value) {
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
            Log.logError(Log.getFullExceptionMessage(exc));
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
            Log.logError(Log.getFullExceptionMessage(e));
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
        jTextPane1.setBackground((Color) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.TEXT_AREA_BACKGROUND_2_PROPERTY_NAME));
        jTextPane1.setFont((Font) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.GENERAL_TEXT_FONT_PROPERTY_NAME)); // NOI18N
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

    public static String getFullErrorMessage(Error e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String exceptionAsString = sw.toString();
        return exceptionAsString;
    }

    public static String getFullExceptionMessage(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String exceptionAsString = sw.toString();
        System.out.println(exceptionAsString);
        return exceptionAsString;
    }

    // Variables declaration - do not modify                     
    private static javax.swing.JScrollPane jScrollPane1;
    private static javax.swing.JTextPane jTextPane1;
    // End of variables declaration                   

}
