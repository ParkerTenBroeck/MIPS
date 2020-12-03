/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
    private static JsonObject latestJsonRequest = null;

    public static void checkForUpdates() {
        try {
            URL url = new URL(LATEST_JSON_LINK);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            InputStream inputStream = http.getInputStream();
            Reader reader = new InputStreamReader(inputStream);

            JsonParser parser = new JsonParser();

            latestJsonRequest = parser.parse(reader).getAsJsonObject();
            String latestTag = latestJsonRequest.get("tag_name").getAsString();

            VersionComparison vc = new VersionComparison(latestTag, MIPS.VERSION);

//            if(vc.v2PreReleseFlag){
//                Log.logWarning("You are using a PreRelese there may be bugs or unexpected behavior");
//            }
            if (vc.isV1EqualToV2()) {
                if (vc.v2PreReleseFlag) {
                    Log.logWarning("You are using a PreRelese. Features may contain bugs or unexpected behavior, to upgrade to the latest goto Options>Update");
                } else {
                    isUpToDate = true;
                }
            } else if (vc.isV1GreaterThanV2()) {
                if (vc.v2PreReleseFlag) {
                    Log.logWarning("You are using an outdated PreRelese. Features may be outdated and may contain bugs or unexpected behavior, to upgrade to the latest goto Options>Update");
                } else {
                    Log.logWarning("There is a new Version available. To upgrade goto Options>Update");
                }
            } else if (vc.isV1LessThanV2()) {
                if (vc.v2PreReleseFlag) {
                    Log.logWarning("You are using an unrelesed PreRelese there may be bugs or unexpected behavior goto. to upgrade to the latest stable version goto Options>Update");
                } else {
                    Log.logWarning("You are using a inrelesed full relese there may be bugs and unexpected behavior goto Options>Update to get the latest stable relese");
                }
            }

            if (vc.v1PreReleseFlag) {
                Log.logWarning("The latest version is a PreReslese?. This could be a bug please report");
            }

            latestVersionLink = latestJsonRequest.getAsJsonArray("assets").get(0).getAsJsonObject().get("browser_download_url").getAsString();

            //System.out.println(sb.toString());
            //return sb.toString();
        } catch (MalformedURLException ex) {
            Logger.getLogger(MIPS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MIPS.class.getName()).log(Level.SEVERE, null, ex);
        }
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

        String[] run = {"java", "-jar", ResourceHandler.DEFAULT_PATH + ResourceHandler.FILE_SEPERATOR + "updater.jar", MIPS.JAR_PATH, latestVersionLink};
        try {
            Runtime.getRuntime().exec(run);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.exit(0);

        //return false;
    }

    private static final class VersionComparison {

        public final String v1;
        public final boolean v1PreReleseFlag;
        public final String v2;
        public final boolean v2PreReleseFlag;
        public final int comparasin;

        public VersionComparison(final String v1, boolean v1PreReleseFlag, final String v2, boolean v2PreReleseFlag, int comparasin) {
            this.v1 = v1;
            this.v1PreReleseFlag = v1PreReleseFlag;
            this.v2 = v2;
            this.v2PreReleseFlag = v2PreReleseFlag;
            this.comparasin = comparasin;
        }

        public VersionComparison(String v1, String v2) {
            this.v1 = v1;
            this.v2 = v2;

            if (v1.contains("pre_") && v2.contains("pre_")) {
                v1 = v1.replace("pre_", "").replace("_", ".");
                v2 = v2.replace("pre_", "").replace("_", ".");
                v1PreReleseFlag = true;
                v2PreReleseFlag = true;
            } else if (v1.contains("pre_") && !v2.contains("_pre")) { // if only one of the releses are pre preselese then get rid of the pre relese number and only compair versions
                v1 = v1.replace("pre_", "").split("_")[0];
                v1PreReleseFlag = true;
                v2PreReleseFlag = false;
            } else if (v2.contains("pre_") && !v1.contains("_pre")) {
                v2 = v2.replace("pre_", "").split("_")[0];
                v1PreReleseFlag = false;
                v2PreReleseFlag = true;
            } else {
                v1PreReleseFlag = false;
                v2PreReleseFlag = false;
            }

            String[] v1a = v1.split("\\.");
            String[] v2a = v2.split("\\.");
            int length = Math.max(v1a.length, v2a.length);

            int tempCom = 0;

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
                    if (tempCom == 0) {
                        tempCom = Integer.compare(v1PlaceValue, v2PlaceValue); //return Integer.compare(v1PlaceValue, v2PlaceValue);
                    }
                    break;
                }
            }
            if (tempCom != 0) {
                this.comparasin = tempCom;
            } else {
                this.comparasin = 0;
            }

        }

        public boolean isV1EqualToV2() {
            return this.comparasin == 0;
        }

        public boolean isV1GreaterThanV2() {
            return this.comparasin > 0;
        }

        public boolean isV1LessThanV2() {
            return this.comparasin < 0;
        }
    }

    public static int compareVersions(final String v1, final String v2) {

        boolean v1PreReleseFlag = false;
        boolean v2PreReleseFlag = false;

        String tempV1 = v1;
        String tempV2 = v2;

        if (v1.contains("pre_") && v2.contains("pre_")) {
            tempV1 = v1.replace("pre_", "").replace("_", ".");
            tempV2 = v2.replace("pre_", "").replace("_", ".");
            v1PreReleseFlag = true;
            v2PreReleseFlag = true;
        } else {
            if (v1.contains("pre_") && !v2.contains("_pre")) { // if only one of the releses are pre preselese then get rid of the pre relese number and only compair versions
                tempV1 = v1.replace("pre_", "").split("_")[0];
                v1PreReleseFlag = true;
            }
            if (v2.contains("pre_") && !v1.contains("_pre")) {
                tempV2 = v2.replace("pre_", "").split("_")[0];
                v2PreReleseFlag = true;
            }
        }

        String[] v1a = tempV1.split("\\.");
        String[] v2a = tempV2.split("\\.");
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
