package code.utils;

import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UITools {
    public static void initDatePicker(DatePicker datePicker) {
        StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
            String pattern = "dd/MM/yyyy";
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
}
