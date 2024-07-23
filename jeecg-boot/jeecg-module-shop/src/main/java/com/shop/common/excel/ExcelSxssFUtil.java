package com.shop.common.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lixy
 * @className: ExcelSxssFUtil
 * @Version 1.0
 * @description: TODO 大批量数据导出
 * @Email： 2542130759@qq.com
 * @date 2020/12/16 17:14
 */
public class ExcelSxssFUtil {


    public static <T> void exportExcel(List<Field> fields, SXSSFWorkbook workbook, List<T> list, String sheetTitle) {
        List<String> fns = getFieldName(fields);

        SXSSFSheet sheet = getCommon(workbook, sheetTitle, fields);
        CellStyle cellStyle = getCellStyle(workbook);

        // 时间样式
        CellStyle dateStyle = workbook.createCellStyle();
        dateStyle.cloneStyleFrom(cellStyle);
        DataFormat format = workbook.createDataFormat();
        dateStyle.setDataFormat(format.getFormat("yyyy-MM-dd HH:mm:ss"));

        for (int i = 0; i < list.size(); i++) {
            SXSSFRow row = sheet.createRow(i + 2);
            T item = list.get(i);

            // 通过反射机制获取实体对象的状态
            try {
                final BeanInfo bi = Introspector.getBeanInfo(item.getClass());
                for (final PropertyDescriptor pd : bi.getPropertyDescriptors()) {
                    if (fns.contains(pd.getName())) {
                        Object value = pd.getReadMethod().invoke(item, (Object[]) null);
                        int index = fns.indexOf(pd.getName());
                        SXSSFCell cell = row.createCell(index);
                        if (value != null) {
                            Excel excel = fields.get(index).getAnnotation(Excel.class);
                            // 给单元格赋值
                            if (value instanceof Number) {
                                cell.setCellValue((Double.valueOf(String.valueOf(value))));
                            } else if (value instanceof Date) {
                                cell.setCellValue((Date) value);
                                cell.setCellStyle(dateStyle);
                                continue;
                            } else {
                                cell.setCellValue(String.valueOf(value));
                            }
                        }
                        cell.setCellStyle(cellStyle);
                    }
                }
            } catch (InvocationTargetException | IntrospectionException | IllegalAccessException e) {
                String message = "导入失败：字段名称匹配失败！";
                throw new IllegalArgumentException(message, e);
            }

        }
    }


    /**
     * 获取通用样式
     */
    private static CellStyle getCellStyle(SXSSFWorkbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setFontName("Microsoft YaHei UI");
        cellStyle.setFont(font);
        return cellStyle;
    }

    /**
     * 功能模板（标题及表头）
     */
    public static SXSSFSheet getCommon(SXSSFWorkbook workbook, String sheetTitle, List<Field> fields) {
        SXSSFSheet sheet = workbook.createSheet(sheetTitle);

        // 设置列宽度
        for (int i = 0; i < fields.size(); i++) {
            sheet.setColumnWidth(i, 16 * 256);
        }

        // 通用样式
        CellStyle cellStyle = getCellStyle(workbook);

        // 标题样式
        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.cloneStyleFrom(cellStyle);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        Font titleFont = workbook.createFont();
        titleFont.setFontName("Microsoft YaHei UI");
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 14);
        titleStyle.setFont(titleFont);

        // 表头样式
        CellStyle thStyle = workbook.createCellStyle();
        thStyle.cloneStyleFrom(titleStyle);
        thStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        thStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        Font thFont = workbook.createFont();
        thFont.setFontName("Microsoft YaHei UI");
        thFont.setBold(titleFont.getBold());
        thFont.setColor(IndexedColors.WHITE.getIndex());
        thStyle.setFont(thFont);

        // 创建标题样式、表格表头
        SXSSFRow titleRow = sheet.createRow(0);
        SXSSFRow thsRow = sheet.createRow(1);
        for (int i = 0; i < fields.size(); i++) {
            Excel excel = fields.get(i).getAnnotation(Excel.class);
            SXSSFCell title = titleRow.createCell(i);
            title.setCellStyle(titleStyle);
            SXSSFCell th = thsRow.createCell(i);
            th.setCellValue(excel.value());
            th.setCellStyle(thStyle);
        }

        // 绘制标题
        titleRow.setHeight((short) (26 * 20));
        SXSSFCell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(sheetTitle);
        titleCell.setCellStyle(titleStyle);

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, fields.size() - 1));
        return sheet;

    }

    public static List<Field> getExcelList(Class<?> entity, ExcelType type) {
        List<Field> list = new ArrayList<>();
        Field[] fields = entity.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Excel.class)) {
                ExcelType fieldType = field.getAnnotation(Excel.class).type();
                if (fieldType.equals(ExcelType.ALL) || fieldType.equals(type)) {
                    list.add(field);
                }
            }
        }
        return list;
    }

    /**
     * 获取实体类带有@Excel字段名
     */
    private static List<String> getFieldName(List<Field> fields) {
        List<String> list = new ArrayList<>();
        for (Field field : fields) {
            list.add(field.getName());
        }
        return list;
    }

    public static void download(SXSSFWorkbook workbook, String fileName) {
        try {
            fileName = URLEncoder.encode(fileName + ".xlsx", "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ServletRequestAttributes getServletRequest = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = getServletRequest.getResponse();
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
        OutputStream ros = null;
        try {
            ros = response.getOutputStream();
            workbook.write(ros);
            ros.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ros != null) {
                try {
                    ros.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
