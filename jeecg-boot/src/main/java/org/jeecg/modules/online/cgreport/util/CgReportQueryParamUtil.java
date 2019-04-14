package org.jeecg.modules.online.cgreport.util;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.online.cgreport.def.CgReportConstant;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @Title:QueryParamUtil
 * @description:动态报表查询条件处理工具
 * @author 赵俊夫
 * @date Jul 5, 2013 10:22:31 PM
 * @version V1.0
 */
public class CgReportQueryParamUtil{
	
	/**
	 * 组装查询参数
	 * @param request 请求(查询值从此处取)
	 * @param item 动态报表字段配置
	 * @param pageSearchFields 页面参数查询字段（占位符的条件语句）
	 * @param paramData 占位符对应的数据
	 */
	public static void loadQueryParams(HttpServletRequest request, Map<String,Object> item, Map<String,Object> pageSearchFields,Map<String,Object> paramData) {
		String filedName = (String) item.get(CgReportConstant.ITEM_FIELDNAME);
		String queryMode = (String) item.get(CgReportConstant.ITEM_QUERYMODE);
		String filedType = (String) item.get(CgReportConstant.ITEM_FIELDTYPE);
		if("single".equals(queryMode)){
			//单条件组装方式
			String value =request.getParameter(filedName.toLowerCase());
			try {
				if(oConvertUtils.isEmpty(value)){
					return;
				}
				String uri = request.getQueryString();
				if(uri.contains(filedName+"=")){
					String contiansChinesevalue = new String(value.getBytes("ISO-8859-1"), "UTF-8");
					value = contiansChinesevalue;
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return;
			} 
			if(oConvertUtils.isNotEmpty(value)){
				if(value.contains("*")){
					//模糊查询
					value = value.replaceAll("\\*", "%");
					pageSearchFields.put(filedName, CgReportConstant.OP_LIKE+":"+filedName);
				}else{
					pageSearchFields.put(filedName, CgReportConstant.OP_EQ+":"+filedName);
				}
			}
			paramData.put(filedName, covertData(filedType,value,true));
		}else if("group".equals(queryMode)){
			//范围查询组装
			String begin = request.getParameter(filedName.toLowerCase()+"_begin");
			String end = request.getParameter(filedName.toLowerCase()+"_end");
			if(oConvertUtils.isNotEmpty(begin)){
				String re = CgReportConstant.OP_RQ+":"+filedName+"_begin";
				pageSearchFields.put(filedName, re);
				paramData.put(filedName+"_begin", covertData(filedType,begin,true));
			} 
			if(oConvertUtils.isNotEmpty(end)){
				String re = CgReportConstant.OP_LQ+":"+filedName+"_end";
				pageSearchFields.put(new String(filedName), re);
				paramData.put(filedName+"_end", covertData(filedType,end,false));
			}
		}
	}
	
	private static Object covertData(String fieldType,String value,boolean isBegin){
		Object obj = null;
		if(oConvertUtils.isNotEmpty(value)){
			if(CgReportConstant.TYPE_STRING.equalsIgnoreCase(fieldType)){
				obj = value;
			}else if(CgReportConstant.TYPE_DATE.equalsIgnoreCase(fieldType)){
				if (value.length() == 19) {
				} else if (value.length() == 10) {
					if(isBegin){
						value +=" 00:00:00";
					}else{
						value +=" 23:59:59";
					}
				}
				SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				obj = DateUtils.str2Date(value, datetimeFormat);
			}else if(CgReportConstant.TYPE_DOUBLE.equalsIgnoreCase(fieldType)){
				obj = value;
			}else if(CgReportConstant.TYPE_INTEGER.equalsIgnoreCase(fieldType)){
				obj = value;
			}else{
				obj = value;
			}
		}
		return obj;
	}
	
	/**
	 * 将结果集转化为列表json格式
	 * @param result 结果集
	 * @param size 总大小
	 * @return 处理好的json格式
	 */
	public static String getJson(List<Map<String, Object>> result,Long size){
		JSONObject main = new JSONObject();
		JSONArray rows = new JSONArray();
		main.put("total",size );
		if(result!=null){
			for(Map m:result){
				JSONObject item = new JSONObject();
				Iterator  it =m.keySet().iterator();
				while(it.hasNext()){
					String key = (String) it.next();
					String value =String.valueOf(m.get(key));
					key = key.toLowerCase();
					if(key.contains("time")||key.contains("date")){
						value = datatimeFormat(value);
					}
					item.put(key,value );
				}
				rows.add(item);
			}
		}
		main.put("rows", rows);
		return main.toString();
	}
	
	/**
	 * 将结果集转化为列表json格式(不含分页信息)
	 * @param result 结果集
	 * @param size 总大小
	 * @return 处理好的json格式
	 */
	public static String getJson(List<Map<String, Object>> result){
		JSONArray rows = new JSONArray();
		for(Map m:result){
			JSONObject item = new JSONObject();
			Iterator  it =m.keySet().iterator();
			while(it.hasNext()){
				String key = (String) it.next();
				String value =String.valueOf(m.get(key));
				key = key.toLowerCase();
				if(key.contains("time")||key.contains("date")){
					value = datatimeFormat(value);
				}
				item.put(key,value );
			}
			rows.add(item);
		}
		return rows.toString();
	}
	
	/**
	 * 将毫秒数去掉
	 * @param datetime
	 * @return
	 */
	public static String datatimeFormat(String datetime){
		SimpleDateFormat  dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
		SimpleDateFormat  dateFormatTo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d  = null;
		try{
			d = dateFormat.parse(datetime);
			return dateFormatTo.format(d);
		}catch (Exception e) {
			return datetime;
		}
	}
}
