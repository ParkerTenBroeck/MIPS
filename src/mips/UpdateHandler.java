/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mips;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author parke
 */
public class UpdateHandler {

    public static final String LATEST_JSON_LINK = "https://api.github.com/repos/ParkerTenBroeck/MIPS/releases/latest";
    private static String latestVersionLink = "";
    private static boolean isUpToDate = false;

    public static String checkForUpdates() {
        try {
            URL url = new URL(LATEST_JSON_LINK);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            InputStream inputStream = http.getInputStream();

            int ch;
            StringBuilder sb = new StringBuilder();
            while ((ch = inputStream.read()) != -1) {
                sb.append((char) ch);
            }
            String JSON = sb.toString();

            latestVersionLink = getValueOfJSONTag("tag_name", JSON);

            int compare = compareVersions(latestVersionLink, MIPS.VERSION);

            if (compare == 0) {
                isUpToDate = true;
            } else if (compare > 0) {
                Log.logWarning("There is an update avalible goto Options>Update to update");
                latestVersionLink = getValueOfJSONTag("browser_download_url", JSON);
            } else if (compare < 0) {
                Log.logWarning("You are using a Beta Version that has not been relesed yet there may be un");
                latestVersionLink = getValueOfJSONTag("browser_download_url", JSON);
            } else {

            }

            //System.out.println(sb.toString());
            return sb.toString();

        } catch (MalformedURLException ex) {
            Logger.getLogger(MIPS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MIPS.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static void update() {

        String protocol = ResourceHandler.class.getResource("").getProtocol();
        if (!Objects.equals(protocol, "jar")) { //run in jar
            Log.logError("Cannot Update from IDE? can only update from JAR");
            return;
        }
        if (isUpToDate) {
            Log.logMessage("Already Up to Date");
            return;
        }

        if (!FileHandler.isASMFileSaved()) {
            int confirm = JOptionPane.showOptionDialog(
                    null, "you have unsaved work are you sure you want to continue?",
                    "Exit Confirmation", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (confirm == 0) {
            } else {
                Log.logMessage("Update Cancelled");
                return;
            }
        } else {
            int confirm = JOptionPane.showOptionDialog(
                    null, "are you sure you want to continue?",
                    "Exit Confirmation", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (confirm == 0) {
            } else {
                Log.logMessage("Update Cancelled");
                return;
            }
        }

        String[] run = {"java", "-jar", ResourceHandler.DEFAULT_PATH + "\\updater.jar", MIPS.JAR_PATH, latestVersionLink};
        try {
            Runtime.getRuntime().exec(run);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.exit(0);

        //return false;
    }

    public static String getValueOfJSONTag(String tag, String JSON) {

        tag = "\"" + tag + "\":";

        int startIndex = JSON.indexOf(tag) + tag.length();
        int endIndex = JSON.indexOf(",", startIndex);

        return JSON.substring(startIndex, endIndex).replaceAll("\"", "").replaceAll("\'", "").trim();
    }

    public static int compareVersions(String v1, String v2) {
        String[] v1a = v1.split("\\.");
        String[] v2a = v2.split("\\.");
        int length = Math.max(v1a.length, v2a.length);

        for (int i = 0; i < length; i++) {

            int v1PlaceValue;
            int v2PlaceValue;

            try {
                v1PlaceValue = Integer.parseInt(v1a[i]);
            } catch (Exception e) {
                v1PlaceValue = 0;
            }

            try {
                v2PlaceValue = Integer.parseInt(v2a[i]);
            } catch (Exception e) {
                v2PlaceValue = 0;
            }
            if (Integer.compare(v1PlaceValue, v2PlaceValue) == 0) {

            } else {
                return Integer.compare(v1PlaceValue, v2PlaceValue);
            }
        }
        return 0;
    }
}
