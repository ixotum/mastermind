package code.ui.models;

import code.Main;
import code.db.OrderDB;
import code.managers.OrderManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.scene.control.LocalDatePicker;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ixotum on 13.10.15
 */
public class MainScreenModel {
    public static void initCalendar(LocalDatePicker calendar) {
        List<Date> dates = extractDates();
        highlightDates(dates, calendar);
    }

    private static void highlightDates(List<Date> dates, LocalDatePicker calendar) {
        List<LocalDate> localDates = dates.stream().map(MainScreenModel::convertToLocalDate).collect(Collectors.toList());
        ObservableList<LocalDate> obsLocalDates = FXCollections.observableArrayList(localDates);
        calendar.highlightedLocalDates().setAll(obsLocalDates);
    }

    private static LocalDate convertToLocalDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return LocalDate.of(calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH) + 1,
                            calendar.get(Calendar.DAY_OF_MONTH));
    }

    private static List<Date> extractDates() {
        OrderManager orderManager = Main.getOrderManager();
        List<OrderDB> orderDBList = orderManager.getOrders();
        List<Date> dates = new ArrayList<>();

        for (OrderDB orderDB : orderDBList) {
            Date date = orderDB.getDueDate();
            saveDate(date, dates);
            date = orderDB.getEventDate();
            saveDate(date, dates);
        }

        return dates;
    }

    private static void saveDate(Date date, List<Date> dates) {
        if (!dates.contains(date)) {
            dates.add(date);
        }
    }
}
