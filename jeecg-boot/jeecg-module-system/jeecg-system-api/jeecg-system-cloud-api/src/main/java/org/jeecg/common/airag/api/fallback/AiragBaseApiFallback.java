package org.jeecg.common.airag.api.fallback;

import lombok.Setter;
import org.jeecg.common.airag.api.IAiragBaseApi;

public class AiragBaseApiFallback implements IAiragBaseApi {

    @Setter
    private Throwable cause;

    @Override
    public String knowledgeWriteTextDocument(String knowledgeId, String title, String content) {
        return null;
    }

}
