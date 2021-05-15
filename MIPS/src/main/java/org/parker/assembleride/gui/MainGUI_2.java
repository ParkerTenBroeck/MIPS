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
package org.parker.assembleride.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.parker.assembleride.architecture.ArchitecturePluginHandler;
import org.parker.assembleride.architecture.BaseComputerArchitecture;
import org.parker.assembleride.gui.components.FlatZeroWidthSplitPane;
import org.parker.assembleride.gui.docking.UserPaneTabbedPane;
import org.parker.assembleride.gui.docking.userpanes.editor.rsyntax.FormattedTextEditor;
import org.parker.assembleride.log.LogPanel;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainGUI_2 extends JFrame {
    private JPanel contentRoot;
    private UserPaneTabbedPane userPaneTabbedPane1;
    private test test1;
    private JSplitPane hSplitter;
    private JSplitPane vSplitter;
    private JPanel rootPanel;
    private ToolBar toolBar1;

    private final BaseComputerArchitecture bca;
    private static final Logger LOGGER = Logger.getLogger(MainGUI_2.class.getName());

    public static void main(String... args) {
        ArchitecturePluginHandler.loadArchitecturePlugin();
    }

    public MainGUI_2(BaseComputerArchitecture bca) {
        this.bca = bca;
        $$$setupUI$$$();

        try {
            URL url = ClassLoader.getSystemClassLoader().getResource("Images/Icons/PNG/logo4.png");
            assert url != null;
            ImageIcon icon = new ImageIcon(url);
            this.setIconImage(icon.getImage());
            this.setTitle(ArchitecturePluginHandler.getCurrentArchitecture().getPluginDescription().NAME);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load Icon", e);
        }

        this.add($$$getRootComponent$$$());
        //this.buttonsPanel.setBorder(new FlatMatteBorder(0, 0, 1, 0));
        this.pack();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);

        WindowListener exitListener = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                LOGGER.log(Level.FINER, "Main Window Exit Action Preformed");
                bca.requestSystemExit();
            }
        };

        this.addWindowListener(exitListener);

    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        rootPanel = new JPanel();
        rootPanel.setLayout(new BorderLayout(0, 0));
        contentRoot = new JPanel();
        contentRoot.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), 0, 0));
        rootPanel.add(contentRoot, BorderLayout.CENTER);
        final Spacer spacer1 = new Spacer();
        contentRoot.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        vSplitter.setContinuousLayout(true);
        vSplitter.setOneTouchExpandable(false);
        vSplitter.setOrientation(0);
        vSplitter.setResizeWeight(1.0);
        contentRoot.add(vSplitter, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final LogPanel logPanel1 = new LogPanel();
        logPanel1.setVisible(true);
        vSplitter.setRightComponent(logPanel1);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        vSplitter.setLeftComponent(panel1);
        hSplitter.setAutoscrolls(true);
        hSplitter.setContinuousLayout(true);
        hSplitter.setOneTouchExpandable(false);
        panel1.add(hSplitter, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        hSplitter.setLeftComponent(test1.$$$getRootComponent$$$());
        hSplitter.setRightComponent(userPaneTabbedPane1);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout(0, 0));
        rootPanel.add(panel2, BorderLayout.NORTH);
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        panel2.add(toolBar1.$$$getRootComponent$$$(), BorderLayout.CENTER);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }

    private void createUIComponents() {
        toolBar1 = ToolBar.ToolBarFactory();
        hSplitter = new JSplitPane();//new FlatZeroWidthSplitPane();
        vSplitter = new JSplitPane();//new FlatZeroWidthSplitPane();
        test1 = new test(bca);
        userPaneTabbedPane1 = new UserPaneTabbedPane();

        try {
            new FormattedTextEditor(null);
            throw new NotImplementedException();
        }catch ( Exception ignore){

        }
    }
}
