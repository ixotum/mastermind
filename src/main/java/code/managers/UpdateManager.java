package code.managers;

import code.Defines;
import code.ftp.FtpManager;
import code.ftp.FtpSettings;
import code.utils.LoggerManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by ixotum on 6/29/15
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
        if (isSecondVersionNewerThanFirst(Defines.APP_VERSION, newestVer)) {
            newVersionNumber = newestVer;
            return true;
        }

        return false;
    }

    private static String getNewestVersion(List<String> fileList) {
        String newerVersion = "0.0.0";

        for (String fileName : fileList) {
            String checkVersion = getAppVersion(fileName);

            if (isSecondVersionNewerThanFirst(newerVersion, checkVersion)) {
                newerVersion = checkVersion;
            }
        }

        return newerVersion;
    }

    private static boolean isSecondVersionNewerThanFirst(String firstVer, String secondVer) {
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

    public String getNewVersion() {
        return newVersionNumber;
    }

    public void updateToNewest() {
        if (Defines.APP_VERSION.equals(newVersionNumber)) {
            return;
        }

        if (ftpManager == null) {
            initFtpManager();
        }

        if (!ftpManager.connect()) {
            return;
        }

        String localPath = retrieveFile(ftpManager, newVersionNumber, Defines.APP_DIR_PATH);
        ftpManager.disconnect();

        if (localPath == null) {
            return;
        }

        overWriteOldAndRestartApp();
    }

    private static void overWriteOldAndRestartApp() {
        final ProcessBuilder builder = new ProcessBuilder("./update_app.sh");
        try {
            builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    private static String retrieveFile(FtpManager ftpManager, String versionNumber, String appDirPath) {
        String fileName = Defines.APP_NAME + "-" + versionNumber + "." + Defines.APP_ARCH_EXTENSION;
        String localPath = appDirPath + fileName;

        try {
            OutputStream outputStream = new FileOutputStream(localPath);
            ftpManager.retrieveFile(fileName, outputStream);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return localPath;
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
