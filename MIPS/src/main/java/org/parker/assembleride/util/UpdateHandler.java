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
package org.parker.assembleride.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.parker.assembleride.core.MIPS;
import org.parker.assembleride.architectures.ArchitecturePluginHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author parke
 */
public class UpdateHandler {

    public static final String LATEST_JSON_LINK = "https://api.github.com/repos/ParkerTenBroeck/MIPS/releases/latest";
    private static String latestVersionLink = "";
    private static boolean isUpToDate = false;
    private static JsonObject latestJsonRequest = null;

    private static final Logger LOGGER = Logger.getLogger(UpdateHandler.class.getName());

    public static void checkForUpdates() {
        try {
            URL url = new URL(LATEST_JSON_LINK);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            InputStream inputStream = http.getInputStream();
            Reader reader = new InputStreamReader(inputStream);


            latestJsonRequest = JsonParser.parseReader(reader).getAsJsonObject();
            String latestTag = latestJsonRequest.get("tag_name").getAsString();

            VersionComparison vc = new VersionComparison(latestTag, MIPS.VERSION);


            if (vc.isV1EqualToV2()) {
                if (vc.v2PreReleaseFlag) {
                    //LogP.logWarning("You are using a PreRelease. Features may contain bugs or unexpected behavior, to upgrade to the latest goto Options>Update");
                    LOGGER.log(Level.WARNING, "You are using a PreRelease. Features may contain bugs or unexpected behavior, to upgrade to the latest goto Options>Update");
                } else {
                    isUpToDate = true;
                }
            } else if (vc.isV1GreaterThanV2()) {
                if (vc.v2PreReleaseFlag) {
                    LOGGER.log(Level.WARNING,"You are using an outdated PreRelease. Features may be outdated and may contain bugs or unexpected behavior, to upgrade to the latest goto Options>Update");
                } else {
                    LOGGER.log(Level.WARNING,"There is a new version available. To upgrade goto Options>Update");
                }
            } else if (vc.isV1LessThanV2()) {
                if (vc.v2PreReleaseFlag) {
                    LOGGER.log(Level.WARNING,"You are using an unreleased PreRelease there may be bugs or unexpected behavior goto. to upgrade to the latest stable version goto Options>Update");
                } else {
                    LOGGER.log(Level.WARNING,"You are using a unreleased full release there may be bugs and unexpected behavior goto Options>Update to get the latest stable release");
                }
            }

            if (vc.v1PreReleaseFlag) {
                LOGGER.log(Level.WARNING,"The latest version is a PreRelease?. This could be a bug please report");
            }

            latestVersionLink = latestJsonRequest.getAsJsonArray("assets").get(0).getAsJsonObject().get("browser_download_url").getAsString();

        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE,"Error While Updating", ex);
        }
    }

    public static void update() {

        String protocol = ResourceHandler.class.getResource("").getProtocol();
        if (!Objects.equals(protocol, "jar")) { //run in jar
            LOGGER.log(Level.SEVERE,"Cannot Update from "+protocol+"? can only update from jar");
            return;
        }
        if (isUpToDate) {
            LOGGER.log(Level.INFO,"Already Up to Date");
            return;
        }

        ArchitecturePluginHandler.requestSystemExit(() -> {
            String[] run = {"java", "-jar", ResourceHandler.DEFAULT_PATH + FileUtils.FILE_SEPARATOR + "updater.jar", MIPS.JAR_PATH, latestVersionLink};
            try {
                Runtime.getRuntime().exec(run);
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Failed to Launch Updater", ex);
            }
        });
        LOGGER.log(Level.INFO, "Update Canceled");
        /*
        if (!EditorHandler.isAllSaved()) {
            int confirm = BaseComputerArchitecture.createWarningQuestion("Exit Confirmation", "You have unsaved work would you like to save before continuing?");

            if (confirm == JOptionPane.CANCEL_OPTION) {
                return;
            }
            if (confirm == JOptionPane.YES_OPTION) {
                EditorHandler.saveAll();
                Preferences.savePreferencesToDefaultFile();
            }
            if (confirm == JOptionPane.NO_OPTION) {
                Preferences.savePreferencesToDefaultFile();
            }
        }
         */
    }

    private static final class VersionComparison {

        public final String v1;
        public final boolean v1PreReleaseFlag;
        public final String v2;
        public final boolean v2PreReleaseFlag;
        public final int comparison;

        public VersionComparison(String v1, String v2) {
            this.v1 = v1;
            this.v2 = v2;

            if (v1.contains("pre_") && v2.contains("pre_")) {
                v1 = v1.replace("pre_", "").replace("_", ".");
                v2 = v2.replace("pre_", "").replace("_", ".");
                v1PreReleaseFlag = true;
                v2PreReleaseFlag = true;
            } else if (v1.contains("pre_") && !v2.contains("_pre")) { // if only one of the releases are pre prerelease then get rid of the pre release number and only compare versions
                v1 = v1.replace("pre_", "").split("_")[0];
                v1PreReleaseFlag = true;
                v2PreReleaseFlag = false;
            } else if (v2.contains("pre_") && !v1.contains("_pre")) {
                v2 = v2.replace("pre_", "").split("_")[0];
                v1PreReleaseFlag = false;
                v2PreReleaseFlag = true;
            } else {
                v1PreReleaseFlag = false;
                v2PreReleaseFlag = false;
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
                if (v1PlaceValue != v2PlaceValue) {
                    tempCom = Integer.compare(v1PlaceValue, v2PlaceValue); //return Integer.compare(v1PlaceValue, v2PlaceValue);
                    break;
                }
            }
            this.comparison = tempCom;

        }

        public boolean isV1EqualToV2() {
            return this.comparison == 0;
        }

        public boolean isV1GreaterThanV2() {
            return this.comparison > 0;
        }

        public boolean isV1LessThanV2() {
            return this.comparison < 0;
        }
    }

}
