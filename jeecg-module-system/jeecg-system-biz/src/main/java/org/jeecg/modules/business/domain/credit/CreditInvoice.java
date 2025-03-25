package org.jeecg.modules.business.domain.credit;

import lombok.Getter;
import org.apache.poi.ss.usermodel.*;
import org.jeecg.modules.business.domain.invoice.AbstractInvoice;
import org.jeecg.modules.business.domain.invoice.InvoiceStyleFactory;
import org.jeecg.modules.business.domain.invoice.Row;
import org.jeecg.modules.business.entity.Client;
import org.jeecg.modules.business.entity.Credit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CreditInvoice extends AbstractInvoice<String, Object, Integer, Object, BigDecimal> {

    private final BigDecimal balance;
    private final Credit credit;
    @Getter
    private final String currency;

    public CreditInvoice(Client client, Credit credit, String subject, BigDecimal exchangeRate, BigDecimal balance, String currency) {
        super(client, credit.getInvoiceNumber(), subject, exchangeRate);
        this.credit = credit;
        this.balance = balance;
        this.currency = currency;
    }

    private final static String BALANCE_LOCATION_EUR = "H23";
    private final static String BALANCE_LOCATION_USD = "H24";


    @Override
    protected void fillInformation(InvoiceStyleFactory factory) {
        super.fillInformation(factory);
        // fill balance
        CellStyle cellStyle = factory.getWorkbook().createCellStyle();
        DataFormat format = factory.getWorkbook().createDataFormat();
        Font arial = factory.getWorkbook().createFont();
        arial.setFontName("Arial");
        arial.setFontHeightInPoints((short) 12);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.RIGHT);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setFont(arial);
        cellStyle.setDataFormat(format.getFormat("#,##0.00")); // to get decimal format eg : "1234,56" and not "1234,5678" by default

        String cellLocation = currency.equals("USD") ? BALANCE_LOCATION_USD : BALANCE_LOCATION_EUR;
        configCell(cellLocation, balance, cellStyle);
    }
    @Override
    protected List<Row<String, Object, Integer, Object, BigDecimal>> tableData() {
        List<Row<String, Object, Integer, Object, BigDecimal>> rows = new ArrayList<>();
        Row<String, Object, Integer, Object, BigDecimal> row = new Row<>(
                credit.getDescription().isEmpty() ? "Credit" : credit.getDescription(),
                null,
                null,
                null,
                credit.getAmount()
        );
        rows.add(row);
        return rows;
    }
    @Override
    protected void fillTable(InvoiceStyleFactory factory) {
        Row<String, Object, Integer, Object, BigDecimal> data = tableData().get(0);
        int lineNum;
        Row<String, Object, Integer, Object, BigDecimal> rowValue;
        Sheet sheet = factory.getWorkbook().getSheetAt(0);

        lineNum = FIRST_ROW;
        rowValue = data;
//            log.info("Writing line {} with data {}", lineNum, rowValue);
        configCell("C", lineNum, String.format("%06d", 1), factory.fullBorderLeftStyle());
        configCell("D", lineNum, rowValue.getCol1(), factory.fullBorderLeftStyle());
        CellStyle creditAmountStyle = factory.fullBorderRightStyle();
        CreationHelper creationHelper = workbook.getCreationHelper();
        creditAmountStyle.setDataFormat(creationHelper.createDataFormat().getFormat("#,##0.00"));
        configCell("H", lineNum, rowValue.getCol5(), creditAmountStyle);

        if (currency.equals("USD")) {
            org.apache.poi.ss.usermodel.Row euroRow;
            euroRow = sheet.getRow(FIRST_ROW + 2);
            String formula = "H"+ (FIRST_ROW + 2) +" /" + exchangeRate;
            Cell dollarCell = euroRow.createCell(7); // column H
            CellStyle cellStyle = factory.getWorkbook().createCellStyle();
            DataFormat format = factory.getWorkbook().createDataFormat();
            Font arial = factory.getWorkbook().createFont();
            arial.setFontName("Arial");
            arial.setFontHeightInPoints((short) 12);
            cellStyle.setBorderRight(BorderStyle.THIN);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setAlignment(HorizontalAlignment.RIGHT);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            cellStyle.setFont(arial);
            cellStyle.setDataFormat(format.getFormat("#,##0.00")); // to get decimal format eg : "1234,56" and not "1234,5678" by default
            dollarCell.setCellStyle(cellStyle);
            euroRow.getCell(7).setCellFormula(formula);
        }
    }
}
