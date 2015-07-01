package code;

import code.ftp.FtpManager;
import code.ftp.FtpSettings;
import code.utils.LoggerManager;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by ixotum on 6/29/15.
 */
public class UpdateManager {
    private final Logger logger = LoggerManager.getLoggerInstance();
    private FtpManager ftpManager;
    private String newVersionNumber;

    public boolean isNewVersionAvailable() {
        if (ftpManager == null) {
            initFtpManager();
        }

        if (!ftpManager.connect()) {
            logger.warning("Can't connect to FTP server");
            return false;
        }

        List<String> foundFiles = ftpManager.searchFilesByTemplate(Defines.APP_NAME);
        ftpManager.disconnect();

        if (foundFiles.isEmpty()) {
            return false;
        }

        String newestVer = getNewestVersion(foundFiles);
        if (isSecondNewerThanFirst(Defines.APP_VERSION, newestVer)) {
            newVersionNumber = newestVer;
            return true;
        }

        return false;
    }

    private static String getNewestVersion(List<String> fileList) {
        String newerVersion = "0.0.0";

        for (String fileName : fileList) {
            String checkVersion = getAppVersion(fileName);

            if (isSecondNewerThanFirst(newerVersion, checkVersion)) {
                newerVersion = checkVersion;
            }
        }

        return newerVersion;
    }

    private static boolean isSecondNewerThanFirst(String firstVer, String secondVer) {
        VersionApp firstVersion = new VersionApp(firstVer);
        VersionApp secondVersion = new VersionApp(secondVer);

        if (secondVersion.getRelease() > firstVersion.getRelease()) {
            return true;
        } else if (secondVersion.getMajor() > firstVersion.getMajor()) {
            return true;
        } else if (secondVersion.getMinor() > firstVersion.getMinor()) {
            return true;
        }

        return false;
    }

    private static String getAppVersion(String fileName) {
        String prefix = Defines.APP_NAME + "-";
        String suffix = "." + Defines.APP_ARCH_EXTENSION;
        int startIndex = prefix.length();
        int endIndex = fileName.length() - suffix.length();
        return fileName.substring(startIndex, endIndex);
    }

    private void initFtpManager() {
        FtpSettings ftpSettings = new FtpSettings();
        ftpSettings.setHost(Defines.FTP_HOST);
        ftpSettings.setName(Defines.FTP_NAME);
        ftpSettings.setPassword(Defines.FTP_PASSWORD);

        ftpManager = new FtpManager(ftpSettings);
    }

    private static class VersionApp {
        private int release;
        private int major;
        private int minor;

        public VersionApp(String version) {
            parseString(version);
        }

        private void parseString(String version) {
            int startIndex = 0;
            int endIndex = version.indexOf(".", startIndex);
            release = Integer.parseInt(version.substring(startIndex, endIndex));

            startIndex = endIndex + 1;
            endIndex = version.indexOf(".", startIndex);
            major = Integer.parseInt(version.substring(startIndex, endIndex));

            startIndex = endIndex + 1;
            endIndex = version.length();
            minor = Integer.parseInt(version.substring(startIndex, endIndex));
        }

        public int getRelease() {
            return release;
        }

        public int getMajor() {
            return major;
        }

        public int getMinor() {
            return minor;
        }
    }
}
