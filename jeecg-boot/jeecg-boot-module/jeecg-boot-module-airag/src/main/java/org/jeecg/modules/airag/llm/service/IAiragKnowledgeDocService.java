package org.jeecg.modules.airag.llm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.airag.llm.entity.AiragKnowledgeDoc;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * airag知识库文档
 * @Author: jeecg-boot
 * @Date: 2025-02-18
 * @Version: V1.0
 */
public interface IAiragKnowledgeDocService extends IService<AiragKnowledgeDoc> {

    /**
     * 重建文档
     *
     * @param docIds
     * @return
     * @author chenrui
     * @date 2025/2/18 11:14
     */
    Result<?> rebuildDocument(String docIds);

    /**
     * 添加文档
     *
     * @param airagKnowledgeDoc
     * @return
     * @author chenrui
     * @date 2025/2/18 15:30
     */
    Result<?> editDocument(AiragKnowledgeDoc airagKnowledgeDoc);


    /**
     * 通过知识库id重建文档
     *
     * @param knowId
     * @return
     * @author chenrui
     * @date 2025/2/18 18:54
     */
    Result<?> rebuildDocumentByKnowId(String knowId);


    /**
     * 通过知识库id删除文档
     *
     * @param knowIds
     * @return
     * @author chenrui
     * @date 2025/2/18 18:59
     */
    Result<?> removeByKnowIds(List<String> knowIds);

    /**
     * 通过文档id批量删除文档
     *
     * @param docIds
     * @return
     * @author chenrui
     * @date 2025/2/18 19:16
     */
    Result<?> removeDocByIds(List<String> docIds);

    /**
     * 从zip包导入文档
     * @param knowId
     * @param file
     * @return
     * @author chenrui
     * @date 2025/3/20 13:50
     */
    Result<?> importDocumentFromZip(String knowId, MultipartFile file);
}
