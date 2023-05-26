package org.jeecg.modules.business.domain.api.mabang.getorderlist;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.jeecg.modules.business.domain.excel.SheetManager;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class OrderExcel {
    private final List<Order> data;

    private final SheetManager sheetManager;


    private final static String[] titles = {
            "Shop ERP Code",
            "Logistic Channel Name",
            "Platform Order ID",
            "Platform Order Number",
            "ERP Order ID",
            "Tracking Number",
            "Order Time",
            "Shipping Time",
            "Recipient",
            "Country",
            "Postcode",
            "Status",
            "Is Union",
            "ERP Code",
            "Quantity",
            "Origin Order ID"
    };

    private final CellStyle titleStyle;


    public OrderExcel(List<Order> data) {
        this.data = data;
        this.sheetManager = SheetManager.createXLSX();
        this.titleStyle = this.sheetManager.createStyle();
    }

    public void export(Path target) throws IOException {
        writeTitle();
        sheetManager.go(1, 0);
        writeContent();
        sheetManager.export(target);
    }

    private void writeTitle() {
        initTitleStyle();
        Arrays.stream(titles)
                .forEach(
                        (title) -> {
                            sheetManager.write(title);
                            sheetManager.decorate(this.titleStyle);
                            sheetManager.nextCol();
                        }
                );
    }

    private void writeContent() {
        for (Order order : this.data) {
            sheetManager.go(
                    sheetManager.row() + writeOrder(order),
                    0
            );
        }
    }

    /**
     * Write a order to worksheet, from current cursor position,
     * each attribute possesses a cell.
     *
     * @param order the order to write
     * @return number of line occupied
     */
    private int writeOrder(Order order) {
        /* main */
        sheetManager.write(order.getShopErpCode());
        sheetManager.nextCol();
        sheetManager.write(order.getLogisticChannelName());
        sheetManager.nextCol();
        sheetManager.write(order.getPlatformOrderId());
        sheetManager.nextCol();
        sheetManager.write(order.getPlatformOrderNumber());
        sheetManager.nextCol();
        sheetManager.write(order.getErpOrderId());
        sheetManager.nextCol();
        sheetManager.write(order.getTrackingNumber());
        sheetManager.nextCol();
        sheetManager.write(order.getOrderTime());
        sheetManager.nextCol();
        sheetManager.write(order.getShippingTime());
        sheetManager.nextCol();
        sheetManager.write(order.getRecipient());
        sheetManager.nextCol();
        sheetManager.write(order.getCountry());
        sheetManager.nextCol();
        sheetManager.write(order.getPostcode());
        sheetManager.nextCol();
        sheetManager.write(order.getStatus());
        sheetManager.nextCol();
        sheetManager.write(order.getIsUnion());
        sheetManager.nextCol();

        /* children */
        int startCol = sheetManager.col();
        for (OrderItem item : order.getOrderItems()) {
            writeOrderItem(item);
            sheetManager.nextRow();
            sheetManager.moveCol(startCol);
        }
        return order.getOrderItems().size();
    }

    private void writeOrderItem(OrderItem item) {
        sheetManager.write(item.getErpCode());
        sheetManager.nextCol();
        sheetManager.write(item.getQuantity());
        sheetManager.nextCol();
        sheetManager.write(item.getOriginOrderId());
        sheetManager.nextCol();
    }

    private void initTitleStyle() {
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        Font font = sheetManager.createFont();
        font.setBold(true);
        titleStyle.setFont(font);
    }
}
