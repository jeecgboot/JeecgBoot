package org.jeecg.modules.system.service;

import java.util.List;
import java.util.Map;
import org.jeecg.modules.system.entity.SysDict;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 字典表 服务类
 * </p>
 *
 * @author zhangweijian
 * @since 2018-12-28
 */
public interface ISysDictService extends IService<SysDict> {
	public List<Map<String,String>> queryDictItemsByCode(String code);
	
	public String queryDictTextByKey(String code,String key);
}
