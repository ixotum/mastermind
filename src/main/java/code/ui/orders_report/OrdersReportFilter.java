package code.ui.orders_report;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ixotum on 07.11.15
 */
public class OrdersReportFilter {
    private LocalDate showFrom;
    private LocalDate showTo;

    public void initDates(LocalDate showFrom, LocalDate showTo) {
        this.showFrom = showFrom;
        this.showTo = showTo;
    }

    public List<OrdersReportRowData> filter(List<OrdersReportRowData> inputList) {
        List<OrdersReportRowData> outputList = inputList;

        if (showFrom == null || showTo == null) {
            return outputList;
        }

        outputList = outputList.stream().filter(this::isInsideDateInterval).collect(Collectors.toList());

        return outputList;
    }

    private boolean isInsideDateInterval(OrdersReportRowData rowData) {
        LocalDate localDate = LocalDate.parse(rowData.getDueDate());

        return (localDate.isAfter(showFrom) || localDate.isEqual(showFrom)) &&
                (localDate.isBefore(showTo) || localDate.isEqual(showTo));
    }
}
