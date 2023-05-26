package org.jeecg.modules.business.domain.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.Date;

/**
 * Provide operation that manipulate Excel sheet driven by cursor.
 * The cursor's position is in the (0, 0) at the beginning.
 */
public class SheetManager {
    private final Workbook workbook;

    private final Sheet sheet;

    private int row;

    private int col;

    private int max_col;

    private SheetManager(Workbook workbook, Sheet sheet) {
        this.workbook = workbook;
        this.sheet = sheet;
        row = 0;
        col = 0;
        max_col = 10;
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    /**
     * Create a manager for the first worksheet of an empty ".xlsx" Excel file.
     * The created worksheet named "sheet1".
     *
     * @return the sheetManager instance.
     */
    public static SheetManager createXLSX() {
        return createXLSX("sheet1");
    }

    /**
     * Same as {@code createXLSX()}, with customer sheet name.
     *
     * @param name the customer sheet name
     * @return the sheetManager object.
     */
    public static SheetManager createXLSX(String name) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(name);
        return new SheetManager(workbook, sheet);
    }

    /**
     * Create a sheet manager for a sheet of a ".xls" Excel workbook.
     *
     * @param path       path of the workbook.
     * @param sheetIndex index of the sheet, begin from 0
     * @return the sheet manager object
     * @throws IOException any error while opening the workbook.
     */
    public static SheetManager readXLS(Path path, int sheetIndex) throws IOException {
        FileInputStream fis = new FileInputStream(path.toFile());
        Workbook workbook = new HSSFWorkbook(fis);
        return new SheetManager(workbook, workbook.getSheetAt(sheetIndex));
    }

    /**
     * Same as {@code readXLS} but for ".xlsx" Excel workbook.
     *
     * @param path       path of the workbook.
     * @param sheetIndex index of the sheet, begin from 0
     * @return the sheet manager object
     * @throws IOException any error while opening the workbook.
     */
    public static SheetManager readXLSX(Path path, int sheetIndex) throws IOException {
        FileInputStream fis = new FileInputStream(path.toFile());
        Workbook workbook = new XSSFWorkbook(fis);
        return new SheetManager(workbook, workbook.getSheetAt(sheetIndex));
    }

    /**
     * Move the cursor to a specific location.
     * <b>Attention, row and col begin from 0.</b>
     *
     * @param row row index of the location
     * @param col col index of the location
     */
    public void go(int row, int col) {
        this.row = row;
        _moveCol(col);
    }

    public void moveRow(int row) {
        this.row = row;
    }

    public void moveCol(int col) {
        _moveCol(col);
    }

    public int row() {
        return row;
    }

    public int col() {
        return col;
    }

    /**
     * Move cursor to the bottom cell.
     */
    public void nextRow() {
        this.row += 1;
    }

    /**
     * Move cursor to the left cell.
     */
    public void nextCol() {
        _moveCol(this.col + 1);
    }

    /**
     * Write a value to the cell pointed by cursor.
     *
     * @param value the value to be wrote, if value is null then we will change the cell to a Blank cell.
     */
    public void write(String value) {
        cell().setCellValue(value);
    }

    /**
     * Write a value to the cell pointed by cursor.
     *
     * @param value the value to be wrote, if value is null then we will change the cell to a Blank cell.
     */
    public void write(int value) {
        cell().setCellValue(value);
    }

    /**
     * Write a value to the cell pointed by cursor.
     *
     * @param value the value to be wrote, if value is null then we will change the cell to a Blank cell.
     */
    public void write(BigDecimal value) {
        cell().setCellValue(value.doubleValue());
    }

    /**
     * Write a value to the cell pointed by cursor.
     *
     * @param value the value to be wrote
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
            sheet.autoSizeColumn(i);
        }
        FileOutputStream fos = new FileOutputStream(target.toFile());
        workbook.write(fos);
        workbook.close();
    }

    private Cell cell() {

        Row row = sheet.getRow(this.row);
        if (row == null) {
            row = sheet.createRow(this.row);
        }
        Cell cell = row.getCell(col);
        if (cell == null) {
            cell = row.createCell(col);
        }
        return cell;
    }

    private void _moveCol(int dst) {
        if (dst > max_col) {
            max_col = dst + 5;
        }
        col = dst;
    }


}
