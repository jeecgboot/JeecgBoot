package org.jeecg.modules.fms.reimburse.biz.mapper;

import java.util.List;
import org.jeecg.modules.fms.reimburse.biz.entity.ReimburseBizPaymentList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 报销单付款清单
 * @Author: jeecg-boot
 * @Date:   2020-01-08
 * @Version: V1.0
 */
public interface ReimburseBizPaymentListMapper extends BaseMapper<ReimburseBizPaymentList> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<ReimburseBizPaymentList> selectByMainId(@Param("mainId") String mainId);
	
	public Integer selectMaxSeqByApplyNo(@Param("applyNo") String applyNo);

}
