package org.jeecg.modules.online.cgreport.service.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.jeecg.modules.online.cgreport.service.CgReportExcelServiceI;
import org.springframework.stereotype.Service;

/**
 * @Description: Excel导出工具类
 * @author: scott
 * @date: 2019-03-21
 * @version: V1.0
 */
@Service(value = "cgReportExcelService")
public class CgReportExcelServiceImpl implements CgReportExcelServiceI {

	@Override
	public HSSFWorkbook exportExcel(String title, Collection<?> titleSet, Collection<?> dataSet) {
		// 声明一个工作薄
		HSSFWorkbook workbook = null;
		try {
			// 首先检查数据看是否是正确的
			if (titleSet == null || titleSet.size() == 0) {
				throw new Exception("读取表头失败！");
			}
			if (title == null) {
				title = "";
			}
			// 声明一个工作薄
			workbook = new HSSFWorkbook();
			// 生成一个表格
			Sheet sheet = workbook.createSheet(title);
			int rindex = 0;
			int cindex = 0;
			// 产生表格标题行
			Row row = sheet.createRow(rindex);
			row.setHeight((short) 450);
			CellStyle titleStyle = getTitleStyle(workbook);
//			Iterator itTitle = titleSet.iterator();
			List<Map> titleList = (List<Map>) titleSet;
			Iterator itData = dataSet.iterator();
			// 遍历标题行
			for (Map titleM : titleList) {
				String titleContent = (String) titleM.get("field_txt");
				Cell cell = row.createCell(cindex);
				RichTextString text = new HSSFRichTextString(titleContent);
				cell.setCellValue(text);
				cell.setCellStyle(titleStyle);
				cindex++;
			}
			HSSFCellStyle bodyStyle = getOneStyle(workbook);
			// 遍历内容
			while (itData.hasNext()) {
				cindex = 0;
				rindex++;
				row = sheet.createRow(rindex);
				Map dataM = (Map) itData.next();// 获取每一行的内容
				for (Map titleM : titleList) {
					String field = (String) titleM.get("field_name");
					String content = dataM.get(field) == null ? "" : dataM.get(field).toString();
					Cell cell = row.createCell(cindex);
					RichTextString text = new HSSFRichTextString(content);
					cell.setCellStyle(bodyStyle);
					cell.setCellValue(text);
					cindex++;
				}
			}
			for (int i = 0; i < titleList.size(); i++) {
				sheet.autoSizeColumn(i);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return workbook;
	}

	/**
	 * exce表头单元格样式处理
	 * 
	 * @param workbook
	 * @return
	 */
	private HSSFCellStyle getTitleStyle(HSSFWorkbook workbook) {
		// 产生Excel表头
		HSSFCellStyle titleStyle = workbook.createCellStyle();
		titleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN); // 左边框
		titleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 右边框
		titleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 底边框
		titleStyle.setBorderTop(HSSFCellStyle.BORDER_THIN); // 顶边框
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		titleStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index); // 填充的背景颜色
		titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND); // 填充图案

		return titleStyle;
	}

	// 生成多少行的带有比边框的空行
	private void setBlankRows(int rows, int columns, HSSFWorkbook workbook) {
		// 得到第一页
		Sheet sheet = workbook.getSheetAt(0);
		// 样式
		CellStyle cellStyle = getOneStyle(workbook);
		for (int i = 1; i <= rows; i++) {
			Row row = sheet.createRow(i);
			for (int j = 0; j < columns; j++) {
				row.createCell(j).setCellStyle(cellStyle);
			}
		}
	}

	private HSSFCellStyle getTwoStyle(HSSFWorkbook workbook) {
		// 产生Excel表头
		HSSFCellStyle style = workbook.createCellStyle();
		style.setBorderLeft((short) 1); // 左边框
		style.setBorderRight((short) 1); // 右边框
		style.setBorderBottom((short) 1);
		style.setBorderTop((short) 1);
		style.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index); // 填充的背景颜色
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND); // 填充图案
		return style;
	}

	private HSSFCellStyle getOneStyle(HSSFWorkbook workbook) {
		// 产生Excel表头
		HSSFCellStyle style = workbook.createCellStyle();
		style.setBorderLeft((short) 1); // 左边框
		style.setBorderRight((short) 1); // 右边框
		style.setBorderBottom((short) 1);
		style.setBorderTop((short) 1);
		return style;
	}
}