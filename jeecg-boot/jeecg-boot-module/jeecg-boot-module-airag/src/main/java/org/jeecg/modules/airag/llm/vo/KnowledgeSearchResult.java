package org.jeecg.modules.airag.llm.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 知识库查询返回结果
 *
 * @Author: chenrui
 * @Date: 2025/2/18 17:53
 */
@Data
@NoArgsConstructor
public class KnowledgeSearchResult {

    /**
     * 命中的文档内容
     */
    String data;

    /**
     * 命中的文档列表
     */
    List<Map<String, Object>> documents;

    public KnowledgeSearchResult(String data, List<Map<String, Object>> documents) {
        this.data = data;
        this.documents = documents;
    }
}
