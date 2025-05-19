package org.jeecg.modules.airag.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.airag.app.entity.AiragApp;

/**
 * @Description: AI应用
 * @Author: jeecg-boot
 * @Date:   2025-02-26
 * @Version: V1.0
 */
public interface IAiragAppService extends IService<AiragApp> {

    /**
     * 生成提示词
     * @param prompt
     * @return blocking 是否阻塞
     * @return
     * @author chenrui
     * @date 2025/3/12 14:45
     */
    Object generatePrompt(String prompt,boolean blocking);
}
