/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.util;

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

    public static void openLinkInBrowser(String url) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI(url.replaceAll("\\\\", "/")));
            } catch (URISyntaxException | IOException ex) {
                Logger.getLogger(DesktopBrowser.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}
