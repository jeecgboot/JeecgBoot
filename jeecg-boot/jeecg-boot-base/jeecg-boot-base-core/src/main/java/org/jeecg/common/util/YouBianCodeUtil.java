package org.jeecg.common.util;

import io.netty.util.internal.StringUtil;

/**
 * 流水号生成规则(按默认规则递增，数字从1-99开始递增，数字到99，递增字母;位数不够增加位数)
 * A001
 * A001A002
 * @Author zhangdaihao
 *
 */
public class YouBianCodeUtil {

	// 数字位数(默认生成3位的数字)

    /**代表数字位数*/
	private static final int NUM_LENGTH = 2;

	public static final int ZHANWEI_LENGTH = 1+ NUM_LENGTH;

	public static final char LETTER= 'Z';

	/**
	 * 根据前一个code，获取同级下一个code
	 * 例如:当前最大code为D01A04，下一个code为：D01A05
	 * 
	 * @param code
	 * @return
	 */
	public static synchronized String getNextYouBianCode(String code) {
		String newcode = "";
		if (oConvertUtils.isEmpty(code)) {
			String zimu = "A";
			String num = getStrNum(1);
			newcode = zimu + num;
		} else {
			String beforeCode = code.substring(0, code.length() - 1- NUM_LENGTH);
			String afterCode = code.substring(code.length() - 1 - NUM_LENGTH,code.length());
			char afterCodeZimu = afterCode.substring(0, 1).charAt(0);
			Integer afterCodeNum = Integer.parseInt(afterCode.substring(1));
//			org.jeecgframework.core.util.LogUtil.info(after_code);
//			org.jeecgframework.core.util.LogUtil.info(after_code_zimu);
//			org.jeecgframework.core.util.LogUtil.info(after_code_num);

			String nextNum = "";
			char nextZimu = 'A';
			// 先判断数字等于999*，则计数从1重新开始，递增
			if (afterCodeNum == getMaxNumByLength(NUM_LENGTH)) {
				nextNum = getNextStrNum(0);
			} else {
				nextNum = getNextStrNum(afterCodeNum);
			}
			// 先判断数字等于999*，则字母从A重新开始,递增
			if(afterCodeNum == getMaxNumByLength(NUM_LENGTH)) {
				nextZimu = getNextZiMu(afterCodeZimu);
			}else{
				nextZimu = afterCodeZimu;
			}

			// 例如Z99，下一个code就是Z99A01
			if (LETTER == afterCodeZimu && getMaxNumByLength(NUM_LENGTH) == afterCodeNum) {
				newcode = code + (nextZimu + nextNum);
			} else {
				newcode = beforeCode + (nextZimu + nextNum);
			}
		}
		return newcode;

	}

	/**
	 * 根据父亲code,获取下级的下一个code
	 * 
	 * 例如：父亲CODE:A01
	 *       当前CODE:A01B03
	 *       获取的code:A01B04
	 *       
	 * @param parentCode   上级code
	 * @param localCode    同级code
	 * @return
	 */
	public static synchronized String getSubYouBianCode(String parentCode,String localCode) {
		if(localCode!=null && localCode!=""){

//			return parentCode + getNextYouBianCode(localCode);
			return getNextYouBianCode(localCode);

		}else{
			parentCode = parentCode + "A"+ getNextStrNum(0);
		}
		return parentCode;
	}

	

	/**
	 * 将数字前面位数补零
	 * 
	 * @param num
	 * @return
	 */
	private static String getNextStrNum(int num) {
		return getStrNum(getNextNum(num));
	}

	/**
	 * 将数字前面位数补零
	 * 
	 * @param num
	 * @return
	 */
	private static String getStrNum(int num) {
		String s = String.format("%0" + NUM_LENGTH + "d", num);
		return s;
	}

	/**
	 * 递增获取下个数字
	 * 
	 * @param num
	 * @return
	 */
	private static int getNextNum(int num) {
		num++;
		return num;
	}

	/**
	 * 递增获取下个字母
	 * 
	 * @param num
	 * @return
	 */
	private static char getNextZiMu(char zimu) {
		if (zimu == LETTER) {
			return 'A';
		}
		zimu++;
		return zimu;
	}
	
	/**
	 * 根据数字位数获取最大值
	 * @param length
	 * @return
	 */
	private static int getMaxNumByLength(int length){
		if(length==0){
			return 0;
		}
        StringBuilder maxNum = new StringBuilder();
		for (int i=0;i<length;i++){
            maxNum.append("9");
		}
		return Integer.parseInt(maxNum.toString());
	}
	public static String[] cutYouBianCode(String code){
		if(code==null || StringUtil.isNullOrEmpty(code)){
			return null;
		}else{
			//获取标准长度为numLength+1,截取的数量为code.length/numLength+1
			int c = code.length()/(NUM_LENGTH +1);
			String[] cutcode = new String[c];
			for(int i =0 ; i <c;i++){
				cutcode[i] = code.substring(0,(i+1)*(NUM_LENGTH +1));
			}
			return cutcode;
		}
		
	}
//	public static void main(String[] args) {
//		// org.jeecgframework.core.util.LogUtil.info(getNextZiMu('C'));
//		// org.jeecgframework.core.util.LogUtil.info(getNextNum(8));
//	    // org.jeecgframework.core.util.LogUtil.info(cutYouBianCode("C99A01B01")[2]);
//	}
}
