package org.jeecg.modules.airag.wordtpl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.constant.DataBaseConstant;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.AssertUtils;
import org.jeecg.modules.airag.wordtpl.dto.WordTplGenDTO;
import org.jeecg.modules.airag.wordtpl.entity.EoaWordTemplate;
import org.jeecg.modules.airag.wordtpl.mapper.EoaWordTemplateMapper;
import org.jeecg.modules.airag.wordtpl.service.IEoaWordTemplateService;
import org.jeecg.modules.airag.wordtpl.utils.WordTplUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Map;

/**
 * @Description: word模版管理
 * @Author: jeecg-boot
 * @Date:   2025-07-04
 * @Version: V1.0
 */
@Slf4j
@Service("eoaWordTemplateService")
public class EoaWordTemplateServiceImpl extends ServiceImpl<EoaWordTemplateMapper, EoaWordTemplate> implements IEoaWordTemplateService {

    /**
     * 内置的系统变量键列表
     */
    private static final String[] SYSTEM_KEYS = {
            DataBaseConstant.SYS_ORG_CODE, DataBaseConstant.SYS_ORG_CODE_TABLE, DataBaseConstant.SYS_MULTI_ORG_CODE,
            DataBaseConstant.SYS_MULTI_ORG_CODE_TABLE, DataBaseConstant.SYS_ORG_ID, DataBaseConstant.SYS_ORG_ID_TABLE,
            DataBaseConstant.SYS_ROLE_CODE, DataBaseConstant.SYS_ROLE_CODE_TABLE, DataBaseConstant.SYS_USER_CODE,
            DataBaseConstant.SYS_USER_CODE_TABLE, DataBaseConstant.SYS_USER_ID, DataBaseConstant.SYS_USER_ID_TABLE,
            DataBaseConstant.SYS_USER_NAME, DataBaseConstant.SYS_USER_NAME_TABLE, DataBaseConstant.SYS_DATE,
            DataBaseConstant.SYS_DATE_TABLE, DataBaseConstant.SYS_TIME, DataBaseConstant.SYS_TIME_TABLE,
            DataBaseConstant.SYS_BASE_PATH
    };

    @Autowired
    WordTplUtils wordTplUtils;

    @Override
    public void generateWordFromTpl(WordTplGenDTO wordTplGenDTO, ByteArrayOutputStream wordOutputStream) {
        AssertUtils.assertNotEmpty("参数异常", wordTplGenDTO);
        AssertUtils.assertNotEmpty("模版ID不能为空", wordTplGenDTO.getTemplateId());
        String templateId = wordTplGenDTO.getTemplateId();
        // 生成word模版 date:2025/7/10
        EoaWordTemplate template = getById(templateId);
        ByteArrayOutputStream wordTemplateOut = new ByteArrayOutputStream();
        wordTplUtils.generateWordTemplate(template, wordTemplateOut);
        //根据word模版和数据生成word文件
        Map<String, Object> data = wordTplGenDTO.getData();
        mergeSystemVarsToData(data);
        try {
            XWPFTemplate.compile(new ByteArrayInputStream(wordTemplateOut.toByteArray())).render(data).write(wordOutputStream);
        }catch (Exception e){
            log.error(e.getMessage(), e);
            throw new JeecgBootException("生成word文档失败，请检查模版和数据是否正确");
        }

    }

    /**
     * 将系统变量合并到数据中
     *
     * @param data
     * @author chenrui
     * @date 2025/7/3 17:43
     */
    private static void mergeSystemVarsToData(Map<String, Object> data) {
        for (String key : SYSTEM_KEYS) {
            if (!data.containsKey(key)) {
                String value = JwtUtil.getUserSystemData(key, null);
                if (value != null) {
                    data.put(key, value);
                }
            }
        }
    }
}