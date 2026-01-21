package org.jeecg.modules.airag.wordtpl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.airag.wordtpl.dto.WordTplGenDTO;
import org.jeecg.modules.airag.wordtpl.entity.EoaWordTemplate;

import java.io.ByteArrayOutputStream;

/**
 * @Description: word模版管理
 * @Author: jeecg-boot
 * @Date:   2025-07-04
 * @Version: V1.0
 */
public interface IEoaWordTemplateService extends IService<EoaWordTemplate> {

    /**
     * 通过模版生成word文档
     *
     * @param wordTplGenDTO
     * @return
     * @author chenrui
     * @date 2025/7/10 14:40
     */
    void generateWordFromTpl(WordTplGenDTO wordTplGenDTO, ByteArrayOutputStream wordOutputStream);
}
