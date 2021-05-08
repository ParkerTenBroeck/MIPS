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

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.components.FlatTabbedPane;
import org.parker.assembleride.architecture.BaseComputerArchitecture;
import org.parker.mips.architecture.MipsArchitecture;
import org.parker.assembleride.gui.components.VerticalJLabel;
import org.parker.assembleride.gui.theme.ThemeHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class test extends JPanel {
    private final BaseComputerArchitecture bca;

    private JPanel rootPanel;
    private FlatTabbedPane flatTabbedPane1;
    private JPanel emulatorPanel;
    private JPanel debugPanel;
    private JPanel projectPanel;

    public static void main(String... args) {
        UIManager.put("TabbedPane.tabHeight", UIManager.getInt("TabbedPane.tabHeight") - 30);
        ThemeHandler.init();

        JFrame frame = new JFrame();
        frame.setContentPane(new test(new MipsArchitecture()));
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public test(BaseComputerArchitecture bca) {
        this.bca = bca;
        $$$setupUI$$$();

        JLabel project = new VerticalJLabel("Project", new FlatSVGIcon("Images/Icons/SVG/projectDirectory.svg", 16, 16));
        JLabel debug = new VerticalJLabel("Debug", new FlatSVGIcon("Images/Icons/SVG/debugger.svg", 16, 16));
        JLabel emulatorState = new VerticalJLabel("Emulator State", new FlatSVGIcon("Images/Icons/SVG/memory.svg", 16, 16));


        flatTabbedPane1.removeAll();
        flatTabbedPane1.addTab("", projectPanel);
        flatTabbedPane1.setTabComponentAt(0, project);
        flatTabbedPane1.addTab("", debugPanel);
        flatTabbedPane1.setTabComponentAt(1, debug);
        flatTabbedPane1.addTab("", emulatorPanel);
        flatTabbedPane1.setTabComponentAt(2, emulatorState);

        flatTabbedPane1.addMouseListener(new MouseListener() {

            int lastMoved = flatTabbedPane1.getSelectedIndex();
            Component components;

            @Override
            public void mouseClicked(MouseEvent e) {
                if (lastMoved != flatTabbedPane1.getSelectedIndex()) {
                    lastMoved = flatTabbedPane1.getSelectedIndex();
                } else if (lastMoved == flatTabbedPane1.getSelectedIndex()) {

                    //flatTabbedPane1.setVisible(false);//getComponentAt(lastMoved).setVisible(false);
                    //flatTabbedPane1.setSize(flatTabbedPane1.getWidth() + 200, flatTabbedPane1.getHeight());
                    //((JSplitPane) getParent()).resetToPreferredSizes();
                    //revalidate();
                    //flatTabbedPane1.getPreferredSize();
                    //System.out.println("double click");
                    if (flatTabbedPane1.isEnabled()) {
                        components = flatTabbedPane1.getComponentAt(lastMoved);
                        flatTabbedPane1.setComponentAt(lastMoved, null);
                    //flatTabbedPane1.getComponentAt(lastMoved).setVisible(false);
                    } else {
                        flatTabbedPane1.setComponentAt(lastMoved, components);
                    //flatTabbedPane1.getComponentAt(lastMoved).setVisible(true);
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

    }

    private void createUIComponents() {
        rootPanel = this;

        if (bca != null) {
            this.emulatorPanel = bca.getEmulatorStatePanel();
        }
        this.debugPanel = new DebugPanel();
        this.projectPanel = new ProjectPanel();
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
        rootPanel.setLayout(new BorderLayout(0, 0));
        flatTabbedPane1 = new FlatTabbedPane();
        flatTabbedPane1.setAutoscrolls(false);
        flatTabbedPane1.setHasFullBorder(false);
        flatTabbedPane1.setHideTabAreaWithOneTab(false);
        flatTabbedPane1.setTabAreaInsets(new Insets(0, 0, 0, 0));
        flatTabbedPane1.setTabHeight(10);
        flatTabbedPane1.setTabInsets(new Insets(5, 2, 5, 2));
        flatTabbedPane1.setTabPlacement(2);
        flatTabbedPane1.setTabWidthMode(FlatTabbedPane.TabWidthMode.preferred);
        flatTabbedPane1.setTabsClosable(false);
        flatTabbedPane1.setTabsPopupPolicy(FlatTabbedPane.TabsPopupPolicy.never);
        rootPanel.add(flatTabbedPane1, BorderLayout.CENTER);
        flatTabbedPane1.addTab("Emulator State", emulatorPanel);
        flatTabbedPane1.addTab("Debug", debugPanel);
        flatTabbedPane1.addTab("Project", projectPanel);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }

}