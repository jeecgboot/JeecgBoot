package org.jeecg.modules.fms.reimburse.biz.service.impl;

import java.util.List;
import java.util.Map;

import org.jeecg.modules.fms.reimburse.biz.mapper.UnfiyWorkItemMapper;
import org.jeecg.modules.fms.reimburse.biz.service.IUnfiyWorkItemListService;
import org.jeecg.modules.fms.reimburse.biz.utils.VO.UnifyWorkItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service
@DS("reimbursement")
public class UnifyWorkItemListServiceImpl  extends ServiceImpl<UnfiyWorkItemMapper, UnifyWorkItemVO> implements IUnfiyWorkItemListService {

	@Autowired
	private UnfiyWorkItemMapper unifyWorkItemMapper;
	
	@Override
	public Page<UnifyWorkItemVO> pageUnfiyWorkItemList(Page<UnifyWorkItemVO> page, Map<String, String> paramsMap) {
		String userCode = paramsMap.get("userCode");
		String applyNo = paramsMap.get("applyNo");
		String reimbursementItem = paramsMap.get("reimbursementItem");
		List<UnifyWorkItemVO> recordList = unifyWorkItemMapper.selectUnifyWorkItemByUser(userCode,applyNo,reimbursementItem);
		return page.setRecords(recordList);
	}
	
}
