package org.jeecg.modules.airag;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.airag.flow.component.enhance.IAiRagEnhanceJava;
import org.jeecg.modules.airag.wordtpl.entity.EoaWordTemplate;
import org.jeecg.modules.airag.wordtpl.service.IEoaWordTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: JavaAIFlow增强节点:生成在线word文档
 * @Author: chenrui
 * @Date: 2025-08-06 16:39
 */
@Slf4j
@Component("jeecgDemoAiWordGen")
public class TestAiGenWordEnhance implements IAiRagEnhanceJava {

    @Autowired
    IEoaWordTemplateService eoaWordTemplateService;

    @Override
    public Map<String, Object> process(Map<String, Object> inputParams) {
        Object resp = inputParams.get("resp");
        String respStr = String.valueOf(resp);
        log.info("AI生成word响应内容:{}", respStr);
        if(oConvertUtils.isEmpty(respStr)){
            throw new JeecgBootException("AI生成内容失败。请稍后再试或查看后台日志。");
        }
        String mainStr = null;
        Matcher matcher = Pattern.compile("\\[.*]", Pattern.DOTALL).matcher(respStr);
        if (matcher.find()) {
            mainStr = matcher.group();
            // 替换中文双引号为英文双引号
            mainStr = mainStr.replaceAll("[“”]", "\"");
            // 替换 NBSP 为普通空格
            mainStr = mainStr.replaceAll("\\u00A0", " ");
            
            log.info("生成word json:{}", mainStr);
            // 校验是否为合法 JSON 字符串
            try {
                JSON.parse(mainStr);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new JeecgBootException("AI生成的内容不是合法的 JSON 字符串，请稍后再试或优化提示词。");
            }
        }else{
            throw new JeecgBootException("AI生成的内容不是合法的 JSON 字符串，请稍后再试或优化提示词。");
        }

        EoaWordTemplate template = new EoaWordTemplate();
        String dateFormat = DateUtils.formatDate();
        template.setName("AI生成的简历_"+dateFormat);
        template.setCode("AI_GEN_"+System.currentTimeMillis());
        template.setHeader("[]");
        template.setFooter("[]");
        template.setMain(mainStr);
        template.setWidth(794);
        template.setHeight(1123);
        template.setMargins("[100,120,100,120]");
        template.setPaperDirection("vertical");
        eoaWordTemplateService.save(template);
        return Collections.singletonMap("result","success");
    }
}