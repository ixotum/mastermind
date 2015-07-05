package code.ftp;

import code.utils.LoggerManager;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by ixotum on 6/29/15.
 */
public class FtpManager {
    private final Logger logger = LoggerManager.getLoggerInstance();
    private final FtpSettings ftpSettings;
    private final FTPClient ftpClient;

    public FtpManager(FtpSettings ftpSettings) {
        this.ftpSettings = ftpSettings;
        ftpClient = new FTPClient();
    }

    public boolean connect() {
        String hostName = ftpSettings.getHost();
        logger.info("Trying to connect to host: " + hostName);
        try {
            ftpClient.connect(hostName);

            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                ftpClient.disconnect();
                logger.severe("Ftp Server refused connection. ReplyCode is: " + replyCode);
                return false;
            } else {
                logger.info("Done. Port is: " + ftpClient.getDefaultPort());
            }

            String name = ftpSettings.getName();
            logger.info("Try to login with name: " + name + "...");
            if (ftpClient.login(name, ftpSettings.getPassword())) {
                logger.info("Done. Remote system is: " + ftpClient.getSystemType());
                return true;
            } else {
                logger.info("Could not login!");
                ftpClient.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void disconnect() {
        try {
            ftpClient.logout();
            ftpClient.disconnect();
            logger.info("ftpClient disconnected.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> searchFilesByTemplate(String fileTemplate) {
        if (!ftpClient.isAvailable()) {
            logger.severe("FtpClient is not available!");
            return null;
        }

        FTPFile[] fileList;

        try {
            fileList = ftpClient.listFiles();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        List<String> foundFiles = findFilesByTemplate(fileList, fileTemplate);

        return foundFiles;
    }

    private List<String> findFilesByTemplate(FTPFile[] fileList, String fileTemplate) {
        List<String> correspondedList = new ArrayList<>();

        for (FTPFile ftpFile : fileList) {
            String ftpFileName = ftpFile.getName();

            if (ftpFileName.startsWith(fileTemplate)) {
                correspondedList.add(ftpFileName);
            }
        }

        return correspondedList;
    }

    public void retrieveFile(String remoteFileName, OutputStream outputStream) {
        try {
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            if (ftpClient.retrieveFile(remoteFileName, outputStream)) {
                logger.info("Remote file " + remoteFileName + " is downloaded.");
            }
        } catch (IOException e) {
            logger.severe("Error downloading file " + remoteFileName);
            e.printStackTrace();
        }
    }
}
