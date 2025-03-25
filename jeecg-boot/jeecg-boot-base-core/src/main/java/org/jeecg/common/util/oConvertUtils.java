package org.jeecg.common.util;

import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.SymbolConstant;
import org.springframework.beans.BeanUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.*;
import java.sql.Date;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @Author  张代浩
 *
 */
@Slf4j
public class oConvertUtils {
	public static boolean isEmpty(Object object) {
		if (object == null) {
			return (true);
		}
		if ("".equals(object)) {
			return (true);
		}
		if (CommonConstant.STRING_NULL.equals(object)) {
			return (true);
		}
		return (false);
	}
	
	public static boolean isNotEmpty(Object object) {
		if (object != null && !"".equals(object) && !object.equals(CommonConstant.STRING_NULL)) {
			return (true);
		}
		return (false);
	}

	
	/**
	 * 返回decode解密字符串
	 * 
	 * @param inStr
	 * @return
	 */
	public static String decodeString(String inStr) {
		if (oConvertUtils.isEmpty(inStr)) {
			return null;
		}

		try {
			inStr = URLDecoder.decode(inStr, "UTF-8");
		} catch (Exception e) {
			// 解决：URLDecoder: Illegal hex characters in escape (%) pattern - For input string: "自动"
			//e.printStackTrace();
		}
		return inStr;
	}

	public static String decode(String strIn, String sourceCode, String targetCode) {
		String temp = code2code(strIn, sourceCode, targetCode);
		return temp;
	}

