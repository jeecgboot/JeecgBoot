package org.jeecg.modules.online.cgreport.service;


import java.util.Collection;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * 
 * @Title:CgReportExcelServiceI
 * @description:动态报表excel导出
 * @author scott
 * @date 2019-03-21
 * @version V1.0
 */
public interface CgReportExcelServiceI{
	/**
	 * 
	 * @param title 标题
	 * @param titleSet	报表头
	 * @param dataSet	报表内容
	 * @return
	 */
	public HSSFWorkbook exportExcel(String title, Collection<?> titleSet,Collection<?> dataSet);
}
