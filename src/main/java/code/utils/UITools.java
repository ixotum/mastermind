package code.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;
import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

import code.Defines;

public class UITools {
    private static final Logger logger = LoggerManager.getLoggerInstance();

    public static void initDatePicker(DatePicker datePicker) {
        StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
            String pattern = "yyyy/MM/dd";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };
        datePicker.setConverter(converter);
    }

    public static boolean isCurrency(String value) {
        if (value.isEmpty()) {
            return true;
        }

        BigDecimal bigDecimal;
        try {
            bigDecimal = new BigDecimal(value);
        } catch (NumberFormatException e) {
            return false;
        }

        BigDecimal fraction = bigDecimal.remainder(BigDecimal.ONE);
        String fractionStr = fraction.toString();
        fractionStr = fractionStr.replace("0.", "");

        return fractionStr.length() <= 2;
    }

    public static LocalDate convertToLocalDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return LocalDate.of(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH));
    }

    public static String getPaymentStatus(BigDecimal total, BigDecimal due) {
        String status = "UNPAID";

        if (due.compareTo(total) < 0 && due.compareTo(BigDecimal.ZERO) > 0) {
            status = "PARTIALLY PAID";
        } else if (due.compareTo(BigDecimal.ZERO) == 0) {
            status = "PAID";
        } else if (due.compareTo(BigDecimal.ZERO) < 0) {
            status = "OVERPAID";
        }

        return status;
    }

    public static String createBackupDbFileName() {
        String todayDate = LocalDate.now().toString();
        String dbName = getDbFileName();
        return todayDate + "-" + dbName;
    }

    public static String getDbFileName() {
        return Defines.APP_NAME + "." + Defines.DB_FILE_EXTENSION;
    }

    public static boolean copyFile(String source, String target) {
        Path pathSource = FileSystems.getDefault().getPath(source);
        Path pathTarget = FileSystems.getDefault().getPath(target);

        File fileSource = pathSource.toFile();
        String sourceFullFileName = fileSource.getAbsolutePath();


        if (!fileSource.exists()) {
            logger.info("Can not find source file: " + sourceFullFileName);
            return false;
        }

        try {
            Files.copy(pathSource, pathTarget, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static void removeOldestDbFiles() {
        Path startPath = FileSystems.getDefault().getPath(".");
        String dbFileName = getDbFileName();
        Stream<Path> backupDbFiles = findFiles(startPath, dbFileName);

        assert backupDbFiles != null;
        List<Path> sortedFiles = backupDbFiles.sorted(Path::compareTo).collect(Collectors.toList());

        int foundBackupsCount = sortedFiles.size();
        int backupLimit = Defines.BACKUP_LIMIT;
        if (foundBackupsCount <= backupLimit) {
            return;
        }

        int filesToRemoveCount = foundBackupsCount - backupLimit;
        for (int fileIndex = 0; fileIndex < filesToRemoveCount; ++fileIndex) {
            removeFile(sortedFiles.get(fileIndex));
        }
    }

    private static void removeFile(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Stream<Path> findFiles(Path path, String template) {
        Stream<Path> foundFiles;

        try {
            foundFiles = Files.find(path, 1, getMatcher(template));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return foundFiles;
    }

    private static BiPredicate<Path, BasicFileAttributes> getMatcher(String dbFileName) {
        return (path, basicFileAttributes) -> path.toString().contains(dbFileName);
    }

    public static BufferedImage scaleImage(File imageFile, int scaleSize) {
        BufferedImage outputImage = null;

        try {
            BufferedImage inputImage = ImageIO.read(imageFile);
            outputImage = Scalr.resize(inputImage, scaleSize);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return outputImage;
    }

    public static String createEpochFileName(String suffix) {
        return System.currentTimeMillis() + suffix;
    }

    public static void saveFileToMediaStorage(BufferedImage image, String fileName) {
        File dir = new File(Defines.MEDIA_STORAGE + "/");
        if (!dir.exists()) {
            if (!dir.mkdir()) {
                return;
            }
        }

        String path = Defines.MEDIA_STORAGE + "/" + fileName;
        File outputFile = new File(path);
        try {
            ImageIO.write(image, "jpg", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String findFile(String fileName) {
        Path start = Paths.get(Defines.MEDIA_STORAGE);
        Stream<Path> foundFiles = null;
        String foundFile = null;

        try {
            foundFiles = Files.find(start, 1, (path, attr) -> filePredicate(path, fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (foundFiles != null) {
            foundFile = foundFiles.findFirst().get().toFile().getAbsolutePath();
        }

        return foundFile;
    }

    private static boolean filePredicate(Path path, String fileName) {
        return path.toString().contains(fileName);
    }
}
