/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.GUI;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import org.parker.mips.PluginHandler.SystemCallPluginHandler.SystemCallPlugin;

/**
 *
 * @author parke
 */
public class PluginInfoPane extends JPanel {

    private final SystemCallPlugin myPlugin;

    public PluginInfoPane(SystemCallPlugin plugin) {
        this.myPlugin = plugin;

    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        int[] da;
        try {
            da = myPlugin.getSystemCalls()[1].DATA.REGISTERS_READ_FROM;
        } catch (Exception e) {
            da = new int[0];
        }
        boolean[] da2 = new boolean[32];

        for (int i = 0; i < da.length; i++) {
            da2[da[i]] = true;
        }

        for (int i = 0; i < 32; i++) {
            if (da2[i]) {
                g.setColor(Color.yellow);
            } else {
                g.setColor(Color.red);
            }
            g.fillRect(i * 15, 0, 10, 10);
        }
    }

//    public static void main(String[] args) {
//        JFrame frame = new JFrame();
//
//        SystemCallPlugin spc = loadInternalPluginFromClassPath("org.parker.mips.Processor.InternalSystemCallPlugins.DefaultSystemCalls");
//        JPanel pane = new PluginInfoPane(spc);
//        frame.add(pane);
//        pane.setPreferredSize(new Dimension(600, 600));
//
//        frame.pack();
//        frame.setVisible(true);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    }

}
