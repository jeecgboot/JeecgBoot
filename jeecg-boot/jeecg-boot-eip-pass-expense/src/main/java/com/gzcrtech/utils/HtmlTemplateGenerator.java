package com.gzcrtech.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.util.ClassUtils;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 根据ftl模板，生成静态页面
 * @author Administrator
 *
 */
public class HtmlTemplateGenerator {
	@Resource
    Configuration cfg;
           
    /**     
     * 生成静态文件     
     * @param ftlTemplate ftl模版文件     
     * @param contents    ftl要用到的动态内容     
     * @param savePath    文件保存路径     
     * @param saveFilename 保存文件名     
     * @throws IOException     
     * @throws TemplateException     
     */      
    public  void create(String ftlTemplate, Map contents,  String savePath,String saveFilename) throws Exception{       
        try {
			Template temp = cfg.getTemplate(ftlTemplate);       
			/* Merge data model with template */      
			       
//        String realPath = FilePathUtils.getHtmlFilePath();  
			
			//测试代码 start
			String realPath = ClassUtils.getDefaultClassLoader().getResource("").getPath()+"/static";
			//测试代码 end
			
			realPath = realPath + savePath;
			FilePathUtils.dirChecker(realPath);
			
			System.out.println( realPath   + saveFilename);       
			File file = new File(realPath);       
			if(!file.exists())       
			    file.mkdirs();       
			       
			Writer out = new OutputStreamWriter(new FileOutputStream(realPath + "/" + saveFilename),"GBK");       
			temp.process(contents, out);       
			out.flush();    
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
    }  
    
    
    private void freeMarkerContent(Map<String,Object> root){
        try {
            Template temp = cfg.getTemplate("index.ftl");
            //以classpath下面的static目录作为静态页面的存储目录，同时命名生成的静态html文件名称
            String path=this.getClass().getResource("/").toURI().getPath()+"static/"+System.currentTimeMillis()+".html";
            Writer file = new FileWriter(new File(path.substring(path.indexOf("/"))));
            temp.process(root, file);
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
