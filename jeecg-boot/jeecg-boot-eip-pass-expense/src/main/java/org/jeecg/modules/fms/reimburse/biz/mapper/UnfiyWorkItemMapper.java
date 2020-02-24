package org.jeecg.modules.fms.reimburse.biz.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.fms.reimburse.biz.utils.VO.UnifyWorkItemVO;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface UnfiyWorkItemMapper extends BaseMapper<UnifyWorkItemVO> {

	public List<UnifyWorkItemVO> selectUnifyWorkItemByUser(@Param("userCode") String userCode,@Param("applyNo") String applyNo,@Param("reimbursementItem") String reimbursementItem);
}
