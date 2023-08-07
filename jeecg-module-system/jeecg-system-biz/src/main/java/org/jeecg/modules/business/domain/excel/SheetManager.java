package org.jeecg.modules.business.domain.excel;

import lombok.Data;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.Date;

/**
 * Provide operation that manipulates Excel sheet driven by cursor.
 * The cursor's position is in the (0, 0) at the beginning.
 */
@Data
public class SheetManager {

    private final Workbook workbook;

    private final Sheet detailSheet;

    private final Sheet savSheet;

    private Sheet currentSheet;

    private int currentRow;

    private int currentCol;

    private int max_col;

    private SheetManager(Workbook workbook, Sheet detailSheet, Sheet savSheet) {
        this.workbook = workbook;
        this.detailSheet = detailSheet;
        this.savSheet = savSheet;
        this.currentRow = 0;
        this.currentCol = 0;
        max_col = 10;
    }

    /**
     * Create a manager for the first worksheet of an empty ".xlsx" Excel file.
     * The created worksheet named "sheet1".
     *
     * @return the sheetManager instance.
     */
    public static SheetManager createXLSX() {
        return createXLSX("Détails", "SAV");
    }

    /**
     * Same as {@code createXLSX()}, with customer sheet name.
     *
     * @param detailSheetName Details sheet name
     * @param savSheetName SAV sheet name
     * @return the sheetManager object.
     */
    public static SheetManager createXLSX(String detailSheetName, String savSheetName) {
        Workbook workbook = new XSSFWorkbook();
        Sheet detailSheet = workbook.createSheet(detailSheetName);
        Sheet savSheet = workbook.createSheet(savSheetName);
        return new SheetManager(workbook, detailSheet, savSheet);
    }

    public void startDetailsSheet() {
        this.currentSheet = detailSheet;
    }

    public void startSavSheet() {
        this.currentSheet = savSheet;
        this.currentRow = 0;
        this.currentCol = 0;
    }

    /**
     * Same as {@code readXLS} but for ".xlsx" Excel workbook.
     *
     * @param path       path of the workbook.
     * @return the sheet manager object
     * @throws IOException any error while opening the workbook.
     */
    public static SheetManager readXLSX(Path path) throws IOException {
        FileInputStream fis = new FileInputStream(path.toFile());
        Workbook workbook = new XSSFWorkbook(fis);
        return new SheetManager(workbook, workbook.getSheetAt(0), workbook.getSheetAt(1));
    }

    /**
     * Move the cursor to a specific location.
     * <b>Attention, row and col begin from 0.</b>
     *
     * @param row row index of the location
     * @param col col index of the location
     */
    public void go(int row, int col) {
        this.currentRow = row;
        _moveCol(col);
    }

    public void moveRow(int row) {
        this.currentRow = row;
    }

    public void moveCol(int col) {
        _moveCol(col);
    }

    public int row() {
        return currentRow;
    }

    public int col() {
        return currentCol;
    }

    /**
     * Move the cursor to the bottom cell.
     */
    public void nextRow() {
        this.currentRow += 1;
    }

    /**
     * Move the cursor to the left cell.
     */
    public void nextCol() {
        _moveCol(this.currentCol + 1);
    }

    /**
     * Write a value to the cell pointed by cursor.
     *
     * @param value the value to be written, if value is null then we will change the cell to a Blank cell.
     */
    public void write(String value) {
        cell().setCellValue(value);
    }

    /**
     * Write a value to the cell pointed by cursor.
     *
     * @param value the value to be written, if value is null then we will change the cell to a Blank cell.
     */
    public void write(int value) {
        cell().setCellValue(value);
    }

    /**
     * Write a value to the cell pointed by cursor.
     *
     * @param value the value to be written, if value is null then we will change the cell to a Blank cell.
     */
    public void write(BigDecimal value) {
        cell().setCellValue(value.doubleValue());
    }

    /**
     * Write a value to the cell pointed by cursor.
     *
     * @param value the value to be written
     */
    public void write(Date value) {
        cell().setCellValue(value);
    }

    /**
     * Set current cell's style.
     *
     * @param style the style to set
     */
    public void decorate(CellStyle style) {
        cell().setCellStyle(style);
    }

    /**
     * Write a formula to current cell.
     *
     * @param formula formula to write
     */
    public void writeFormula(String formula) {
        cell().setCellFormula(formula);
    }

    public CellStyle createStyle() {
        return workbook.createCellStyle();
    }

    public Font createFont() {
        return workbook.createFont();
    }


    /**
     * Export the workbook of this sheet to a file.
     *
     * @param target path of the file, it must existe.
     * @throws IOException Any error while opening the file or writing the sheet.
     */
    public void export(Path target) throws IOException {
        /* adjust all cols' width before export */
        for (int i = 0; i < max_col; i++) {
            detailSheet.autoSizeColumn(i);
            savSheet.autoSizeColumn(i);
        }
        FileOutputStream fos = new FileOutputStream(target.toFile());
        workbook.write(fos);
        workbook.close();
    }

    private Cell cell() {

        Row row = currentSheet.getRow(this.currentRow);
        if (row == null) {
            row = currentSheet.createRow(this.currentRow);
        }
        Cell cell = row.getCell(currentCol);
        if (cell == null) {
            cell = row.createCell(currentCol);
        }
        return cell;
    }

    private void _moveCol(int dst) {
        if (dst > max_col) {
            max_col = dst + 5;
        }
        currentCol = dst;
    }


}
