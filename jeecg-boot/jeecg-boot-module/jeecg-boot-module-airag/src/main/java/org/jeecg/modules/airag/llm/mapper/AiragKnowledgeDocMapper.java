package org.jeecg.modules.airag.llm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.airag.llm.entity.AiragKnowledgeDoc;

/**
 * @Description: airag知识库文档
 * @Author: jeecg-boot
 * @Date:   2025-02-18
 * @Version: V1.0
 */
public interface AiragKnowledgeDocMapper extends BaseMapper<AiragKnowledgeDoc> {

	/**
	 * 通过主表id删除子表数据
	 *
	 * @param mainId 主表id
	 * @return boolean
	 */
	public boolean deleteByMainId(@Param("mainId") String mainId);

}
