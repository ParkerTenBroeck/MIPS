/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.gui;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author parke
 */
public class DesktopBrowser {

    public static boolean openLinkInBrowser(String url) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI(url.replaceAll("\\\\", "/")));
                return true;
            } catch (URISyntaxException|IOException ex) {
                Logger.getLogger(DesktopBrowser.class.getName()).log(Level.SEVERE, "Failed to open browser", ex);
            }

        }
        return false;
    }

    public static boolean openFileInBrowser(String url) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                url = "file://" + url.replaceAll("\\\\", "/");
                Desktop.getDesktop().browse(new URI(url));
                return true;
            } catch (URISyntaxException|IOException ex) {
                Logger.getLogger(DesktopBrowser.class.getName()).log(Level.SEVERE, "Failed to open browser", ex);
            }

        }
        return false;
    }
}
