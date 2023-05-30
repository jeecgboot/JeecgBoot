package org.jeecg.config.init;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * 自动初始化代码生成器模板
 * <p>
 * 解决JAR发布需要手工配置代码生成器模板问题
 * @author zhang
 */
@Slf4j
@Component
public class CodeTemplateInitListener implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            log.info(" Init Code Generate Template [ 检测如果是JAR启动环境，Copy模板到config目录 ] ");
            this.initJarConfigCodeGeneratorTemplate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ::Jar包启动模式下::
     * 初始化代码生成器模板文件
     */
    private void initJarConfigCodeGeneratorTemplate() throws Exception {
        //1.获取jar同级下的config路径
        String configPath = System.getProperty("user.dir") + File.separator + "config" + File.separator;
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath*:jeecg/code-template-online/**/*");
        for (Resource re : resources) {
            URL url = re.getURL();
            String filepath = url.getPath();
            //System.out.println("native url= " + filepath);
            filepath = java.net.URLDecoder.decode(filepath, "utf-8");
            //System.out.println("decode url= " + filepath);

            //2.在config下，创建jeecg/code-template-online/*模板
            String createFilePath = configPath + filepath.substring(filepath.indexOf("jeecg/code-template-online"));

            // 非jar模式不生成模板
            // 不生成目录，只生成具体模板文件
            if (!filepath.contains(".jar!/BOOT-INF/lib/") || !createFilePath.contains(".")) {
                continue;
            }
            if (!FileUtil.exist(createFilePath)) {
                log.info("create file codeTemplate = " + createFilePath);
                FileUtil.writeString(IOUtils.toString(url, StandardCharsets.UTF_8), createFilePath, "UTF-8");
            }
        }
    }
}
