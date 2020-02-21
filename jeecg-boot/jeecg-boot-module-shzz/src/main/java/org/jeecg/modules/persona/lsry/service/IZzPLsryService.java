package org.jeecg.modules.persona.lsry.service;

import org.jeecg.modules.persona.lsry.entity.ZzPLsryPersona;
import org.jeecg.modules.persona.lsry.entity.ZzPLsry;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 留守人员
 * @Author: jeecg-boot
 * @Date:   2020-02-21
 * @Version: V1.0
 */
public interface IZzPLsryService extends IService<ZzPLsry> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(ZzPLsry zzPLsry,List<ZzPLsryPersona> zzPLsryPersonaList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(ZzPLsry zzPLsry,List<ZzPLsryPersona> zzPLsryPersonaList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
