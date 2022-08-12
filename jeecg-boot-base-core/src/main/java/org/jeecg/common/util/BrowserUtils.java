package org.jeecg.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @Author 张代浩
 * 
 */
public class BrowserUtils {

    /**
     * 判断是否是IE
     * @param request
     * @return
     */
	public static boolean isIe(HttpServletRequest request) {
		return (request.getHeader("USER-AGENT").toLowerCase().indexOf("msie") > 0 || request
				.getHeader("USER-AGENT").toLowerCase().indexOf("rv:11.0") > 0) ? true
				: false;
	}

	/**
	 * 获取IE版本
	 * 
	 * @param request
	 * @return
	 */
	public static Double getIeVersion(HttpServletRequest request) {
		Double version = 0.0;
		if (getBrowserType(request, IE11)) {
			version = 11.0;
		} else if (getBrowserType(request, IE10)) {
			version = 10.0;
		} else if (getBrowserType(request, IE9)) {
			version = 9.0;
		} else if (getBrowserType(request, IE8)) {
			version = 8.0;
		} else if (getBrowserType(request, IE7)) {
			version = 7.0;
		} else if (getBrowserType(request, IE6)) {
			version = 6.0;
		}
		return version;
	}

	/**
	 * 获取浏览器类型
	 * 
	 * @param request
	 * @return
	 */
	public static BrowserType getBrowserType(HttpServletRequest request) {
		BrowserType browserType = null;
		if (getBrowserType(request, IE11)) {
			browserType = BrowserType.IE11;
		}
		if (getBrowserType(request, IE10)) {
			browserType = BrowserType.IE10;
		}
		if (getBrowserType(request, IE9)) {
			browserType = BrowserType.IE9;
		}
		if (getBrowserType(request, IE8)) {
			browserType = BrowserType.IE8;
		}
		if (getBrowserType(request, IE7)) {
			browserType = BrowserType.IE7;
		}
		if (getBrowserType(request, IE6)) {
			browserType = BrowserType.IE6;
		}
		if (getBrowserType(request, FIREFOX)) {
			browserType = BrowserType.Firefox;
		}
		if (getBrowserType(request, SAFARI)) {
			browserType = BrowserType.Safari;
		}
		if (getBrowserType(request, CHROME)) {
			browserType = BrowserType.Chrome;
		}
		if (getBrowserType(request, OPERA)) {
			browserType = BrowserType.Opera;
		}
		if (getBrowserType(request, CAMINO)) {
			browserType = BrowserType.Camino;
		}
		return browserType;
	}

	private static boolean getBrowserType(HttpServletRequest request,
			String brosertype) {
		return request.getHeader("USER-AGENT").toLowerCase()
				.indexOf(brosertype) > 0 ? true : false;
	}

	private final static String IE11 = "rv:11.0";
	private final static String IE10 = "MSIE 10.0";
	private final static String IE9 = "MSIE 9.0";
	private final static String IE8 = "MSIE 8.0";
	private final static String IE7 = "MSIE 7.0";
	private final static String IE6 = "MSIE 6.0";
	private final static String MAXTHON = "Maxthon";
	private final static String QQ = "QQBrowser";
	private final static String GREEN = "GreenBrowser";
	private final static String SE360 = "360SE";
	private final static String FIREFOX = "Firefox";
	private final static String OPERA = "Opera";
	private final static String CHROME = "Chrome";
	private final static String SAFARI = "Safari";
	private final static String OTHER = "其它";
	private final static String CAMINO = "Camino";

	public static String checkBrowse(HttpServletRequest request) {
		String userAgent = request.getHeader("USER-AGENT");
		if (regex(OPERA, userAgent)) {
			return OPERA;
		}
		if (regex(CHROME, userAgent)) {
			return CHROME;
		}
		if (regex(FIREFOX, userAgent)) {
			return FIREFOX;
		}
		if (regex(SAFARI, userAgent)) {
			return SAFARI;
		}
		if (regex(SE360, userAgent)) {
			return SE360;
		}
		if (regex(GREEN, userAgent)) {
			return GREEN;
		}
		if (regex(QQ, userAgent)) {
			return QQ;
		}
		if (regex(MAXTHON, userAgent)) {
			return MAXTHON;
		}
		if (regex(IE11, userAgent)) {
			return IE11;
		}
		if (regex(IE10, userAgent)) {
			return IE10;
		}
		if (regex(IE9, userAgent)) {
			return IE9;
		}
		if (regex(IE8, userAgent)) {
			return IE8;
		}
		if (regex(IE7, userAgent)) {
			return IE7;
		}
		if (regex(IE6, userAgent)) {
			return IE6;
		}
		return OTHER;
	}

	public static boolean regex(String regex, String str) {
		Pattern p = Pattern.compile(regex, Pattern.MULTILINE);
		Matcher m = p.matcher(str);
		return m.find();
	}

	
	private static Map<String, String> langMap = new HashMap<String, String>();
	private final static String ZH = "zh";
	private final static String ZH_CN = "zh-cn";
	
	private final static String EN = "en";
	private final static String EN_US = "en";
	
	
	static 
	{
		langMap.put(ZH, ZH_CN);
		langMap.put(EN, EN_US);
	}
	
	public static String getBrowserLanguage(HttpServletRequest request) {
		
		String browserLang = request.getLocale().getLanguage();
		String browserLangCode = (String)langMap.get(browserLang);
		
		if(browserLangCode == null)
		{
			browserLangCode = EN_US;
		}
		return browserLangCode;
	}

    /** 判断请求是否来自电脑端 */
    public static boolean isDesktop(HttpServletRequest request) {
        return !isMobile(request);
    }

    /** 判断请求是否来自移动端 */
    public static boolean isMobile(HttpServletRequest request) {
        String ua = request.getHeader("User-Agent").toLowerCase();
        String type = "(phone|pad|pod|iphone|ipod|ios|ipad|android|mobile|blackberry|iemobile|mqqbrowser|juc|fennec|wosbrowser|browserng|webos|symbian|windows phone)";
        Pattern pattern = Pattern.compile(type);
        return pattern.matcher(ua).find();
    }

}
