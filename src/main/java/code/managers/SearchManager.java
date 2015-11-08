package code.managers;

import java.util.List;
import java.util.stream.Collectors;

import code.db.order.OrderDB;

/**
 * Created by ixotum on 08.11.15
 */
public class SearchManager {
    public static List<OrderDB> search(String searchText, List<OrderDB> inputOrders) {
        List<OrderDB> outputOrders = inputOrders;

        outputOrders = outputOrders.stream().filter(orderDB -> isContainText(searchText, orderDB)).collect(Collectors.toList());

        return outputOrders;
    }

    private static boolean isContainText(String searchText, OrderDB orderDB) {
        if (orderDB.getName().contains(searchText)) {
            return true;
        } else if (orderDB.getCustomer().contains(searchText)) {
            return true;
        } else if (orderDB.getAddress().contains(searchText)) {
            return true;
        } else if (orderDB.getDescription().contains(searchText)) {
            return true;
        } else if (orderDB.getNotes().contains(searchText)) {
            return true;
        }

        return false;
    }
}
