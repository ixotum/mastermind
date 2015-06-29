package code.utils;

import code.Defines;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

/**
 * Created by ixotum on 6/29/15.
 */
public class LoggerManager {
    public static Logger getLogger() {
        Logger logger = Logger.getLogger(Defines.APP_NAME);
        logger.setUseParentHandlers(false);

        CustomFormatter formatter = new CustomFormatter();
        ConsoleHandler handler = new ConsoleHandler();
//        FileHandler handler = new FileHandler("file.log");
        handler.setFormatter(formatter);
        logger.addHandler(handler);

        return logger;
    }

    private static class CustomFormatter extends Formatter {
        private static final DateFormat df = new SimpleDateFormat("yy/MM/dd hh:mm:ss.SSS");

        @Override
        public String format(LogRecord record) {
            StringBuilder builder = new StringBuilder();
            builder.append(df.format(new Date(record.getMillis()))).append(" ");
            builder.append("[").append(record.getLevel()).append("] ");
            builder.append(record.getSourceClassName()).append(".");
            builder.append(record.getSourceMethodName()).append(": ");
            builder.append(record.getMessage());
            builder.append("\n");

            return builder.toString();
        }
    }
}
