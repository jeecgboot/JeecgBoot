package org.jeecg.modules.fms.reimburse.biz.service;

import java.util.Map;

import org.jeecg.modules.fms.reimburse.biz.utils.VO.UnifyWorkItemVO;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 报销单基本信息
 * @Author: admin
 * @Date:   2020-02-17
 * @Version: V1.0
 */

public interface IUnfiyWorkItemListService extends IService<UnifyWorkItemVO> {
	
	public Page<UnifyWorkItemVO> pageUnfiyWorkItemList(Page<UnifyWorkItemVO> page,Map<String,String> paramsMap);

}
