package code.ui.models;

import code.bus.BusEvent;
import code.bus.BusEventListener;
import code.bus.BusEventManager;
import code.bus.BusEventType;
import code.ui.EditOrderScreenController;

/**
 * Created by ixotum on 15.10.15
 */
public class EditOrderScreenModel implements BusEventListener {
    private final EditOrderScreenController controller;

    public EditOrderScreenModel(EditOrderScreenController editOrderScreenController) {
        this.controller = editOrderScreenController;
    }

    public void initListeners() {
        BusEventManager.addListener(this, BusEventType.ESC_PRESSED);
    }

    @Override
    public void busEventDispatched(BusEvent busEvent) {
        BusEventType busEventType = busEvent.getType();
        if (busEventType == BusEventType.ESC_PRESSED) {
            controller.hide();
        }
    }
}
