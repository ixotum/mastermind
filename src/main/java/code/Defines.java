package code;

/**
 * Created by ixotum on 6/28/15.
 */
public class Defines {
    public static final String APP_NAME = "mastermind";
    public static final String APP_VERSION = "0.0.0";
    public static final String APP_FULL_NAME = APP_NAME + "-" + APP_VERSION;
    public static final String APP_ARCH_EXTENSION = "tar.gz";
    public static final String VERSION_FILE_NAME = "app_full_name";
    public static final String APP_DIR_PATH = "./";

    public static final String FTP_HOST = "192.168.2.5";
    public static final String FTP_NAME = "anonymous";
    public static final String FTP_PASSWORD = "";

    public static final String UPDATE_APP_TITLE = "Application Updating";
    public static final String UPDATE_HEADER_TEXT = "New version of " + APP_NAME + " available";
    public static final String UPDATE_CONTENT_TEXT = "Would you like to update?";

    public enum ApplicationMode {GENERAL, VERSION}
}
