package org.jeecg.modules.fms.reimburse.biz.service;

import org.jeecg.modules.fms.reimburse.biz.entity.ReimburseBizBaseDetailInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 报销单基本明细
 * @Author: jeecg-boot
 * @Date:   2020-01-08
 * @Version: V1.0
 */
public interface IReimburseBizBaseDetailInfoService extends IService<ReimburseBizBaseDetailInfo> {

	public List<ReimburseBizBaseDetailInfo> selectByMainId(String mainId);
}
