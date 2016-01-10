package code;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ixotum on 6/28/15
 */
public class Defines {
    public static final String APP_NAME = "mastermind";
    public static final String APP_VERSION = "0.0.0";
    public static final String APP_FULL_NAME = APP_NAME + "-" + APP_VERSION;
    public static final String APP_ARCH_EXTENSION = "tar.gz";
    public static final String DB_FILE_EXTENSION = "mv.db";
    public static final String VERSION_FILE_NAME = "app_full_name";
    public static final String APP_DIR_PATH = "./";
    public static final int BACKUP_LIMIT = 5;

    public static final String FTP_HOST = "192.168.2.4";
    public static final String FTP_NAME = "anonymous";
    public static final String FTP_PASSWORD = "";

    public static final String UPDATE_APP_TITLE = "Application Updating";
    public static final String UPDATE_HEADER_TEXT = "New version of " + APP_NAME + " available";
    public static final String UPDATE_CONTENT_TEXT = "Would you like to update?";
    public static final String BEANS_CONFIG = "Beans.xml";
    public static final int SCALE_IMAGE = 1024;
    public static final int SCALE_THUMBNAIL = 100;
    public static final String MEDIA_STORAGE = "media";

    public enum ApplicationMode {GENERAL, VERSION}

    public static List<String> orderStatuses = Arrays.asList("NEW", "IN PROGRESS", "COMPLETED", "SHIPPED", "CLOSED", "CANCELED");
    public static List<String> expenseTypes = Arrays.asList("MATERIALS", "PRINTING", "PROMOTION", "TOOL", "OTHER");
}
