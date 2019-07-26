package org.jeecg.modules.bjcl.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.bjcl.news.entity.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.util.Map;

@Service
public class FreeMarkerUtil {

    @Autowired
    private RedisUtil redisUtil;

    @Resource
    Configuration cfg;

    public void freeMarkerContent(Map<String,Object> root,String ftl,File file){
        try {
            Template temp = cfg.getTemplate(ftl);
            //以classpath下面的static目录作为静态页面的存储目录，同时命名生成的静态html文件名称
//            File newsFile = new File("E:/static/news/"+root.get("page")+".html");
            if(FileUtil.existsFile(file)){
                FileUtil.deleteIfExists(file);
            }
            Writer page = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            temp.process(root, page);
            page.flush();
            page.close();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (TemplateException e) {
            e.printStackTrace();
        }
    }
}
