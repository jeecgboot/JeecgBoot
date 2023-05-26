package org.jeecg.modules.order.service.impl;

import org.jeecg.modules.order.entity.ResponseOrder;
import org.jeecg.modules.order.mapper.ResponseOrderMapper;
import org.jeecg.modules.order.service.IResponseOrderService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: Response Order
 * @Author: jeecg-boot
 * @Date:   2021-05-05
 * @Version: V1.0
 */
@Service
public class ResponseOrderServiceImpl extends ServiceImpl<ResponseOrderMapper, ResponseOrder> implements IResponseOrderService {
	
	@Autowired
	private ResponseOrderMapper responseOrderMapper;
	
	@Override
	public List<ResponseOrder> selectByMainId(String mainId) {
		return responseOrderMapper.selectByMainId(mainId);
	}
}
