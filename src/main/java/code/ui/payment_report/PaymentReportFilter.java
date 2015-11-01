package code.ui.payment_report;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ixotum on 31.10.15
 */
public class PaymentReportFilter {
    private LocalDate showFrom;
    private LocalDate showTo;

    public void initPeriod(String period) {
        showTo = LocalDate.now();

        switch (period) {
            case "Month" :
                showFrom = showTo.withDayOfMonth(1);
                break;
            case "Year" :
                showFrom = showTo.withDayOfYear(1);
                break;
            default:
                showFrom = showTo = null;
        }
    }

    public List<PaymentReportRowData> filter(List<PaymentReportRowData> inputList) {
        List<PaymentReportRowData> outputList = inputList;

        if (showFrom == null || showTo == null) {
            return outputList;
        }

        outputList = outputList.stream().filter(this::isInsideDateInterval).collect(Collectors.toList());

        return outputList;
    }

    private boolean isInsideDateInterval(PaymentReportRowData rowData) {
        LocalDate localDate = LocalDate.parse(rowData.getDate());

        return (localDate.isAfter(showFrom) || localDate.isEqual(showFrom)) &&
               (localDate.isBefore(showTo) || localDate.isEqual(showTo));
    }

    public void setDates(LocalDate localDateFrom, LocalDate localDateTo) {
        showFrom = localDateFrom;
        showTo = localDateTo;
    }
}
