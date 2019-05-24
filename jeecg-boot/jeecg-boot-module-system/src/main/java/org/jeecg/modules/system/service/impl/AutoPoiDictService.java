package org.jeecg.modules.system.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.mapper.SysDictMapper;
import org.jeecgframework.dict.service.AutoPoiDictServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * 描述：AutoPoi Excel注解支持字典参数设置
 *  举例： @Excel(name = "性别", width = 15, dicCode = "sex")
 * 1、导出的时候会根据字典配置，把值1,2翻译成：男、女;
 * 2、导入的时候，会把男、女翻译成1,2存进数据库;
 * 
 * @Author:scott 
 * @since：2019-04-09 
 * @Version:1.0
 */
@Slf4j
@Service
public class AutoPoiDictService implements AutoPoiDictServiceI {
	@Autowired
	private SysDictMapper sysDictMapper;

	/**
	 * 通过字典查询easypoi，所需字典文本
	 * 
	 * @Author:scott 
	 * @since：2019-04-09
	 * @return
	 */
	@Override
	public String[] queryDict(String dicTable, String dicCode, String dicText) {
		List<String> dictReplaces = new ArrayList<String>();
		List<DictModel> dictList = null;
		// step.1 如果没有字典表则使用系统字典表
		if (oConvertUtils.isEmpty(dicTable)) {
			dictList = sysDictMapper.queryDictItemsByCode(dicCode);
		} else {
			try {
				dicText = oConvertUtils.getString(dicText, dicCode);
				dictList = sysDictMapper.queryTableDictItemsByCode(dicTable, dicText, dicCode);
			} catch (Exception e) {
				log.error(e.getMessage(),e);
			}
		}
		for (DictModel t : dictList) {
			dictReplaces.add(t.getText() + "_" + t.getValue());
		}
		if (dictReplaces != null && dictReplaces.size() != 0) {
			log.info("---AutoPoi--Get_DB_Dict------"+ dictReplaces.toString());
			return dictReplaces.toArray(new String[dictReplaces.size()]);
		}
		return null;
	}
}