	@SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
    public static String StrToUTF(String strIn, String sourceCode, String targetCode) {
		strIn = "";
		try {
			strIn = new String(strIn.getBytes("ISO-8859-1"), "GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strIn;

	}

	private static String code2code(String strIn, String sourceCode, String targetCode) {
		String strOut = null;
		if (strIn == null || "".equals(strIn.trim())) {
			return strIn;
		}
		try {
			byte[] b = strIn.getBytes(sourceCode);
			for (int i = 0; i < b.length; i++) {
				System.out.print(b[i] + "  ");
			}
			strOut = new String(b, targetCode);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return strOut;
	}

	public static int getInt(String s, int defval) {
		if (s == null || "".equals(s)) {
			return (defval);
		}
		try {
			return (Integer.parseInt(s));
		} catch (NumberFormatException e) {
			return (defval);
		}
	}

	public static int getInt(String s) {
		if (s == null || "".equals(s)) {
			return 0;
		}
		try {
			return (Integer.parseInt(s));
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public static int getInt(String s, Integer df) {
		if (s == null || "".equals(s)) {
			return df;
		}
		try {
			return (Integer.parseInt(s));
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public static Integer[] getInts(String[] s) {
		if (s == null) {
			return null;
		}
		Integer[] integer = new Integer[s.length];
		for (int i = 0; i < s.length; i++) {
			integer[i] = Integer.parseInt(s[i]);
		}
		return integer;

	}

	public static double getDouble(String s, double defval) {
		if (s == null || "".equals(s)) {
			return (defval);
		}
		try {
			return (Double.parseDouble(s));
		} catch (NumberFormatException e) {
			return (defval);
		}
	}

	public static double getDou(Double s, double defval) {
		if (s == null) {
			return (defval);
		}
		return s;
	}

	/*public static Short getShort(String s) {
		if (StringUtil.isNotEmpty(s)) {
			return (Short.parseShort(s));
		} else {
			return null;
		}
	}*/

	public static int getInt(Object object, int defval) {
		if (isEmpty(object)) {
			return (defval);
		}
		try {
			return (Integer.parseInt(object.toString()));
		} catch (NumberFormatException e) {
			return (defval);
		}
	}
	
	public static Integer getInteger(Object object, Integer defval) {
		if (isEmpty(object)) {
			return (defval);
		}
		try {
			return (Integer.parseInt(object.toString()));
		} catch (NumberFormatException e) {
			return (defval);
		}
	}
	
	public static Integer getInt(Object object) {
		if (isEmpty(object)) {
			return null;
		}
		try {
			return (Integer.parseInt(object.toString()));
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static int getInt(BigDecimal s, int defval) {
		if (s == null) {
			return (defval);
		}
		return s.intValue();
	}

	public static Integer[] getIntegerArry(String[] object) {
		int len = object.length;
		Integer[] result = new Integer[len];
		try {
			for (int i = 0; i < len; i++) {
				result[i] = new Integer(object[i].trim());
			}
			return result;
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static String getString(String s) {
		return (getString(s, ""));
	}

	/**
	 * 转义成Unicode编码
	 * @param s
	 * @return
	 */
	/*public static String escapeJava(Object s) {
		return StringEscapeUtils.escapeJava(getString(s));
	}*/
	
	public static String getString(Object object) {
		if (isEmpty(object)) {
			return "";
		}
		return (object.toString().trim());
	}

	public static String getString(int i) {
		return (String.valueOf(i));
	}

	public static String getString(float i) {
		return (String.valueOf(i));
	}

	/**
	 * 返回常规字符串（只保留字符串中的数字、字母、中文）
	 *
	 * @param input
	 * @return
	 */
	public static String getNormalString(String input) {
		if (oConvertUtils.isEmpty(input)) {
			return null;
		}
		String result = input.replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5]", "");
		return result;
	}

	public static String getString(String s, String defval) {
		if (isEmpty(s)) {
			return (defval);
		}
		return (s.trim());
	}

	public static String getString(Object s, String defval) {
		if (isEmpty(s)) {
			return (defval);
		}
		return (s.toString().trim());
	}

	public static long stringToLong(String str) {
		Long test = new Long(0);
		try {
			test = Long.valueOf(str);
		} catch (Exception e) {
		}
		return test.longValue();
	}

	/**
	 * 获取本机IP
	 */
	public static String getIp() {
		String ip = null;
		try {
			InetAddress address = InetAddress.getLocalHost();
			ip = address.getHostAddress();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return ip;
	}

	/**
	 * 判断一个类是否为基本数据类型。
	 * 
	 * @param clazz
	 *            要判断的类。
	 * @return true 表示为基本数据类型。
	 */
	private static boolean isBaseDataType(Class clazz) throws Exception {
		return (clazz.equals(String.class) || clazz.equals(Integer.class) || clazz.equals(Byte.class) || clazz.equals(Long.class) || clazz.equals(Double.class) || clazz.equals(Float.class) || clazz.equals(Character.class) || clazz.equals(Short.class) || clazz.equals(BigDecimal.class) || clazz.equals(BigInteger.class) || clazz.equals(Boolean.class) || clazz.equals(Date.class) || clazz.isPrimitive());
	}

	/**
	 * 解码base64
	 *
	 * @param base64Str base64字符串
	 * @return 被加密后的字符串
	 */
	public static String decodeBase64Str(String base64Str) {
		byte[] byteContent = Base64.decodeBase64(base64Str);
		if (byteContent == null) {
			return null;
		}
		String decodedString = new String(byteContent);
		return decodedString;
	}
	
	
	/**
	 * @param request
	 *            IP
	 * @return IP Address
	 */
	public static String getIpAddrByRequest(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || CommonConstant.UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || CommonConstant.UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || CommonConstant.UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * @return 本机IP
	 * @throws SocketException
	 */
	public static String getRealIp() throws SocketException {
        // 本地IP，如果没有配置外网IP则返回它
		String localip = null;
        // 外网IP
		String netip = null;

		Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
		InetAddress ip = null;
        // 是否找到外网IP
		boolean finded = false;
		while (netInterfaces.hasMoreElements() && !finded) {
			NetworkInterface ni = netInterfaces.nextElement();
			Enumeration<InetAddress> address = ni.getInetAddresses();
			while (address.hasMoreElements()) {
				ip = address.nextElement();
                // 外网IP
				if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
					netip = ip.getHostAddress();
					finded = true;
					break;
				} else if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
                    // 内网IP
				    localip = ip.getHostAddress();
				}
			}
		}

		if (netip != null && !"".equals(netip)) {
			return netip;
		} else {
			return localip;
		}
	}

	/**
	 * java去除字符串中的空格、回车、换行符、制表符
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
		    String reg = "\\s*|\t|\r|\n";
			Pattern p = Pattern.compile(reg);
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;

	}

	/**
	 * 判断元素是否在数组内
	 * 
	 * @param child
	 * @param all
	 * @return
	 */
	public static boolean isIn(String child, String[] all) {
		if (all == null || all.length == 0) {
			return false;
		}
		for (int i = 0; i < all.length; i++) {
			String aSource = all[i];
			if (aSource.equals(child)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断元素是否在数组内
	 *
	 * @param childArray
	 * @param all
	 * @return
	 */
	public static boolean isArrayIn(String[] childArray, String[] all) {
		if (all == null || all.length == 0) {
			return false;
		}
		for (String v : childArray) {
			if (!isIn(v, all)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断元素是否在数组内
	 *
	 * @param childArray
	 * @param all
	 * @return
	 */
	public static boolean isJsonArrayIn(JSONArray childArray, String[] all) {
		if (all == null || all.length == 0) {
			return false;
		}

		String[] childs = childArray.toArray(new String[]{});
		for (String v : childs) {
			if (!isIn(v, all)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 获取Map对象
	 */
	public static Map<Object, Object> getHashMap() {
		return new HashMap<>(5);
	}

	/**
	 * SET转换MAP
	 * 
	 * @param str
	 * @return
	 */
	public static Map<Object, Object> setToMap(Set<Object> setobj) {
		Map<Object, Object> map = getHashMap();
		for (Iterator iterator = setobj.iterator(); iterator.hasNext();) {
			Map.Entry<Object, Object> entry = (Map.Entry<Object, Object>) iterator.next();
			map.put(entry.getKey().toString(), entry.getValue() == null ? "" : entry.getValue().toString().trim());
		}
		return map;

	}

	public static boolean isInnerIp(String ipAddress) {
		boolean isInnerIp = false;
		long ipNum = getIpNum(ipAddress);
		/**
		 * 私有IP：A类 10.0.0.0-10.255.255.255 B类 172.16.0.0-172.31.255.255 C类 192.168.0.0-192.168.255.255 当然，还有127这个网段是环回地址
		 **/
		long aBegin = getIpNum("10.0.0.0");
		long aEnd = getIpNum("10.255.255.255");
		long bBegin = getIpNum("172.16.0.0");
		long bEnd = getIpNum("172.31.255.255");
		long cBegin = getIpNum("192.168.0.0");
		long cEnd = getIpNum("192.168.255.255");
		String localIp = "127.0.0.1";
		isInnerIp = isInner(ipNum, aBegin, aEnd) || isInner(ipNum, bBegin, bEnd) || isInner(ipNum, cBegin, cEnd) || localIp.equals(ipAddress);
		return isInnerIp;
	}

	private static long getIpNum(String ipAddress) {
		String[] ip = ipAddress.split("\\.");
		long a = Integer.parseInt(ip[0]);
		long b = Integer.parseInt(ip[1]);
		long c = Integer.parseInt(ip[2]);
		long d = Integer.parseInt(ip[3]);

		long ipNum = a * 256 * 256 * 256 + b * 256 * 256 + c * 256 + d;
		return ipNum;
	}

	private static boolean isInner(long userIp, long begin, long end) {
		return (userIp >= begin) && (userIp <= end);
	}
	
	/**
	 * 将下划线大写方式命名的字符串转换为驼峰式。
	 * 如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。</br>
	 * 例如：hello_world->helloWorld
	 * 
	 * @param name
	 *            转换前的下划线大写方式命名的字符串
	 * @return 转换后的驼峰式命名的字符串
	 */
	public static String camelName(String name) {
		StringBuilder result = new StringBuilder();
		// 快速检查
		if (name == null || name.isEmpty()) {
			// 没必要转换
			return "";
		} else if (!name.contains(SymbolConstant.UNDERLINE)) {
			// 不含下划线，仅将首字母小写
			//update-begin--Author:zhoujf  Date:20180503 for：TASK #2500 【代码生成器】代码生成器开发一通用模板生成功能
			//update-begin--Author:zhoujf  Date:20180503 for：TASK #2500 【代码生成器】代码生成器开发一通用模板生成功能
			return name.substring(0, 1).toLowerCase() + name.substring(1).toLowerCase();
			//update-end--Author:zhoujf  Date:20180503 for：TASK #2500 【代码生成器】代码生成器开发一通用模板生成功能
		}
		// 用下划线将原始字符串分割
		String[] camels = name.split("_");
		for (String camel : camels) {
			// 跳过原始字符串中开头、结尾的下换线或双重下划线
			if (camel.isEmpty()) {
				continue;
			}
			// 处理真正的驼峰片段
			if (result.length() == 0) {
				// 第一个驼峰片段，全部字母都小写
				result.append(camel.toLowerCase());
			} else {
				// 其他的驼峰片段，首字母大写
				result.append(camel.substring(0, 1).toUpperCase());
				result.append(camel.substring(1).toLowerCase());
			}
		}
		return result.toString();
	}
	
	/**
	 * 将下划线大写方式命名的字符串转换为驼峰式。
	 * 如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。</br>
	 * 例如：hello_world,test_id->helloWorld,testId
	 * 
	 * @param name
	 *            转换前的下划线大写方式命名的字符串
	 * @return 转换后的驼峰式命名的字符串
	 */
	public static String camelNames(String names) {
		if(names==null||"".equals(names)){
			return null;
		}
		StringBuffer sf = new StringBuffer();
		String[] fs = names.split(",");
		for (String field : fs) {
			field = camelName(field);
			sf.append(field + ",");
		}
		String result = sf.toString();
		return result.substring(0, result.length() - 1);
	}
	
	//update-begin--Author:zhoujf  Date:20180503 for：TASK #2500 【代码生成器】代码生成器开发一通用模板生成功能
	/**
	 * 将下划线大写方式命名的字符串转换为驼峰式。(首字母写)
	 * 如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。</br>
	 * 例如：hello_world->HelloWorld
	 * 
	 * @param name
	 *            转换前的下划线大写方式命名的字符串
	 * @return 转换后的驼峰式命名的字符串
	 */
	public static String camelNameCapFirst(String name) {
		StringBuilder result = new StringBuilder();
		// 快速检查
		if (name == null || name.isEmpty()) {
			// 没必要转换
			return "";
		} else if (!name.contains(SymbolConstant.UNDERLINE)) {
			// 不含下划线，仅将首字母小写
			return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
		}
		// 用下划线将原始字符串分割
		String[] camels = name.split("_");
		for (String camel : camels) {
			// 跳过原始字符串中开头、结尾的下换线或双重下划线
			if (camel.isEmpty()) {
				continue;
			}
			// 其他的驼峰片段，首字母大写
			result.append(camel.substring(0, 1).toUpperCase());
			result.append(camel.substring(1).toLowerCase());
		}
		return result.toString();
	}
	//update-end--Author:zhoujf  Date:20180503 for：TASK #2500 【代码生成器】代码生成器开发一通用模板生成功能
	
	/**
	 * 将驼峰命名转化成下划线
	 * @param para
	 * @return
	 */
	public static String camelToUnderline(String para){
	    int length = 3;
        if(para.length()<length){
        	return para.toLowerCase(); 
        }
        StringBuilder sb=new StringBuilder(para);
        //定位
        int temp=0;
        //从第三个字符开始 避免命名不规范 
        for(int i=2;i<para.length();i++){
            if(Character.isUpperCase(para.charAt(i))){
                sb.insert(i+temp, "_");
                temp+=1;
            }
        }
        return sb.toString().toLowerCase(); 
	}

	/**
	 * 随机数
	 * @param place 定义随机数的位数
	 */
	public static String randomGen(int place) {
		String base = "qwertyuioplkjhgfdsazxcvbnmQAZWSXEDCRFVTGBYHNUJMIKLOP0123456789";
		StringBuffer sb = new StringBuffer();
		Random rd = new Random();
		for(int i=0;i<place;i++) {
			sb.append(base.charAt(rd.nextInt(base.length())));
		}
		return sb.toString();
	}
	
	/**
	 * 获取类的所有属性，包括父类
	 * 
	 * @param object
	 * @return
	 */
	public static Field[] getAllFields(Object object) {
		Class<?> clazz = object.getClass();
		List<Field> fieldList = new ArrayList<>();
		while (clazz != null) {
			fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
			clazz = clazz.getSuperclass();
		}
		Field[] fields = new Field[fieldList.size()];
		fieldList.toArray(fields);
		return fields;
	}
	
	/**
	  * 将map的key全部转成小写
	 * @param list
	 * @return
	 */
	public static List<Map<String, Object>> toLowerCasePageList(List<Map<String, Object>> list){
		List<Map<String, Object>> select = new ArrayList<>();
		for (Map<String, Object> row : list) {
			 Map<String, Object> resultMap = new HashMap<>(5);
			 Set<String> keySet = row.keySet(); 
			 for (String key : keySet) { 
				 String newKey = key.toLowerCase(); 
				 resultMap.put(newKey, row.get(key)); 
			 }
			 select.add(resultMap);
		}
		return select;
	}

	/**
	 * 将entityList转换成modelList
	 * @param fromList
	 * @param tClass
	 * @param <F>
	 * @param <T>
	 * @return
	 */
	public static<F,T> List<T> entityListToModelList(List<F> fromList, Class<T> tClass){
		if(fromList == null || fromList.isEmpty()){
			return null;
		}
		List<T> tList = new ArrayList<>();
		for(F f : fromList){
			T t = entityToModel(f, tClass);
			tList.add(t);
		}
		return tList;
	}

	public static<F,T> T entityToModel(F entity, Class<T> modelClass) {
		log.debug("entityToModel : Entity属性的值赋值到Model");
		Object model = null;
		if (entity == null || modelClass ==null) {
			return null;
		}

		try {
			model = modelClass.newInstance();
		} catch (InstantiationException e) {
			log.error("entityToModel : 实例化异常", e);
		} catch (IllegalAccessException e) {
			log.error("entityToModel : 安全权限异常", e);
		}
		BeanUtils.copyProperties(entity, model);
		return (T)model;
	}

	/**
	 * 判断 list 是否为空
	 *
	 * @param list
	 * @return true or false
	 * list == null		: true
	 * list.size() == 0	: true
	 */
	public static boolean listIsEmpty(Collection list) {
		return (list == null || list.size() == 0);
	}

	/**
	 * 判断旧值与新值 是否相等
	 *
	 * @param oldVal
	 * @param newVal
	 * @return
	 */
	public static boolean isEqual(Object oldVal, Object newVal) {
		if (oldVal != null && newVal != null) {
			if (isArray(oldVal)) {
				return equalityOfArrays((Object[]) oldVal, (Object[]) newVal);
			}else if(oldVal instanceof JSONArray){
				if(newVal instanceof JSONArray){
					return equalityOfJSONArray((JSONArray) oldVal, (JSONArray) newVal);
				}else{
					if (isEmpty(newVal) && (oldVal == null || ((JSONArray) oldVal).size() == 0)) {
						return true;
					}
					List<Object> arrayStr = Arrays.asList(newVal.toString().split(","));
					JSONArray newValArray = new JSONArray(arrayStr);
					return equalityOfJSONArray((JSONArray) oldVal, newValArray);
				}
			}else{
				return oldVal.equals(newVal);
			}
			
		} else {
			if (oldVal == null && newVal == null) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * 方法描述 判断一个对象是否是一个数组
	 *
	 * @param obj
	 * @return
	 * @author yaomy
	 * @date 2018年2月5日 下午5:03:00
	 */
	public static boolean isArray(Object obj) {
		if (obj == null) {
			return false;
		}
		return obj.getClass().isArray();
	}

	/**
	 * 获取集合的大小
	 * 
	 * @param collection
	 * @return
	 */
	public static int getCollectionSize(Collection<?> collection) {
		return collection != null ? collection.size() : 0;
	}
	
	/**
	 * 判断两个数组是否相等（数组元素不分顺序）
	 *
	 * @param oldVal
	 * @param newVal
	 * @return
	 */
	public static boolean equalityOfJSONArray(JSONArray oldVal, JSONArray newVal) {
		if (oldVal != null && newVal != null) {
			Object[] oldValArray = oldVal.toArray();
			Object[] newValArray = newVal.toArray();
			return equalityOfArrays(oldValArray,newValArray);
		} else {
			if ((oldVal == null || oldVal.size() == 0) && (newVal == null || newVal.size() == 0)) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * 比较带逗号的字符串
	 * QQYUN-5212【简流】按日期触发 多选 人员组件 选择顺序不一致时 不触发，应该是统一问题 包括多选部门组件
	 * @param oldVal
	 * @param newVal
	 * @return
	 */
	public static boolean equalityOfStringArrays(String oldVal, String newVal) {
		if(oldVal.equals(newVal)){
			return true;
		}
		if(oldVal.indexOf(",")>=0 && newVal.indexOf(",")>=0){
			String[] arr1 = oldVal.split(",");
			String[] arr2 = newVal.split(",");
			if(arr1.length == arr2.length){
				boolean flag = true;
				Map<String, Integer> map = new HashMap<>();
				for(String s1: arr1){
					map.put(s1, 1);
				}
				for(String s2: arr2){
					if(map.get(s2) == null){
						flag = false;
						break;
					}
				}
				return flag;
			}
		}
		return false;
	}
	
	/**
	 * 判断两个数组是否相等（数组元素不分顺序）
	 *
	 * @param oldVal
	 * @param newVal
	 * @return
	 */
	public static boolean equalityOfArrays(Object[] oldVal, Object newVal[]) {
		if (oldVal != null && newVal != null) {
			Arrays.sort(oldVal);
			Arrays.sort(newVal);
			return Arrays.equals(oldVal, newVal);
		} else {
			if ((oldVal == null || oldVal.length == 0) && (newVal == null || newVal.length == 0)) {
				return true;
			} else {
				return false;
			}
		}
	}

//	public static void main(String[] args) {
////		String[] a = new String[]{"1", "2"};
////		String[] b = new String[]{"2", "1"};
//		Integer a = null;
//		Integer b = 1;
//		System.out.println(oConvertUtils.isEqual(a, b));
//	}
	
	/**
	 * 判断 list 是否不为空
	 *
	 * @param list
	 * @return true or false
	 * list == null		: false
	 * list.size() == 0	: false
	 */
	public static boolean listIsNotEmpty(Collection list) {
		return !listIsEmpty(list);
	}

	/**
	 * 读取静态文本内容
	 * @param url
	 * @return
	 */
	public static String readStatic(String url) {
		String json = "";
		try {
			//换个写法，解决springboot读取jar包中文件的问题
			InputStream stream = oConvertUtils.class.getClassLoader().getResourceAsStream(url.replace("classpath:", ""));
			json = IOUtils.toString(stream,"UTF-8");
		} catch (IOException e) {
			log.error(e.getMessage(),e);
		}
		return json;
	}

	/**
	 * 将List 转成 JSONArray
	 * @return
	 */
	public static JSONArray list2JSONArray(List<String> list){
		if(list==null || list.size()==0){
			return null;
		}
		JSONArray array = new JSONArray();
		for(String str: list){
			array.add(str);
		}
		return array;
	}

	/**
	 * 判断两个list中的元素是否完全一致
	 * QQYUN-5326【简流】获取组织人员 单/多 筛选条件 没有部门筛选
	 * @return
	 */
	public static boolean isEqList(List<String> list1, List<String> list2){
		if(list1.size() != list2.size()){
			return false;
		}
		for(String str1: list1){
			boolean flag = false;
			for(String str2: list2){
				if(str1.equals(str2)){
					flag = true;
					break;
				}
			}
			if(!flag){
				return false;
			}
		}
		return true;
	}


	/**
	 * 判断 list1中的元素是否在list2中出现
	 * QQYUN-5326【简流】获取组织人员 单/多 筛选条件 没有部门筛选
	 * @param list1
	 * @param list2
	 * @return
	 */
	public static boolean isInList(List<String> list1, List<String> list2){
		for(String str1: list1){
			boolean flag = false;
			for(String str2: list2){
				if(str1.equals(str2)){
					flag = true;
					break;
				}
			}
			if(flag){
				return true;
			}
		}
		return false;
	}

	/**
	 * 计算文件大小转成MB
	 * @param uploadCount
	 * @return
	 */
	public static Double calculateFileSizeToMb(Long uploadCount){
		double count = 0.0;
		if(uploadCount>0) {
			BigDecimal bigDecimal = new BigDecimal(uploadCount);
			//换算成MB
			BigDecimal divide = bigDecimal.divide(new BigDecimal(1048576));
			count = divide.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			return count;
		}
		return count;
	}

	/**
	 * map转str
	 *
	 * @param map
	 * @return
	 */
	public static String mapToString(Map<String, String[]> map) {
		if (map == null || map.size() == 0) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String[]> entry : map.entrySet()) {
			String key = entry.getKey();
			String[] values = entry.getValue();
			sb.append(key).append("=");
			sb.append(values != null ? StringUtils.join(values, ",") : "");
			sb.append("&");
		}

		String result = sb.toString();
		if (result.endsWith("&")) {
			result = result.substring(0, sb.length() - 1);
		}
		return result;
	}
	
}
