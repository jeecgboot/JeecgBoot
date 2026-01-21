package org.jeecg.modules.airag.llm.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.airag.api.IAiragBaseApi;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootBizTipException;
import org.jeecg.common.util.AssertUtils;
import org.jeecg.modules.airag.llm.consts.LLMConsts;
import org.jeecg.modules.airag.llm.entity.AiragKnowledgeDoc;
import org.jeecg.modules.airag.llm.service.IAiragKnowledgeDocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * airag baseAPI 实现类
 */
@Slf4j
@Primary
@Service("airagBaseApiImpl")
public class AiragBaseApiImpl implements IAiragBaseApi {

    @Autowired
    private IAiragKnowledgeDocService airagKnowledgeDocService;

    @Override
    public String knowledgeWriteTextDocument(String knowledgeId, String title, String content) {
        AssertUtils.assertNotEmpty("知识库ID不能为空", knowledgeId);
        AssertUtils.assertNotEmpty("写入内容不能为空", content);
        AiragKnowledgeDoc knowledgeDoc = new AiragKnowledgeDoc();
        knowledgeDoc.setKnowledgeId(knowledgeId);
        knowledgeDoc.setTitle(title);
        knowledgeDoc.setType(LLMConsts.KNOWLEDGE_DOC_TYPE_TEXT);
        knowledgeDoc.setContent(content);
        Result<?> result = airagKnowledgeDocService.editDocument(knowledgeDoc);
        if (!result.isSuccess()) {
            throw new JeecgBootBizTipException(result.getMessage());
        }
        if (knowledgeDoc.getId() == null) {
            throw new JeecgBootBizTipException("知识库文档ID为空");
        }
        log.info("[AI-KNOWLEDGE] 文档写入完成，知识库:{}, 文档ID:{}", knowledgeId, knowledgeDoc.getId());
        return knowledgeDoc.getId();
    }
}
