package com.xxl.job.admin.framework.util;

import com.xxl.job.core.constant.ExecutorBlockStrategyEnum;
import com.xxl.tool.core.PropTool;
import com.xxl.tool.freemarker.FtlTool;
import com.xxl.tool.json.GsonTool;
import freemarker.template.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * i18n util
 *
 * @author xuxueli 2018-01-17 20:39:06
 */
@Component
public class I18nUtil implements InitializingBean {
    private static Logger logger = LoggerFactory.getLogger(I18nUtil.class);

    // ---------------------- for i18n config ----------------------

    /**
     * i18n config
     */
    @Value("${xxl.job.i18n}")
    private String i18n;

    /**
     * freemarker config
     */
    @Autowired
    private Configuration configuration;

    @Override
    public void afterPropertiesSet() throws Exception {
        // init freemarker shared variable
        configuration.setSharedVariable("I18nUtil", FtlTool.generateStaticModel(I18nUtil.class.getName()));
        // init single
        single = this;

        // init i18n-enum
        initI18nEnum();
    }

    /**
     * get i18n
     */
    public String getI18n() {
        if (!Arrays.asList("zh_CN", "zh_TC", "en").contains(i18n)) {
            return "zh_CN";
        }
        return i18n;
    }

    private static I18nUtil single = null;
    private static I18nUtil getSingle() {
        return single;
    }

    // ---------------------- tool ----------------------

    private static Properties prop = null;
    public static Properties loadI18nProp(){
        if (prop != null) {
            return prop;
        }
        // build i18n filepath
        String i18n = getSingle().getI18n();
        String i18nFile = MessageFormat.format("i18n/message_{0}.properties", i18n);

        // load prop
        prop = PropTool.loadProp(i18nFile);
        return prop;
    }

    /**
     * get val of i18n key
     *
     * @param key
     * @return
     */
    public static String getString(String key) {
        return loadI18nProp().getProperty(key);
    }

    /**
     * get mult val of i18n mult key, as json
     *
     * @param keys
     * @return
     */
    public static String getMultString(String... keys) {
        Map<String, String> map = new HashMap<>();

        Properties prop = loadI18nProp();
        if (keys!=null && keys.length>0) {
            for (String key: keys) {
                map.put(key, prop.getProperty(key));
            }
        } else {
            for (String key: prop.stringPropertyNames()) {
                map.put(key, prop.getProperty(key));
            }
        }

        return GsonTool.toJson(map);
    }


    // ---------------------- init I18n-enum ----------------------

    /**
     * init i18n-enum
     */
    private void initI18nEnum(){
        for (ExecutorBlockStrategyEnum item : ExecutorBlockStrategyEnum.values()) {
            item.setTitle(I18nUtil.getString("jobconf_block_".concat(item.name())));
        }
    }

}
