package org.jeecg.modules.airag.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.airag.app.entity.AiragApp;
import org.jeecg.modules.airag.app.vo.AiArticleWriteVersionVo;
import java.util.List;

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

    /**
     * 根据应用id生成提示词
     *
     * @param variables
     * @param memoryId
     * @param blocking
     * @return
     */
    Object generateMemoryByAppId(String variables, String memoryId, boolean blocking);

    /**
     * 写作保存
     * 
     * @param aiWriteVersionVo
     */
    void saveArticleWrite(AiArticleWriteVersionVo aiWriteVersionVo);

    /**
     * 写作列表
     * 
     * @return
     */
    List<AiArticleWriteVersionVo> listArticleWrite();

    /**
     * 写作删除
     * 
     * @param version
     */
    void deleteArticleWrite(String version);

}
