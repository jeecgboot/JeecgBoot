package org.jeecg.modules.fms.reimburse.biz.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.fms.reimburse.biz.entity.ReimburseBizMainInfo;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 报销单基本信息
 * @Author: jeecg-boot
 * @Date:   2020-01-08
 * @Version: V1.0
 */
public interface ReimburseBizMainInfoMapper extends BaseMapper<ReimburseBizMainInfo> {
	
	public List<ReimburseBizMainInfo> selectTodayMaxRmnoByOrgcode(@Param("today") String today,@Param("orgCode") String orgCode,@Param("rbsTemplate") String rbsTemplate);
	
	public List<ReimburseBizMainInfo> selectRmnoByOrgcode(@Param("orgCode") String orgCode);
	
	public ReimburseBizMainInfo getReimburseBizMainInfoByApplyNo(@Param("applyNo") String applyNo);

}
