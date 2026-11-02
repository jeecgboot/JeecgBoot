package org.jeecg.common.airag.api.fallback;

import lombok.Setter;
import org.jeecg.common.airag.api.IAiragBaseApi;

public class AiragBaseApiFallback implements IAiragBaseApi {

    @Setter
    private Throwable cause;

    @Override
    public String knowledgeWriteTextDocument(String knowledgeId, String title, String content, String segmentConfig) {
        return null;
    }

    @Override
    public String knowledgeWriteFileDocument(String knowledgeId, String title, String filePath, String segmentConfig) {
        return null;
    }

    @Override
    public String checkKnowledgeDocsVectorizeStatus(String documentIds) {
        return null;
    }

    @Override
    public String getChatVariable(String appId, String username, String name) {
        return null;
    }

    @Override
    public void setChatVariable(String appId, String username, String name, String value) {
    }

    @Override
    public String getMemoryIdByAppId(String appId) {
        return null;
    }

    @Override
    public String getPromptContent(String promptId) {
        return null;
    }

    @Override
    public String parseFileContent(String filePath) {
        return null;
    }

}
