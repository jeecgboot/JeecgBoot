package org.jeecg.modules.online.cgreport.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeecg.modules.online.cgreport.entity.OnlCgreportItem;
import org.jeecg.modules.online.cgreport.mapper.OnlCgreportItemMapper;
import org.jeecg.modules.online.cgreport.service.IOnlCgreportItemService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 在线报表配置
 * @author: jeecg-boot
 * @date: 2019-03-08
 * @version: V1.0
 */
@Service
public class OnlCgreportItemServiceImpl extends ServiceImpl<OnlCgreportItemMapper, OnlCgreportItem> implements IOnlCgreportItemService {

	/**
	 * 获取online报表查询条件
	 */
	@Override
	public List<Map<String, String>> getAutoListQueryInfo(String cgrheadId) {
		LambdaQueryWrapper<OnlCgreportItem> query = new LambdaQueryWrapper<OnlCgreportItem>();
		query.eq(OnlCgreportItem::getCgrheadId, cgrheadId);
		query.eq(OnlCgreportItem::getIsSearch,1);
		List<OnlCgreportItem> fieldList =  this.list(query);
		List<Map<String, String>> list = new ArrayList<>();
		int i=0;
		for (OnlCgreportItem item : fieldList) {
			Map<String,String> map = new HashMap<>();
			map.put("label",item.getFieldTxt());
			map.put("view",item.getFieldType());
			map.put("mode",item.getSearchMode());
			map.put("field",item.getFieldName());
			if((++i)>2) {
				map.put("hidden","1");
			}
			list.add(map);
		}
		return list;
	}

}
