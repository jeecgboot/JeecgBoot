package org.jeecg.modules.airag.api;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.airag.api.IAiragBaseApi;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootBizTipException;
import org.jeecg.common.util.AssertUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.airag.app.entity.AiragApp;
import org.jeecg.modules.airag.app.service.IAiragAppService;
import org.jeecg.modules.airag.app.service.IAiragVariableService;
import org.jeecg.modules.airag.llm.consts.LLMConsts;
import org.jeecg.modules.airag.common.handler.IEmbeddingHandler;
import org.jeecg.modules.airag.llm.entity.AiragKnowledgeDoc;
import org.jeecg.modules.airag.llm.service.IAiragKnowledgeDocService;
import org.jeecg.modules.airag.prompts.entity.AiragPrompts;
import org.jeecg.modules.airag.prompts.service.IAiragPromptsService;
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

    @Autowired
    private IEmbeddingHandler embeddingHandler;

    @Override
    public String knowledgeWriteTextDocument(String knowledgeId, String title, String content, String segmentConfig) {
        AssertUtils.assertNotEmpty("知识库ID不能为空", knowledgeId);
        AssertUtils.assertNotEmpty("写入内容不能为空", content);
        AiragKnowledgeDoc knowledgeDoc = new AiragKnowledgeDoc();
        knowledgeDoc.setKnowledgeId(knowledgeId);
        knowledgeDoc.setTitle(title);
        knowledgeDoc.setType(LLMConsts.KNOWLEDGE_DOC_TYPE_TEXT);
        knowledgeDoc.setContent(content);
        // 将分段策略配置写入文档的metadata中，EmbeddingHandler会从中读取分段配置
        if (oConvertUtils.isNotEmpty(segmentConfig)) {
            knowledgeDoc.setMetadata(segmentConfig);
        }
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

    @Override
    public String checkKnowledgeDocsVectorizeStatus(String documentIds) {
        if (oConvertUtils.isEmpty(documentIds)) {
            return "COMPLETED";
        }
        List<String> idList = new ArrayList<>();
        for (String id : documentIds.split(",")) {
            String trimmed = id == null ? "" : id.trim();
            if (oConvertUtils.isNotEmpty(trimmed)) {
                idList.add(trimmed);
            }
        }
        if (idList.isEmpty()) {
            return "COMPLETED";
        }
        List<AiragKnowledgeDoc> docs = airagKnowledgeDocService.listByIds(idList);
        boolean hasFailed = false;
        boolean hasProcessing = false;
        Set<String> foundIds = new HashSet<>();
        for (AiragKnowledgeDoc doc : docs) {
            foundIds.add(doc.getId());
            String status = doc.getStatus();
            if (LLMConsts.KNOWLEDGE_DOC_STATUS_FAILED.equals(status)) {
                hasFailed = true;
            } else if (!LLMConsts.KNOWLEDGE_DOC_STATUS_COMPLETE.equals(status)) {
                hasProcessing = true;
            }
        }
        // 未查到的文档视为失败（可能被删除）
        for (String id : idList) {
            if (!foundIds.contains(id)) {
                hasFailed = true;
            }
        }
        if (hasProcessing) {
            return hasFailed ? "PROCESSING_WITH_FAIL" : "PROCESSING";
        }
        return hasFailed ? "COMPLETED_WITH_FAIL" : "COMPLETED";
    }

    @Override
    public String knowledgeWriteFileDocument(String knowledgeId, String title, String filePath, String segmentConfig) {
        AssertUtils.assertNotEmpty("知识库ID不能为空", knowledgeId);
        AssertUtils.assertNotEmpty("文件地址不能为空", filePath);
        AiragKnowledgeDoc knowledgeDoc = new AiragKnowledgeDoc();
        knowledgeDoc.setKnowledgeId(knowledgeId);
        knowledgeDoc.setTitle(title);
        knowledgeDoc.setType(LLMConsts.KNOWLEDGE_DOC_TYPE_FILE);
        // 文件类型文档将 filePath 放入 metadata，复用知识库文档功能的存储约定
        JSONObject metadata;
        if (oConvertUtils.isNotEmpty(segmentConfig)) {
            metadata = JSONObject.parseObject(segmentConfig);
        } else {
            metadata = new JSONObject();
        }
        metadata.put("filePath", filePath);
        knowledgeDoc.setMetadata(metadata.toJSONString());
        Result<?> result = airagKnowledgeDocService.editDocument(knowledgeDoc);
        if (!result.isSuccess()) {
            throw new JeecgBootBizTipException(result.getMessage());
        }
        if (knowledgeDoc.getId() == null) {
            throw new JeecgBootBizTipException("知识库文档ID为空");
        }
        log.info("[AI-KNOWLEDGE] 文件文档写入完成，知识库:{}, 文档ID:{}, 文件:{}", knowledgeId, knowledgeDoc.getId(), filePath);
        return knowledgeDoc.getId();
    }

    @Autowired
    private IAiragAppService airagAppService;

    @Autowired
    private IAiragVariableService airagVariableService;

    @Autowired
    private IAiragPromptsService airagPromptsService;

    @Override
    public String getChatVariable(String appId, String username, String name) {
        return airagVariableService.getVariable(username, appId, name);
    }

    @Override
    public void setChatVariable(String appId, String username, String name, String value) {
        AssertUtils.assertNotEmpty("应用ID不能为空", appId);
        AssertUtils.assertNotEmpty("用户名不能为空", username);
        AssertUtils.assertNotEmpty("变量名不能为空", name);
        airagVariableService.updateVariable(username, appId, name, value != null ? value : "");
    }

    @Override
    public String getMemoryIdByAppId(String appId) {
        if (oConvertUtils.isEmpty(appId)) {
            return null;
        }
        LambdaQueryWrapper<AiragApp> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AiragApp::getId, appId)
                .eq(AiragApp::getIzOpenMemory, 1)
                .isNotNull(AiragApp::getMemoryId)
                .ne(AiragApp::getMemoryId, "")
                .select(AiragApp::getMemoryId);
        AiragApp app = airagAppService.getOne(queryWrapper);
        return app != null ? app.getMemoryId() : null;
    }

    @Override
    public String getPromptContent(String promptId) {
        if (oConvertUtils.isEmpty(promptId)) {
            return null;
        }
        AiragPrompts prompt = airagPromptsService.getById(promptId);
        if (prompt == null) {
            log.warn("[AiragBaseApi]提示词不存在，promptId={}", promptId);
            return null;
        }
        return prompt.getContent();
    }

    @Override
    public String parseFileContent(String filePath) {
        AssertUtils.assertNotEmpty("文件地址不能为空", filePath);
        return embeddingHandler.parseFileContent(filePath);
    }

}
