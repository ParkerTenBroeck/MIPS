/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package updater;

import java.io.File;
import java.net.URL;
import java.util.Objects;

/**
 *
 * @author parke
 */
public class Updater {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String protocol = Updater.class.getResource("").getProtocol();
        if (Objects.equals(protocol, "file")) {
            //args = new String[]{"C:\\GitHub\\MIPS\\build\\libs\\MIPS.jar", "https://github.com/ParkerTenBroeck/MIPS/releases/download/0.9.7.4/MIPS.jar"};
        }
        MainGUI gui = new MainGUI();

        File jarFile = null;
        URL jarURL = null;

        try {
            jarFile = new File(args[0]);
        } catch (Exception e) {
            MainGUI.logError("Failed to create JAR file error:" + e.getMessage() + "\n" + e.toString() + "\n");

        }
        try {
            jarURL = new URL(args[1]);
        } catch (Exception e) {
            MainGUI.logError("Failed to create JAR URL error:" + e.getMessage() + "\n" + e.toString() + "\n");
        }

        try {
            MainGUI.logMessage("Started Update of: " + args[0] + "/n to: " + args[1]);
        } catch (Exception e) {

        }

        gui.update(jarFile, jarURL);
    }
}
